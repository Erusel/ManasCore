package com.github.manasmods.manascore.fabric;

import com.github.manasmods.manascore.Manascore;
import net.fabricmc.api.ModInitializer;

public final class ManascoreFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        Manascore.init();
    }
}
