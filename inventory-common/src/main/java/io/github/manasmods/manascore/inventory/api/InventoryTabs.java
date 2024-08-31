package io.github.manasmods.manascore.inventory.api;

import io.github.manasmods.manascore.inventory.InventoryTabRegistry;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public final class InventoryTabs {
    /**
     * Registers a {@link AbstractInventoryTab} instance to the Inventory Tab Registry.
     */
    public static void registerTab(AbstractInventoryTab tab) {
        InventoryTabRegistry.register(tab);
    }

    public static Collection<AbstractInventoryTab> getRegisteredTabs() {
        return InventoryTabRegistry.getValues();
    }

    public static Map<Integer, AbstractInventoryTab> getRegistryEntries() {
        return InventoryTabRegistry.getEntries();
    }

    /**
     * Returns the registered {@link AbstractInventoryTab} Object or null.
     * The given {@link Screen} has to extend {@link InventoryTabScreen} to be able to find the {@link AbstractInventoryTab} in the registry.
     */
    @Nullable
    public static AbstractInventoryTab findByScreen(final Screen screen) {
        return InventoryTabRegistry.findByScreen(screen);
    }
}
