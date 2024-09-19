package com.github.manasmods.manascore.api.attribute.event;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Getter
@Cancelable
public class CriticalChanceEvent extends LivingEvent {
    @Setter
    private float damageModifier;
    @Setter
    private double critChance;
    private final float oldDamageModifier;
    private final Entity target;

    public CriticalChanceEvent(LivingEntity attacker, Entity target, float damageModifier, double critChance) {
        super(attacker);
        this.target = target;
        this.oldDamageModifier = damageModifier;
        this.critChance = critChance;
    }
}
