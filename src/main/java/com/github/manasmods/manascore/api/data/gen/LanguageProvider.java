/*
 * Copyright (c) 2022. ManasMods
 */

package com.github.manasmods.manascore.api.data.gen;


import net.minecraftforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.ApiStatus.OverrideOnly;

@AvailableSince("1.0.0.0")
public abstract class LanguageProvider extends net.minecraftforge.common.data.LanguageProvider {
    public LanguageProvider(GatherDataEvent gatherDataEvent, String modid) {
        this(gatherDataEvent, modid, "en_us");
    }

    public LanguageProvider(GatherDataEvent gatherDataEvent, String modid, String localeCode) {
        super(gatherDataEvent.getGenerator(), modid, localeCode);
    }

    @OverrideOnly
    protected abstract void generate();

    @Override
    protected void addTranslations() {
        generate();
    }
}
