package io.github.manasmods.manascore.inventory.mixin;

import io.github.manasmods.manascore.inventory.VanillaInventoryTab;
import io.github.manasmods.manascore.inventory.api.AbstractInventoryTab;
import io.github.manasmods.manascore.inventory.api.InventoryTabScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(InventoryScreen.class)
public class MixinInventoryScreen implements InventoryTabScreen {
    private static final Class<VanillaInventoryTab> TAB_CLASS = VanillaInventoryTab.class;

    @Override
    public Class<? extends AbstractInventoryTab> getTabClass() {
        return TAB_CLASS;
    }
}
