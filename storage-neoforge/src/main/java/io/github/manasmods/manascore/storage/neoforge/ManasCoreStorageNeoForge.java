/*
 * Copyright (c) 2024. ManasMods
 * GNU General Public License 3
 */

package io.github.manasmods.manascore.storage.neoforge;

import io.github.manasmods.manascore.storage.ManasCoreStorage;
import io.github.manasmods.manascore.storage.ModuleConstants;
import io.github.manasmods.manascore.storage.impl.StorageManager;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@Mod(ModuleConstants.MOD_ID)
public final class ManasCoreStorageNeoForge {
    public ManasCoreStorageNeoForge() {
        ManasCoreStorage.init();
        NeoForge.EVENT_BUS.addListener(ManasCoreStorageNeoForge::onPlayerStartTracking);
    }

    private static void onPlayerStartTracking(PlayerEvent.StartTracking event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            StorageManager.syncTarget(event.getTarget(), player);
        }
    }
}
