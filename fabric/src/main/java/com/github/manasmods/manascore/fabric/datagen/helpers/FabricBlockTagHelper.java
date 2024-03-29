package com.github.manasmods.manascore.fabric.datagen.helpers;

import com.github.manasmods.manascore.datagen.events.DatagenEvents;
import com.github.manasmods.manascore.datagen.helpers.TagHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class FabricBlockTagHelper extends FabricTagProvider.BlockTagProvider implements TagHelper<Block> {
    public FabricBlockTagHelper(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        DatagenEvents.registerBlockTags.invoker().registerTags(this, provider);
    }

    @Override
    public TagBuilder createTag(TagKey<Block> tag) {
        return this.getOrCreateRawBuilder(tag);
    }
}
