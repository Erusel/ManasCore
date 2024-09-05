package io.github.manasmods.manascore.inventory;

import io.github.manasmods.manascore.inventory.api.AbstractInventoryTab;
import io.github.manasmods.manascore.inventory.api.InventoryTabScreen;
import lombok.Getter;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryTabRegistry {
    @Getter
    private static int nextEntryId = 1;
    private static final Map<Integer, AbstractInventoryTab> registeredTabs = new ConcurrentHashMap<>();

    public static void register(AbstractInventoryTab tab) {
        registeredTabs.put(nextEntryId++, tab);
    }

    public static Collection<AbstractInventoryTab> getValues() {
        return registeredTabs.values();
    }

    public static Map<Integer, AbstractInventoryTab> getEntries() {
        return Map.copyOf(registeredTabs);
    }

    @Nullable
    public static AbstractInventoryTab findByScreen(Screen screen) {
        if (!(screen instanceof InventoryTabScreen inventoryTabScreen)) return null;
        return getValues().stream()
                .filter(abstractInventoryTab -> abstractInventoryTab.getClass().equals(inventoryTabScreen.getTabClass()))
                .findFirst()
                .orElse(null);
    }
}
