package com.github.manasmods.testmod;

import com.github.manasmods.testmod.client.KeybindingTest;
import com.github.manasmods.testmod.registry.RegisterTest;
import com.github.manasmods.testmod.storage.StorageTest;

public class TestMod {
    public static final String MOD_ID = "testmod";

    public static void init() {
        StorageTest.init();
        KeybindingTest.init();
        RegisterTest.init();
    }
}
