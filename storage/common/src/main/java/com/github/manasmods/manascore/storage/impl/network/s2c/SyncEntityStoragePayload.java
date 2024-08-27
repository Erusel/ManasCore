package com.github.manasmods.manascore.storage.impl.network.s2c;


import com.github.manasmods.manascore.storage.ModuleConstants;
import dev.architectury.networking.NetworkManager;
import dev.architectury.utils.Env;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SyncEntityStoragePayload(
        boolean isUpdate,
        int entityId,
        CompoundTag storageTag
) implements StorageSyncPayload {
    public static final CustomPacketPayload.Type<SyncEntityStoragePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ModuleConstants.MOD_ID, "sync_entity_storage"));
    public static final StreamCodec<FriendlyByteBuf, SyncEntityStoragePayload> STREAM_CODEC = CustomPacketPayload.codec(SyncEntityStoragePayload::encode, SyncEntityStoragePayload::new);

    public SyncEntityStoragePayload(FriendlyByteBuf buf) {
        this(buf.readBoolean(), buf.readInt(), buf.readNbt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(isUpdate);
        buf.writeInt(entityId);
        buf.writeNbt(storageTag);
    }

    public void handle(NetworkManager.PacketContext context) {
        if (context.getEnvironment() != Env.CLIENT) return;
        context.queue(() -> ClientAccess.handle(this));
    }

    @Override
    public Type<SyncEntityStoragePayload> type() {
        return TYPE;
    }
}
