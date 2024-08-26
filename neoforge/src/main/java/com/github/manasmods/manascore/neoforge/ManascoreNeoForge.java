package com.github.manasmods.manascore.neoforge;

import com.github.manasmods.manascore.Manascore;
import net.neoforged.fml.common.Mod;

@Mod(Manascore.MOD_ID)
public final class ManascoreNeoForge {
    public ManascoreNeoForge() {
        // Run our common setup.
        Manascore.init();
    }
}
