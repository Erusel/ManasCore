package io.github.manasmods.manascore.inventory;

import dev.architectury.platform.Platform;
import io.github.manasmods.manascore.inventory.client.ManasCoreInventoryClient;
import net.fabricmc.api.EnvType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManasCoreInventory {
    public static final Logger LOG = LoggerFactory.getLogger("ManasCore - Inventory");

    public static void init() {
        if (Platform.getEnv() == EnvType.CLIENT) {
            ManasCoreInventoryClient.init();
        }
    }
}
