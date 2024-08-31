package io.github.manasmods.manascore.inventory.neoforge;

import io.github.manasmods.manascore.inventory.ManasCoreInventory;
import io.github.manasmods.manascore.inventory.ModuleConstants;
import net.neoforged.fml.common.Mod;

@Mod(ModuleConstants.MOD_ID)
public final class ManasCoreInventoryNeoForge {
    public ManasCoreInventoryNeoForge() {
        ManasCoreInventory.init();
    }
}
