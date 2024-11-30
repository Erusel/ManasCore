/*
 * Copyright (c) 2024. ManasMods
 * GNU General Public License 3
 */

package io.github.manasmods.manascore.command.neoforge;

import io.github.manasmods.manascore.command.api.Permission;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.neoforge.server.permission.PermissionAPI;
import net.neoforged.neoforge.server.permission.nodes.PermissionNode;
import net.neoforged.neoforge.server.permission.nodes.PermissionTypes;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PlatformCommandUtilsImpl {
    public static final Map<String, PermissionNode<Boolean>> PERMISSIONS = new HashMap<>();

    public static boolean hasPermission(CommandSourceStack commandSourceStack, Permission permission) {
        if (!commandSourceStack.isPlayer()) return true;
        return PermissionAPI.getPermission(Objects.requireNonNull(commandSourceStack.getPlayer()), PERMISSIONS.get(permission.value()));
    }

    public static void registerPermission(Permission permission) {
        var modId = permission.value().substring(0, permission.value().indexOf('.'));
        var nodeName = permission.value().substring(permission.value().indexOf('.') + 1);
        PERMISSIONS.put(permission.value(), new PermissionNode<>(modId, nodeName, PermissionTypes.BOOLEAN, (player, playerUUID, context) -> {
            if (player == null) return true;
            return player.hasPermissions(permission.permissionLevel().getLevel());
        }));
    }
}
