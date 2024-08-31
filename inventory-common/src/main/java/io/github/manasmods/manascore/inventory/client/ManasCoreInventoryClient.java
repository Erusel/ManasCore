package io.github.manasmods.manascore.inventory.client;

import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import io.github.manasmods.manascore.inventory.InventoryTabRegistry;
import io.github.manasmods.manascore.inventory.VanillaInventoryTab;
import io.github.manasmods.manascore.inventory.api.AbstractInventoryTab;
import io.github.manasmods.manascore.inventory.api.InventoryTabScreen;
import io.github.manasmods.manascore.inventory.client.widget.InventoryTabSwitcherWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import java.util.Map;

public class ManasCoreInventoryClient {
    public static void init() {
        ClientGuiEvent.INIT_POST.register((screen, access) -> {
            if (!isValidTabScreen(screen)) return;
            if (!(screen instanceof AbstractContainerScreen<?> containerScreen)) return;

            final Map<Integer, AbstractInventoryTab> tabRegistryEntries = InventoryTabRegistry.getEntries();
            InventoryTabSwitcherWidget tabSwitcherWidget = new InventoryTabSwitcherWidget(containerScreen, (int) Math.round(Math.ceil(tabRegistryEntries.size() / 12F)));

            tabRegistryEntries.forEach(tabSwitcherWidget::addUpdateListener);
            tabSwitcherWidget.updateTabs();
            access.addWidget(tabSwitcherWidget);
        });

        ClientLifecycleEvent.CLIENT_SETUP.register(instance -> {
            InventoryTabRegistry.register(new VanillaInventoryTab());
        });
    }

    private static boolean isValidTabScreen(Screen screen) {
        return screen instanceof InventoryTabScreen;
    }
}
