package io.github.manasmods.manascore.storage.impl;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;

public class StoragePersistentState extends SavedData {
    public static final ThreadLocal<Boolean> LOADING = ThreadLocal.withInitial(() -> false);

    public static Factory<StoragePersistentState> getFactory(CombinedStorage storage) {
        return new Factory<>(
                () -> new StoragePersistentState(storage),
                (tag, provider) -> fromNBT(storage, tag),
                DataFixTypes.LEVEL
        );
    }

    private final CombinedStorage storage;

    public StoragePersistentState(CombinedStorage storage) {
        this.storage = storage;
    }

    @Override
    public boolean isDirty() {
        return true;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        return this.storage.toNBT();
    }

    public static StoragePersistentState fromNBT(CombinedStorage storage, CompoundTag tag) {
        StoragePersistentState state = new StoragePersistentState(storage);
        state.storage.handleUpdatePacket(tag);
        return state;
    }
}
