package com.github.manasmods.manascore.api.attribute.event;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Getter
@Cancelable
public class ArrowCriticalChanceEvent extends LivingEvent {
    @Setter
    private double critChance;
    private final Projectile arrow;
    public ArrowCriticalChanceEvent(LivingEntity owner, Projectile arrow, double critChance) {
        super(owner);
        this.arrow = arrow;
        this.critChance = critChance;
    }
}
