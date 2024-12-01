/*
 * Copyright (c) 2024. ManasMods
 * GNU General Public License 3
 */

package io.github.manasmods.manascore.command.fabric;

import io.github.manasmods.manascore.command.ManasCoreCommand;
import io.github.manasmods.manascore.command.api.Permission;
import net.fabricmc.api.ModInitializer;
import net.minecraft.commands.CommandSourceStack;

import java.util.function.BiFunction;

public class ManasCoreCommandFabric implements ModInitializer {
    public static BiFunction<CommandSourceStack, Permission, Boolean> hasPermission = (commandSourceStack, permission) -> {
        if (!commandSourceStack.isPlayer()) return true;
        return commandSourceStack.hasPermission(permission.permissionLevel().getLevel());
    };


    @Override
    public void onInitialize() {
        ManasCoreCommand.init();
    }
}