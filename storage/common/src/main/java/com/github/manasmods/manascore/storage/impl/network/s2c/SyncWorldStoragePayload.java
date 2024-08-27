package com.github.manasmods.manascore.storage.impl.network.s2c;


import com.github.manasmods.manascore.storage.ModuleConstants;
import dev.architectury.networking.NetworkManager;
import dev.architectury.utils.Env;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public record SyncWorldStoragePayload(
        boolean isUpdate,
        CompoundTag storageTag
) implements StorageSyncPacket {
    public static final CustomPacketPayload.Type<SyncWorldStoragePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ModuleConstants.MOD_ID, "sync_world_storage"));
    public static final StreamCodec<FriendlyByteBuf, SyncWorldStoragePayload> STREAM_CODEC = CustomPacketPayload.codec(SyncWorldStoragePayload::encode, SyncWorldStoragePayload::new);

    public SyncWorldStoragePayload(FriendlyByteBuf buf) {
        this(buf.readBoolean(), buf.readNbt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(isUpdate);
        buf.writeNbt(storageTag);
    }

    public void handle(Supplier<NetworkManager.PacketContext> contextSupplier) {
        NetworkManager.PacketContext context = contextSupplier.get();
        if (context.getEnvironment() != Env.CLIENT) return;
        context.queue(() -> ClientAccess.handle(this));
    }

    @Override
    public Type<SyncWorldStoragePayload> type() {
        return TYPE;
    }
}
