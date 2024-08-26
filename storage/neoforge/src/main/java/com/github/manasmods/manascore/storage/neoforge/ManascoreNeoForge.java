package com.github.manasmods.manascore.storage.neoforge;

import com.github.manasmods.manascore.storage.Manascore;
import net.neoforged.fml.common.Mod;

@Mod(Manascore.MOD_ID)
public final class ManascoreNeoForge {
    public ManascoreNeoForge() {
        // Run our common setup.
        Manascore.init();
    }
}
