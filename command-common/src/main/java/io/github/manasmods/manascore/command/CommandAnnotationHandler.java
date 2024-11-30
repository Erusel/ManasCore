/*
 * Copyright (c) 2024. ManasMods
 * GNU General Public License 3
 */

package io.github.manasmods.manascore.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.github.manasmods.manascore.command.api.Command;
import io.github.manasmods.manascore.command.api.Execute;
import io.github.manasmods.manascore.command.api.Permission;
import io.github.manasmods.manascore.command.api.parameter.Enum;
import io.github.manasmods.manascore.command.api.parameter.Literal;
import io.github.manasmods.manascore.command.api.parameter.Sender;
import io.github.manasmods.manascore.command.api.parameter.Uuid;
import lombok.RequiredArgsConstructor;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

@ApiStatus.Internal
public class CommandAnnotationHandler {
    static final List<LiteralArgumentBuilder<CommandSourceStack>> COMMANDS = new ArrayList<>();

    public static <T> void registerCommand(Class<T> commandClass, Supplier<T> factory) {
        var rootAnnotation = requireCommandAnnotation(commandClass);
        validateSubCommands(rootAnnotation);

        CommandNode rootNode = new CommandNode(
                commandClass,
                factory.get(),
                rootAnnotation.value(),
                Arrays.stream(commandClass.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(Execute.class)),
                rootAnnotation.subCommands()
        );

        try {
            COMMANDS.addAll(rootNode.build());
            rootNode.getPermissions().forEach(PlatformCommandUtils::registerPermission);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Require the @Command annotation on a class
     */
    public static Command requireCommandAnnotation(Class<?> commandClass) {
        if (!commandClass.isAnnotationPresent(Command.class)) {
            throw new IllegalArgumentException(String.format("Class %s must be annotated with @Command", commandClass.getName()));
        }

        var commandAnnotation = commandClass.getAnnotation(Command.class);

        if (commandAnnotation.value().length == 0) {
            throw new IllegalArgumentException(String.format("Command Annotation at Class %s must have at least one root node", commandClass.getName()));
        }

        return commandAnnotation;
    }

    /**
     * Recursively validate subcommand classes
     */
    public static void validateSubCommands(Command commandAnnotation) {
        for (var subCommandClass : commandAnnotation.subCommands()) {
            var subCommandAnnotation = requireCommandAnnotation(subCommandClass);
            validateSubCommands(subCommandAnnotation);
        }
    }

    @RequiredArgsConstructor
    public static class CommandNode {
        private final Class<?> commandClass;
        private final Object commandClassInstance;
        private final String[] nodeLiterals;
        private final Stream<Method> executors;
        private final Class<?>[] subCommandClasses;
        private final Map<String, Permission> permissionNodes = new HashMap<>();


        public List<LiteralArgumentBuilder<CommandSourceStack>> build() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            AtomicReference<Consumer<LiteralArgumentBuilder<CommandSourceStack>>> rootExecutor = new AtomicReference<>(builder -> {
            });
            // Create ArgumentBuilders for each executor in the command class
            var arguments = executors.flatMap(method -> {
                        if (!method.getReturnType().isAssignableFrom(boolean.class) && !method.getReturnType().isAssignableFrom(Boolean.class)) {
                            throw new RuntimeException("Method %s in %s has a return type that is not a boolean".formatted(method.getName(), this.commandClass.getName()));
                        }

                        var types = method.getParameterTypes();
                        var parameters = method.getParameters();
                        var allowsConsole = true;
                        // Node hierarchy (Ordered from first param -> last param)
                        List<List<ArgumentBuilder<CommandSourceStack, ?>>> nodeArgumentHierarchy = new ArrayList<>();
                        // List of ArgumentBuilders for each parameter
                        List<ArgumentBuilder<CommandSourceStack, ?>> lastParameterNodes = new ArrayList<>();
                        // List of ParameterSuppliers for each parameter (out list index = parameter index, inner list index = argument index)
                        List<List<ParameterSupplier<Object>>> parameterArgumentSuppliers = new ArrayList<>();

                        for (int i = 0; i < types.length; i++) {
                            final var argumentType = types[i];
                            final var parameter = parameters[i];
                            final List<ArgumentBuilder<CommandSourceStack, ?>> newNodes = new ArrayList<>();
                            boolean isSenderArgument = false;

                            if (parameter.isAnnotationPresent(Literal.class)) {
                                ManasCoreCommand.LOG.debug("Found Literal Parameter in Method {} in {} at index {}", method.getName(), this.commandClass.getName(), i);
                                if (!argumentType.isAssignableFrom(String.class)) {
                                    throw new RuntimeException("Method %s in %s has a parameter annotated with @Literal but is not a String".formatted(method.getName(), this.commandClass.getName()));
                                }
                                var literals = parameter.getAnnotation(Literal.class);
                                for (String literal : literals.value()) {
                                    int index = newNodes.size();
                                    newNodes.add(Commands.literal(literal));
                                    addValueTo2DList(parameterArgumentSuppliers, index, i, commandContext -> literal);
                                }
                            }

                            if (parameter.isAnnotationPresent(Sender.class)) {
                                ManasCoreCommand.LOG.debug("Found Sender Parameter in Method {} in {} at index {}", method.getName(), this.commandClass.getName(), i);
                                if (argumentType.isAssignableFrom(CommandSourceStack.class)) {
                                    addValueTo2DList(parameterArgumentSuppliers, CommandContext::getSource);
                                } else if (argumentType.isAssignableFrom(ServerPlayer.class)) {
                                    addValueTo2DList(parameterArgumentSuppliers, commandContext -> commandContext.getSource().getPlayerOrException());
                                    allowsConsole = false;
                                } else if (argumentType.isAssignableFrom(CommandSource.class)) {
                                    addValueTo2DList(parameterArgumentSuppliers, commandContext -> commandContext.getSource().source);
                                } else {
                                    throw new RuntimeException("Method %s in %s has a parameter annotated with @Sender but is not a ServerPlayer, CommandSourceStack or CommandSource".formatted(method.getName(), this.commandClass.getName()));
                                }
                                isSenderArgument = true;
                            }

                            if (parameter.isAnnotationPresent(Enum.class)) {
                                ManasCoreCommand.LOG.debug("Found Enum Parameter in Method {} in {} at index {}", method.getName(), this.commandClass.getName(), i);
                                if (!argumentType.isEnum()) {
                                    throw new RuntimeException("Method %s in %s has a parameter annotated with @Enum but is not an Enum".formatted(method.getName(), this.commandClass.getName()));
                                }

                                var enumAnnotation = parameter.getAnnotation(Enum.class);
                                var enumClass = enumAnnotation.value();

                                for (var constant : enumClass.getEnumConstants()) {
                                    int index = newNodes.size();
                                    newNodes.add(Commands.literal(constant.name()));
                                    addValueTo2DList(parameterArgumentSuppliers, index, i, commandContext -> constant);
                                }
                            }

                            if (parameter.isAnnotationPresent(Uuid.class)) {
                                ManasCoreCommand.LOG.debug("Found Uuid Parameter in Method {} in {} at index {}", method.getName(), this.commandClass.getName(), i);
                                if (!argumentType.isAssignableFrom(UUID.class)) {
                                    throw new RuntimeException("Method %s in %s has a parameter annotated with @Uuid but is not a UUID".formatted(method.getName(), this.commandClass.getName()));
                                }
                                var uuidAnnotation = parameter.getAnnotation(Uuid.class);
                                var argumentName = uuidAnnotation.value().isBlank() ? parameter.getName() : uuidAnnotation.value();
                                int index = newNodes.size();
                                newNodes.add(Commands.argument(argumentName, StringArgumentType.word()));
                                addValueTo2DList(parameterArgumentSuppliers, index, i, commandContext -> {
                                    var uuidString = StringArgumentType.getString(commandContext, argumentName);
                                    try {
                                        return UUID.fromString(uuidString);
                                    } catch (Exception e) {
                                        throw new CommandSyntaxException(new SimpleCommandExceptionType(e::getMessage), e::getMessage);
                                    }
                                });
                            }

                            if (newNodes.isEmpty() && !isSenderArgument) {
                                throw new RuntimeException("Parameter at index %s in Method %s in %s is not annotated".formatted(i, method.getName(), this.commandClass.getName()));
                            }

                            // Set the new node as the last node
                            if (!isSenderArgument) {
                                lastParameterNodes = newNodes;
                                nodeArgumentHierarchy.add(newNodes);
                            }
                        }

                        // No parameters found
                        if (nodeArgumentHierarchy.isEmpty()) {
                            if (parameterArgumentSuppliers.size() == 1) {
                                rootExecutor.set(builder -> commandExecuteApplier(builder, method, parameterArgumentSuppliers.get(0)));
                            }

                            return Stream.empty();
                        }
                        ;

                        // Add Permission check and execution to the last node
                        final var allowsConsoleFinal = allowsConsole;
                        for (int i = 0; i < lastParameterNodes.size(); i++) {
                            var builder = lastParameterNodes.get(i);
                            final var argumentSuppliers = parameterArgumentSuppliers.get(i);

                            // Handle Permissions
                            if (method.isAnnotationPresent(Permission.class)) {
                                var permission = method.getAnnotation(Permission.class);
                                permissionNodes.put(permission.value(), permission);

                                builder.requires(commandSourceStack -> {
                                    if (commandSourceStack.getPlayer() == null) return allowsConsoleFinal;
                                    return PlatformCommandUtils.hasPermission(commandSourceStack, permission);
                                });
                            }


                            commandExecuteApplier(builder, method, argumentSuppliers);
                        }

                        // Get root node
                        var argumentNodesIterator = nodeArgumentHierarchy.listIterator(nodeArgumentHierarchy.size());

                        while (true) {
                            // Get current nodes
                            var currentArgumentNodes = argumentNodesIterator.previous();
                            // Check if we are at the root node
                            if (!argumentNodesIterator.hasPrevious()) {
                                // no more parents that need children
                                return currentArgumentNodes.stream();
                            }
                            // Get parent nodes
                            var parentArgumentNodes = argumentNodesIterator.previous();
                            // Add current nodes to parent nodes
                            parentArgumentNodes.forEach(parent -> currentArgumentNodes.forEach(parent::then));
                            // Set the parent nodes as the current nodes
                            argumentNodesIterator.next();
                        }
                    })
                    .toList();
            // Create build CommandNodes for each subCommand
            var subCommands = new ArrayList<LiteralArgumentBuilder<CommandSourceStack>>();
            for (Class<?> subCommandClass : subCommandClasses) {
                if (!subCommandClass.isAnnotationPresent(Command.class)) {
                    ManasCoreCommand.LOG.error("Class {} is not annotated with @Command", subCommandClass.getName());
                    continue;
                }

                Command subCommandAnnotation = subCommandClass.getAnnotation(Command.class);

                var subCommandNodes = subCommandAnnotation.value();
                if (subCommandNodes.length == 0) {
                    ManasCoreCommand.LOG.error("Class {} is annotated with @Command but no root nodes are defined", subCommandClass.getName());
                    continue;
                }

                try {
                    var instance = subCommandClass.getDeclaredConstructor().newInstance();
                    CommandNode subCommandNode = new CommandNode(subCommandClass, instance, subCommandNodes, Arrays.stream(subCommandClass.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(Execute.class)), subCommandAnnotation.subCommands());
                    subCommands.addAll(subCommandNode.build());
                    subCommandNode.permissionNodes.forEach(permissionNodes::putIfAbsent);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }

            Permission rootPermissionNode = null;
            if (this.commandClass.isAnnotationPresent(Permission.class)) {
                var permission = this.commandClass.getAnnotation(Permission.class);

                permissionNodes.put(permission.value(), permission);
                rootPermissionNode = permission;
            }
            final var rootPermissionNodeFinal = rootPermissionNode;


            // Finalize the CommandNodes
            return Arrays.stream(this.nodeLiterals).map(literal -> {
                ManasCoreCommand.LOG.debug("Creating Command {} with {} arguments and {} subCommands", literal, arguments.size(), subCommands.size());
                // Create root node
                var node = Commands.literal(literal);
                rootExecutor.get().accept(node);
                // Add Permissions
                if (rootPermissionNodeFinal != null) node.requires(commandSourceStack -> {
                    if (commandSourceStack.getPlayer() == null) return true;
                    return PlatformCommandUtils.hasPermission(commandSourceStack, rootPermissionNodeFinal);
                });
                // Add Arguments
                arguments.forEach(node::then);
                // Add SubCommands
                subCommands.forEach(node::then);

                return node;
            }).toList();
        }

        public Collection<Permission> getPermissions() {
            return permissionNodes.values();
        }

        interface ParameterSupplier<R> {
            R get(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException;
        }

        private static <T> List<T> getOrCreateIndexWithClone(List<List<T>> outerList, int outerListIndex) {
            List<T> innerList;

            // Get or create the inner list
            if (outerList.size() <= outerListIndex) {
                innerList = new ArrayList<>();
                // Fill the new list with existing values
                if (!outerList.isEmpty()) {
                    innerList.addAll(outerList.get(0));
                }

                outerList.add(outerListIndex, innerList);
            } else {
                innerList = outerList.get(outerListIndex);
            }

            return innerList;
        }

        private static <T> void addValueTo2DList(List<List<T>> outerList, int outerListIndex, int innerListIndex, T value) {
            List<T> innerList = getOrCreateIndexWithClone(outerList, outerListIndex);

            // Add the value to the inner list or replace the value
            if (innerList.size() <= innerListIndex) {
                innerList.add(innerListIndex, value);
            } else {
                innerList.set(innerListIndex, value);
            }
        }

        private static <T> void addValueTo2DList(List<List<T>> outerList, T value) {
            // Ensure to add the value at least one time
            if (outerList.isEmpty()) {
                outerList.add(new ArrayList<>());
            }
            // Add the value to all inner lists
            outerList.forEach(innerList -> innerList.add(value));
        }

        private void commandExecuteApplier(ArgumentBuilder<CommandSourceStack, ?> builder, Method targetMetod, List<ParameterSupplier<Object>> parameterSuppliers) {
            builder.executes(commandContext -> {
                var args = new Object[parameterSuppliers.size()];
                for (int j = 0; j < parameterSuppliers.size(); j++) {
                    args[j] = parameterSuppliers.get(j).get(commandContext);
                }

                int flag = 0;

                try {
                    if ((boolean) targetMetod.invoke(this.commandClassInstance, args)) {
                        flag = com.mojang.brigadier.Command.SINGLE_SUCCESS;
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }

                return flag;
            });
        }
    }
}
