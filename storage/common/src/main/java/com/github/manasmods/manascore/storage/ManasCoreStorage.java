package com.github.manasmods.manascore.storage;

import com.github.manasmods.manascore.storage.impl.network.ManasStorageNetwork;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ManasCoreStorage {
    public static final Logger LOG = LogManager.getFormatterLogger("ManasCore - Storage");

    public static void init() {
        ManasStorageNetwork.init();
    }
}
