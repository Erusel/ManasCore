package com.github.manasmods.manascore.utils;

import lombok.NonNull;
import net.minecraft.server.level.ChunkMap.TrackedEntity;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.ChunkSource;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class PlayerLookup {
    public static Collection<ServerPlayer> tracking(@NonNull Entity entity) {
        ChunkSource manager = entity.level().getChunkSource();
        if (!(manager instanceof ServerChunkCache cache)) throw new IllegalArgumentException("Only supported on server worlds!");
        TrackedEntity trackedEntity = cache.chunkMap.entityMap.get(entity.getId());
        if (trackedEntity == null) return Collections.emptySet();
        return trackedEntity.seenBy.stream().map(ServerPlayerConnection::getPlayer).collect(Collectors.toUnmodifiableSet());
    }
}
