package io.github.manasmods.manascore.storage.fabric;

import io.github.manasmods.manascore.storage.ManasCoreStorage;
import net.fabricmc.api.ModInitializer;

public class ManasCoreStorageFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ManasCoreStorage.init();
    }
}