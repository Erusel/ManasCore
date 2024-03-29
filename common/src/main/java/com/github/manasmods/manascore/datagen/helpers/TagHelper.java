package com.github.manasmods.manascore.datagen.helpers;

import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;

public interface TagHelper<T> {

    public TagBuilder createTag(TagKey<T> tag);

}
