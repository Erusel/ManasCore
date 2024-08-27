package com.github.manasmods.manascore.storage.neoforge;

import com.github.manasmods.manascore.storage.ManasCoreStorage;
import com.github.manasmods.manascore.storage.ModuleConstants;
import net.neoforged.fml.common.Mod;

@Mod(ModuleConstants.MOD_ID)
public final class ManasCoreStorageNeoForge {
    public ManasCoreStorageNeoForge() {
        ManasCoreStorage.init();
    }
}
