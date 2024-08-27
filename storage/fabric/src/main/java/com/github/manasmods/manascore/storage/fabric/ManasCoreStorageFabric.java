package com.github.manasmods.manascore.storage.fabric;

import com.github.manasmods.manascore.storage.ManasCoreStorage;
import net.fabricmc.api.ModInitializer;

public final class ManasCoreStorageFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ManasCoreStorage.init();
    }
}
