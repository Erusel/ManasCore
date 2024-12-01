/*
 * Copyright (c) 2024. ManasMods
 * GNU General Public License 3
 */

package io.github.manasmods.manascore.command;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import io.github.manasmods.manascore.command.api.CommandArgumentRegistrationEvent;
import io.github.manasmods.manascore.command.api.parameter.Literal;
import io.github.manasmods.manascore.command.internal.CommandArgumentRegistry;
import net.minecraft.commands.Commands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

public class ManasCoreCommand {
    public static final Logger LOG = LoggerFactory.getLogger("ManasCore - Command");

    public static void init() {
        CommandArgumentRegistrationEvent.EVENT.register((registry) -> {
            // Register Literal Argument
            registry.register(String.class, Literal.class, (annotation, nodeCreator, handler) -> {
                var literals = annotation.value();

                for (var literal : literals) {
                    nodeCreator.add(Commands.literal(literal));
                    handler.addToLastNode(context -> literal);
                }
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
