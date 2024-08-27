package com.github.manasmods.manascore.storage;

import com.github.manasmods.manascore.storage.impl.network.ManasStorageNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ManasCoreStorage {
    public static final Logger LOG = LoggerFactory.getLogger("ManasCore - Storage");

    public static void init() {
        ManasStorageNetwork.init();
    }
}
