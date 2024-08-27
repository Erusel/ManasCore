package com.github.manasmods.manascore.storage;

import com.github.manasmods.manascore.storage.impl.network.ManasStorageNetwork;

public final class ManasCoreStorage {
    public static void init() {
        ManasStorageNetwork.init();
    }
}
