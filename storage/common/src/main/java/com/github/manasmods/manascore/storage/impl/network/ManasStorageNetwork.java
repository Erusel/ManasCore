package com.github.manasmods.manascore.storage.impl.network;

import com.github.manasmods.manascore.storage.impl.network.s2c.SyncChunkStoragePayload;
import com.github.manasmods.manascore.storage.impl.network.s2c.SyncEntityStoragePayload;
import com.github.manasmods.manascore.storage.impl.network.s2c.SyncWorldStoragePayload;
import dev.architectury.networking.NetworkManager;

public class ManasStorageNetwork {
    public static void init() {
        NetworkManager.registerS2CPayloadType(SyncChunkStoragePayload.TYPE, SyncChunkStoragePayload.STREAM_CODEC);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, SyncChunkStoragePayload.TYPE, SyncChunkStoragePayload.STREAM_CODEC, SyncChunkStoragePayload::handle);

        NetworkManager.registerS2CPayloadType(SyncEntityStoragePayload.TYPE, SyncEntityStoragePayload.STREAM_CODEC);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, SyncEntityStoragePayload.TYPE, SyncEntityStoragePayload.STREAM_CODEC, SyncEntityStoragePayload::handle);

        NetworkManager.registerS2CPayloadType(SyncWorldStoragePayload.TYPE, SyncWorldStoragePayload.STREAM_CODEC);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, SyncWorldStoragePayload.TYPE, SyncWorldStoragePayload.STREAM_CODEC, SyncWorldStoragePayload::handle);
    }
}
