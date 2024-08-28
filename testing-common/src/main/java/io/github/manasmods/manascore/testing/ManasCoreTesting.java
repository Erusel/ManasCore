/*
 * Copyright (c) 2024. ManasMods
 * GNU General Public License 3
 */

package io.github.manasmods.manascore.testing;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ManasCoreTesting {
    public static final Logger LOG = LoggerFactory.getLogger("ManasCore - Testing");

    public static void init() {
        StorageModuleTest.init();
    }
}
