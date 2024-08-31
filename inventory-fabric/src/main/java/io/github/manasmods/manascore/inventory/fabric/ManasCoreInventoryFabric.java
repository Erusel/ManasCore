package io.github.manasmods.manascore.inventory.fabric;

import io.github.manasmods.manascore.inventory.ManasCoreInventory;
import net.fabricmc.api.ModInitializer;

public class ManasCoreInventoryFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ManasCoreInventory.init();
    }
}