package com.github.manasmods.manascore.testing;

import com.github.manasmods.manascore.storage.api.Storage;
import com.github.manasmods.manascore.storage.api.StorageEvents;
import com.github.manasmods.manascore.storage.api.StorageHolder;
import com.github.manasmods.manascore.storage.api.StorageKey;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.ChatEvent;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

import static com.github.manasmods.manascore.testing.ManasCoreTesting.LOG;

public class StorageModuleTest {
    private static StorageKey<TestStorage> ENTITY_KEY = null;
    private static StorageKey<TestStorage> CHUNK_KEY = null;
    private static StorageKey<TestStorage> WORLD_KEY = null;

    public static void init() {
        // Register storage
        StorageEvents.REGISTER_ENTITY_STORAGE.register(registry -> {
            ENTITY_KEY = registry.register(ResourceLocation.fromNamespaceAndPath(ModuleConstants.MOD_ID, "test_storage"), TestStorage.class, entity -> entity instanceof Player, TestStorage::new);
            LOG.info("Registered entity storage key: {}", ENTITY_KEY.id());
        });
        StorageEvents.REGISTER_CHUNK_STORAGE.register(registry -> {
            CHUNK_KEY = registry.register(ResourceLocation.fromNamespaceAndPath(ModuleConstants.MOD_ID, "test_storage"), TestStorage.class, chunk -> true, TestStorage::new);
            LOG.info("Registered chunk storage key: {}", CHUNK_KEY.id());
        });
        StorageEvents.REGISTER_WORLD_STORAGE.register(registry -> {
            WORLD_KEY = registry.register(ResourceLocation.fromNamespaceAndPath(ModuleConstants.MOD_ID, "test_storage"), TestStorage.class, level -> true, TestStorage::new);
            LOG.info("Registered world storage key: {}", WORLD_KEY.id());
        });
        // Register event listeners that change the storage
        PlayerEvent.DROP_ITEM.register((player, entity) -> {
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.manasCore$getStorageOptional(ENTITY_KEY).ifPresent(TestStorage::increaseDropCount);
                serverPlayer.level().getChunkAt(entity.blockPosition()).manasCore$getStorageOptional(CHUNK_KEY).ifPresent(TestStorage::increaseDropCount);
                serverPlayer.level().manasCore$getStorageOptional(WORLD_KEY).ifPresent(TestStorage::increaseDropCount);
            }

            return EventResult.pass();
        });
        EntityEvent.LIVING_DEATH.register((entity, source) -> {
            if (entity instanceof ServerPlayer serverPlayer) {
                serverPlayer.manasCore$getStorageOptional(ENTITY_KEY).ifPresent(TestStorage::increaseDeathCount);
                serverPlayer.level().getChunkAt(entity.blockPosition()).manasCore$getStorageOptional(CHUNK_KEY).ifPresent(TestStorage::increaseDeathCount);
                serverPlayer.level().manasCore$getStorageOptional(WORLD_KEY).ifPresent(TestStorage::increaseDeathCount);
            }

            return EventResult.pass();
        });
        // Register event listeners that print the storage on client side and server side
        ChatEvent.RECEIVED.register((player, component) -> {
            if (player != null) printTestStorage(player);
            return EventResult.pass();
        });
    }

    // Utility method to print the storage
    public static void printTestStorage(Player player) {
        Level level = player.level();
        LevelChunk chunk = level.getChunkAt(player.blockPosition());
        boolean isClientSide = level.isClientSide();

        LOG.info("Storage of entity {} on {}:\n{}", player.getId(), isClientSide ? "client" : "server", player.manasCore$getStorage(ENTITY_KEY));
        LOG.info("Storage at chunk {} on {}:\n{}", chunk.getPos(), isClientSide ? "client" : "server", chunk.manasCore$getStorage(CHUNK_KEY));
        LOG.info("Storage of world {} on {}:\n{}", level.dimension().location(), isClientSide ? "client" : "server", level.manasCore$getStorage(WORLD_KEY));
    }

    // Storage implementation
    public static class TestStorage extends Storage {
        private int dropCount = 0;
        private int deathCount = 0;

        protected TestStorage(StorageHolder holder) {
            super(holder);
        }

        public void increaseDropCount() {
            dropCount++;
            markDirty();
        }

        public void increaseDeathCount() {
            deathCount++;
            markDirty();
        }

        @Override
        public void save(CompoundTag data) {
            data.putInt("dropCount", dropCount);
            data.putInt("deathCount", deathCount);
        }

        @Override
        public void load(CompoundTag data) {
            this.dropCount = data.getInt("dropCount");
            this.deathCount = data.getInt("deathCount");
        }

        @Override
        public String toString() {
            return String.format("TestStorage{\n  %s\n  %s\n}", "dropCount=" + dropCount, "deathCount=" + deathCount);
        }
    }
}
