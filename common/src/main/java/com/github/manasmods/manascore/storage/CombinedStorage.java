package com.github.manasmods.manascore.storage;

import com.github.manasmods.manascore.ManasCore;
import com.github.manasmods.manascore.api.storage.Storage;
import com.github.manasmods.manascore.api.storage.StorageHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CombinedStorage {
    private final Map<ResourceLocation, Storage> storages = new HashMap<>();
    private final StorageHolder holder;

    public CombinedStorage(StorageHolder holder) {
        this.holder = holder;
    }

    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();

        ListTag entriesTag = new ListTag();
        this.storages.forEach((id, storage) -> {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putString("manascore_registry_storage_id", id.toString());
            storage.save(entryTag);
            entriesTag.add(entryTag);
        });

        tag.put("manascore_registry_storage", entriesTag);
        return tag;
    }

    public void load(CompoundTag tag) {
        ListTag entriesTag = tag.getList("manascore_registry_storage", ListTag.TAG_COMPOUND);

        entriesTag.forEach(t -> {
            // Get serialized storage data
            CompoundTag entryTag = (CompoundTag) t;
            // Get storage id
            ResourceLocation id = new ResourceLocation(entryTag.getString("manascore_registry_storage_id"));
            // Construct storage
            Storage storage = StorageManager.constructStorageFor(this.holder.manasCore$getStorageType(), id, holder);
            if (storage == null) {
                ManasCore.Logger.warn("Failed to construct storage for id {}. All information about this storage will be dropped!", id);
                return;
            }
            // Load storage data
            storage.load(entryTag);
            // Put storage into map
            this.storages.put(id, storage);
        });
    }

    public void handleUpdatePacket(CompoundTag tag) {
        ListTag entriesTag = tag.getList("manascore_registry_storage", ListTag.TAG_COMPOUND);

        for (Tag e : entriesTag) {
            CompoundTag entryTag = (CompoundTag) e;
            ResourceLocation id = new ResourceLocation(entryTag.getString("manascore_registry_storage_id"));
            Storage storage = this.storages.get(id);
            if (storage == null) {
                ManasCore.Logger.warn("Failed to find storage for id {}. All information about this storage will be dropped!", id);
                continue;
            }

            storage.loadUpdate(entryTag);
        }
    }

    public void add(ResourceLocation id, Storage storage) {
        this.storages.put(id, storage);
    }

    @Nullable
    public Storage get(ResourceLocation id) {
        return this.storages.get(id);
    }

    public CompoundTag createUpdatePacket(boolean clean) {
        CompoundTag tag = new CompoundTag();

        ListTag entriesTag = new ListTag();
        this.storages.forEach((id, storage) -> {
            if (!storage.isDirty()) return;
            CompoundTag entryTag = new CompoundTag();
            entryTag.putString("manascore_registry_storage_id", id.toString());
            storage.saveOutdated(entryTag);
            entriesTag.add(entryTag);
            if (clean) storage.clearDirty();
        });

        tag.put("manascore_registry_storage", entriesTag);
        return tag;
    }

    public boolean isDirty() {
        for (Storage storage : this.storages.values()) {
            if (storage.isDirty()) return true;
        }
        return false;
    }
}
