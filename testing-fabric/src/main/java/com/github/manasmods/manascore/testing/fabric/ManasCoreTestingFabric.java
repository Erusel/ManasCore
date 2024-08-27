package com.github.manasmods.manascore.testing.fabric;

import com.github.manasmods.manascore.testing.ManasCoreTesting;
import net.fabricmc.api.ModInitializer;

public class ManasCoreTestingFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ManasCoreTesting.init();
    }
}