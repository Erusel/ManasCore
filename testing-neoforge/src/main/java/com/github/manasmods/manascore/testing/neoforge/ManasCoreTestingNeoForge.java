package com.github.manasmods.manascore.testing.neoforge;

import com.github.manasmods.manascore.testing.ManasCoreTesting;
import com.github.manasmods.manascore.testing.ModuleConstants;
import net.neoforged.fml.common.Mod;

@Mod(ModuleConstants.MOD_ID)
public final class ManasCoreTestingNeoForge {
    public ManasCoreTestingNeoForge() {
        ManasCoreTesting.init();
    }
}
