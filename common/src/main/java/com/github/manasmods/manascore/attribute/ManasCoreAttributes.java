package com.github.manasmods.manascore.attribute;

import com.github.manasmods.manascore.ManasCore;
import lombok.experimental.UtilityClass;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;

@UtilityClass
public class ManasCoreAttributes {
    public static final Holder<Attribute> CRIT_CHANCE = ManasCore.REGISTER.attribute("crit_chance")
            .withDefaultValue(0)
            .withMinimumValue(0)
            .withMaximumValue(100)
            .applyToAll()
            .syncable()
            .endAsHolder();
    public static final Holder<Attribute> CRIT_MULTIPLIER = ManasCore.REGISTER.attribute("crit_multiplier")
            .withDefaultValue(1.5)
            .withMinimumValue(0)
            .withMaximumValue(1024)
            .applyToAll()
            .syncable()
            .endAsHolder();
    public static final Holder<Attribute> SWEEP_CHANCE = ManasCore.REGISTER.attribute("sweep_chance")
            .withDefaultValue(0)
            .withMinimumValue(0)
            .withMaximumValue(100)
            .applyTo(() -> EntityType.PLAYER)
            .syncable()
            .endAsHolder();

    public static void init() {
        // Just loads the class
    }
}
