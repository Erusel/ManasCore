package com.github.manasmods.manascore.storage.impl.network;

import com.github.manasmods.manascore.network.api.util.NetworkUtils;
import com.github.manasmods.manascore.storage.impl.network.s2c.SyncChunkStoragePayload;
import com.github.manasmods.manascore.storage.impl.network.s2c.SyncEntityStoragePayload;
import com.github.manasmods.manascore.storage.impl.network.s2c.SyncWorldStoragePayload;

public class ManasStorageNetwork {
    public static void init() {
        NetworkUtils.registerS2CPayload(SyncChunkStoragePayload.TYPE, SyncChunkStoragePayload.STREAM_CODEC, SyncChunkStoragePayload::handle);
        NetworkUtils.registerS2CPayload(SyncEntityStoragePayload.TYPE, SyncEntityStoragePayload.STREAM_CODEC, SyncEntityStoragePayload::handle);
        NetworkUtils.registerS2CPayload(SyncWorldStoragePayload.TYPE, SyncWorldStoragePayload.STREAM_CODEC, SyncWorldStoragePayload::handle);
    }
}
