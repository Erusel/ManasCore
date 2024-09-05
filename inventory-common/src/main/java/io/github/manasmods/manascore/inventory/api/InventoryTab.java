package io.github.manasmods.manascore.inventory.api;

import io.github.manasmods.manascore.inventory.InventoryTabRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;

import java.util.function.Predicate;

public interface InventoryTab {
    void sendOpenContainerPacket();

    @Environment(EnvType.CLIENT)
    default Predicate<Screen> isCurrentScreen() {
        return screen -> {
            AbstractInventoryTab tab = InventoryTabRegistry.findByScreen(screen);
            return tab != null && tab.equals(this);
        };
    }
}
