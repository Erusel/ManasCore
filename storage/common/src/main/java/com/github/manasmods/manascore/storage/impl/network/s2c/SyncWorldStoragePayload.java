package com.github.manasmods.manascore.storage.impl.network.s2c;


import com.github.manasmods.manascore.storage.ModuleConstants;
import dev.architectury.networking.NetworkManager;
import dev.architectury.utils.Env;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SyncWorldStoragePayload(
        boolean isUpdate,
        CompoundTag storageTag
) implements StorageSyncPayload {
    public static final CustomPacketPayload.Type<SyncWorldStoragePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ModuleConstants.MOD_ID, "sync_world_storage"));
    public static final StreamCodec<FriendlyByteBuf, SyncWorldStoragePayload> STREAM_CODEC = CustomPacketPayload.codec(SyncWorldStoragePayload::encode, SyncWorldStoragePayload::new);

    public SyncWorldStoragePayload(FriendlyByteBuf buf) {
        this(buf.readBoolean(), buf.readNbt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(isUpdate);
        buf.writeNbt(storageTag);
    }

    public void handle(NetworkManager.PacketContext context) {
        if (context.getEnvironment() != Env.CLIENT) return;
        context.queue(() -> ClientAccess.handle(this));
    }

    @Override
    public Type<SyncWorldStoragePayload> type() {
        return TYPE;
    }
}
