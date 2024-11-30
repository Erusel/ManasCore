/*
 * Copyright (c) 2024. ManasMods
 * GNU General Public License 3
 */

package io.github.manasmods.manascore.command.fabric.integrations;

import io.github.manasmods.manascore.command.api.Permission;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.platform.PlayerAdapter;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;
import java.util.function.BiFunction;

public class LuckPermsIntegration {
    private static final LuckPerms luckPerms = LuckPermsProvider.get();
    private static final PlayerAdapter<Player> playerAdapter = luckPerms.getPlayerAdapter(Player.class);
    public static final BiFunction<CommandSourceStack, Permission, Boolean> hasPermission = (commandSourceStack, permission) -> {
        if (!commandSourceStack.isPlayer()) return true;
        return playerAdapter.getUser(Objects.requireNonNull(commandSourceStack.getPlayer())).getCachedData().getPermissionData().checkPermission(permission.value()).asBoolean();
    };
}
