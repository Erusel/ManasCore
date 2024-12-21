/*
 * Copyright (c) 2024. ManasMods
 * GNU General Public License 3
 */

package io.github.manasmods.manascore.command;

import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import io.github.manasmods.manascore.command.api.CommandArgumentRegistrationEvent;
import io.github.manasmods.manascore.command.api.parameter.*;
import io.github.manasmods.manascore.command.internal.CommandArgumentRegistry;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class ManasCoreCommand {
    public static final Logger LOG = LoggerFactory.getLogger("ManasCore - Command");

    public static void init() {
        CommandArgumentRegistrationEvent.EVENT.register((registry) -> {
            // Register Literal Argument
            registry.register(String.class, Literal.class, (annotation, handler) -> {
                var literals = annotation.value();

                for (var literal : literals) {
                    handler.addNode(Commands.literal(literal));
                    handler.addValueExtractorToLastNode(context -> literal);
                }
            });

            // Register Sender Argument
            registry.register(CommandSourceStack.class, Sender.class, (annotation, handler) -> handler.addValueExtractor(CommandContext::getSource));
            registry.register(ServerPlayer.class, Sender.class, (annotation, handler) -> {
                handler.addValueExtractor(context -> context.getSource().getPlayerOrException());
                handler.preventConsoleUsage();
            });
            registry.register(CommandSource.class, Sender.class, (annotation, handler) -> handler.addValueExtractor(commandContext -> commandContext.getSource().source));

            // Register UUID Argument
            registry.register(UUID.class, Uuid.class, (annotation, handler) -> {
                var argumentName = annotation.value().isBlank() ? handler.getAutoGeneratedArgumentName() : annotation.value();
                handler.addNode(Commands.argument(argumentName, StringArgumentType.word()));
                handler.addValueExtractorToLastNode(commandContext -> {
                    var uuidString = StringArgumentType.getString(commandContext, argumentName);
                    try {
                        return UUID.fromString(uuidString);
                    } catch (Exception e) {
                        throw new CommandSyntaxException(new SimpleCommandExceptionType(e::getMessage), e::getMessage);
                    }
                });
            });

            // Register Boolean Argument
            registry.register(Boolean.class, Bool.class, (annotation, handler) -> {
                var argumentName = annotation.value().isBlank() ? handler.getAutoGeneratedArgumentName() : annotation.value();
                handler.addNode(Commands.argument(argumentName, BoolArgumentType.bool()));
                handler.addValueExtractor(commandContext -> BoolArgumentType.getBool(commandContext, argumentName));
            });
            registry.register(boolean.class, Bool.class, (annotation, handler) -> {
                var argumentName = annotation.value().isBlank() ? handler.getAutoGeneratedArgumentName() : annotation.value();
                handler.addNode(Commands.argument(argumentName, BoolArgumentType.bool()));
                handler.addValueExtractor(commandContext -> BoolArgumentType.getBool(commandContext, argumentName));
            });

            // Register Text Argument
            registry.register(String.class, Text.class, (annotation, handler) -> {
                var argumentName = annotation.name().isBlank() ? handler.getAutoGeneratedArgumentName() : annotation.name();

                switch (annotation.value()) {
                    case WORD -> handler.addNode(
                            Commands.argument(argumentName, StringArgumentType.word())
                    );
                    case STRING -> handler.addNode(
                            Commands.argument(argumentName, StringArgumentType.string())
                    );
                    case GREEDY_STRING -> handler.addNode(
                            Commands.argument(argumentName, StringArgumentType.greedyString())
                    );
                }

                handler.addValueExtractor(commandContext -> StringArgumentType.getString(commandContext, argumentName));
            });

            // Register Double Argument
            registry.register(Double.class, DoubleNumber.class, (annotation, handler) -> {
                var argumentName = annotation.value().isBlank() ? handler.getAutoGeneratedArgumentName() : annotation.value();
                handler.addNode(Commands.argument(argumentName, DoubleArgumentType.doubleArg(annotation.min(), annotation.max())));
                handler.addValueExtractor(commandContext -> DoubleArgumentType.getDouble(commandContext, argumentName));
            });
            registry.register(double.class, DoubleNumber.class, (annotation, handler) -> {
                var argumentName = annotation.value().isBlank() ? handler.getAutoGeneratedArgumentName() : annotation.value();
                handler.addNode(Commands.argument(argumentName, DoubleArgumentType.doubleArg(annotation.min(), annotation.max())));
                handler.addValueExtractor(commandContext -> DoubleArgumentType.getDouble(commandContext, argumentName));
            });

            // Register Float Argument
            registry.register(Float.class, FloatNumber.class, (annotation, handler) -> {
                var argumentName = annotation.value().isBlank() ? handler.getAutoGeneratedArgumentName() : annotation.value();
                handler.addNode(Commands.argument(argumentName, FloatArgumentType.floatArg(annotation.min(), annotation.max())));
                handler.addValueExtractor(commandContext -> FloatArgumentType.getFloat(commandContext, argumentName));
            });
            registry.register(float.class, FloatNumber.class, (annotation, handler) -> {
                var argumentName = annotation.value().isBlank() ? handler.getAutoGeneratedArgumentName() : annotation.value();
                handler.addNode(Commands.argument(argumentName, FloatArgumentType.floatArg(annotation.min(), annotation.max())));
                handler.addValueExtractor(commandContext -> FloatArgumentType.getFloat(commandContext, argumentName));
            });

            // Register Integer Argument
            registry.register(Integer.class, IntNumber.class, (annotation, handler) -> {
                var argumentName = annotation.value().isBlank() ? handler.getAutoGeneratedArgumentName() : annotation.value();
                handler.addNode(Commands.argument(argumentName, IntegerArgumentType.integer(annotation.min(), annotation.max())));
                handler.addValueExtractor(commandContext -> IntegerArgumentType.getInteger(commandContext, argumentName));
            });
            registry.register(int.class, IntNumber.class, (annotation, handler) -> {
                var argumentName = annotation.value().isBlank() ? handler.getAutoGeneratedArgumentName() : annotation.value();
                handler.addNode(Commands.argument(argumentName, IntegerArgumentType.integer(annotation.min(), annotation.max())));
                handler.addValueExtractor(commandContext -> IntegerArgumentType.getInteger(commandContext, argumentName));
            });

            // Register Long Argument
            registry.register(Long.class, LongNumber.class, (annotation, handler) -> {
                var argumentName = annotation.value().isBlank() ? handler.getAutoGeneratedArgumentName() : annotation.value();
                handler.addNode(Commands.argument(argumentName, LongArgumentType.longArg(annotation.min(), annotation.max())));
                handler.addValueExtractor(commandContext -> LongArgumentType.getLong(commandContext, argumentName));
            });
            registry.register(long.class, LongNumber.class, (annotation, handler) -> {
                var argumentName = annotation.value().isBlank() ? handler.getAutoGeneratedArgumentName() : annotation.value();
                handler.addNode(Commands.argument(argumentName, LongArgumentType.longArg(annotation.min(), annotation.max())));
                handler.addValueExtractor(commandContext -> LongArgumentType.getLong(commandContext, argumentName));
            });
        });

        CommandRegistrationEvent.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> {
            var registry = new CommandArgumentRegistry();
            // Register Argument Annotations
            CommandArgumentRegistrationEvent.EVENT.invoker().register(registry);
            // Register Commands
            CommandAnnotationHandler.COMMANDS.forEach(commandNode -> {
                try {
                    var command = commandNode.build(registry);
                    command.forEach(commandDispatcher::register);

                    // Register Permissions
                    commandNode.getPermissions().forEach(PlatformCommandUtils::registerPermission);
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                         IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }
}
