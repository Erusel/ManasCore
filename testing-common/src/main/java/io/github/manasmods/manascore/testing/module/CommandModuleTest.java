/*
 * Copyright (c) 2024. ManasMods
 * GNU General Public License 3
 */

package io.github.manasmods.manascore.testing.module;

import io.github.manasmods.manascore.command.api.*;
import io.github.manasmods.manascore.command.api.parameter.Enum;
import io.github.manasmods.manascore.command.api.parameter.Literal;
import io.github.manasmods.manascore.command.api.parameter.Sender;
import io.github.manasmods.manascore.command.api.parameter.Uuid;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import java.util.UUID;

public class CommandModuleTest {
    private static final Component RESPONSE = Component.literal("Works!");

    public static void init() {
        CommandArgumentRegistrationEvent.EVENT.register(registry -> {
            registry.registerEnum(TestEnum.class);
        });

        CommandRegistry.registerCommand(TestCommand.class);
    }

    @Command(value = "foo", subCommands = {TestSubCommand.class})
    public static class TestCommand {
        // TODO Check Permission
        @Permission("manascore.command.test")
        @Execute
        public boolean withPerms(@Sender CommandSourceStack sender) {
            sender.sendSystemMessage(RESPONSE);
            return true;
        }
    }

    @Command(value = "bar")
    public static class TestSubCommand {
        @Execute
        public boolean args(@Sender CommandSourceStack sender, @Literal("literal") String l, @Uuid("uuid") UUID uuid, @Enum(TestEnum.class) TestEnum _enum) {
            sender.sendSystemMessage(RESPONSE.copy()
                    .append("\nLiteral: '")
                    .append(Component.literal(l))
                    .append("'\nUUID: '")
                    .append(Component.literal(uuid.toString()))
                    .append("'\nEnum: '")
                    .append(_enum.name())
                    .append("'")
            );
            return true;
        }
    }

    public enum TestEnum {
        TEST,
        TEST2
    }
}
