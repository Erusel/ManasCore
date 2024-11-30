/*
 * Copyright (c) 2024. ManasMods
 * GNU General Public License 3
 */

package io.github.manasmods.manascore.command;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManasCoreCommand {
    public static final Logger LOG = LoggerFactory.getLogger("ManasCore - Command");

    public static void init() {
        CommandRegistrationEvent.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> {
            CommandAnnotationHandler.COMMANDS.forEach(commandDispatcher::register);
        });
    }
}
