package com.github.manasmods.manascore.storage.impl.network.s2c;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface StorageSyncPacket extends CustomPacketPayload {
    boolean isUpdate();

    CompoundTag storageTag();
}
