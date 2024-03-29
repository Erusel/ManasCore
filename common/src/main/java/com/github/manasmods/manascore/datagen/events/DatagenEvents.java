package com.github.manasmods.manascore.datagen.events;

import com.github.manasmods.manascore.datagen.helpers.TagHelper;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Consumer;

public interface DatagenEvents {

    Event<RegisterAdvancements> registerAdvancementsEvent = EventFactory.createLoop();
    Event<RegisterBlockLoot> registerBlockLoot = EventFactory.createLoop();
    Event<RegisterBlockTags> registerBlockTags = EventFactory.createLoop();
    Event<RegisterEntityTags> registerEntityTags = EventFactory.createLoop();
    Event<RegisterEntityTypeTags> registerEntityTypeTags = EventFactory.createLoop();
    Event<RegisterItemTags> registerItemTags = EventFactory.createLoop();
    Event<RegisterFluidTags> registerFluidTags = EventFactory.createLoop();
    Event<RegisterEnchantmentTags> registerEnchantmentTags = EventFactory.createLoop();
    Event<RegisterGameEventTags> registerGameEventTags = EventFactory.createLoop();

    interface RegisterAdvancements {
        void registerAdvancements(Consumer<AdvancementHolder> advancementConsumer);
    }

    interface RegisterBlockLoot {
        void registerBlockLoot(BlockLootSubProvider helper);
    }

    interface RegisterBlockTags {
        void registerTags(TagHelper<Block> helper, HolderLookup.Provider provider);
    }

    interface RegisterEntityTags {
        void registerTags(TagHelper<Entity> helper, HolderLookup.Provider provider);
    }

    interface RegisterEntityTypeTags {
        void registerTags(TagHelper<EntityType> helper, HolderLookup.Provider provider);
    }

    interface RegisterItemTags {
        void registerTags(TagHelper<Item> helper, HolderLookup.Provider provider);
    }

    interface RegisterFluidTags {
        void registerTags(TagHelper<Fluid> helper, HolderLookup.Provider provider);
    }

    interface RegisterEnchantmentTags {
        void registerTags(TagHelper<Enchantment> helper, HolderLookup.Provider provider);
    }

    interface RegisterGameEventTags {
        void registerTags(TagHelper<GameEvent> helper, HolderLookup.Provider provider);
    }


}
