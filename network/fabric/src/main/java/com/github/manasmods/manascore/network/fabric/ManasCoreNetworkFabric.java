package com.github.manasmods.manascore.network.fabric;

import com.github.manasmods.manascore.network.ManasCoreNetwork;
import net.fabricmc.api.ModInitializer;

public class ManasCoreNetworkFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ManasCoreNetwork.init();
    }
}