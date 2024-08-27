package com.github.manasmods.manascore.network.fabric;

import com.github.manasmods.manascore.storage.ManasCoreStorage;
import net.fabricmc.api.ModInitializer;

public class ManasCoreNetworkFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ManasCoreStorage.init();
    }
}