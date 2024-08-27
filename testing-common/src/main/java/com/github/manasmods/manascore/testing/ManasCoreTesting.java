package com.github.manasmods.manascore.testing;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ManasCoreTesting {
    public static final Logger LOG = LoggerFactory.getLogger("ManasCore - Testing");

    public static void init() {
        StorageModuleTest.init();
    }
}
