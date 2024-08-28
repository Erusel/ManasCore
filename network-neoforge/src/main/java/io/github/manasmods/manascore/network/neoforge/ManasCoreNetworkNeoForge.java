package io.github.manasmods.manascore.network.neoforge;

import io.github.manasmods.manascore.network.ManasCoreNetwork;
import io.github.manasmods.manascore.network.ModuleConstants;
import net.neoforged.fml.common.Mod;

@Mod(ModuleConstants.MOD_ID)
public final class ManasCoreNetworkNeoForge {
    public ManasCoreNetworkNeoForge() {
        ManasCoreNetwork.init();
    }
}
