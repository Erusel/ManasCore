package com.github.manasmods.manascore.api.skills.event;

import lombok.Getter;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.ApiStatus;

/**
 * This Event splits the {@link LivingHurtEvent} into three phases in a sorted order.
 * Canceling this event will cancel the Damage reduction.
 * <p>
 */
@ApiStatus.AvailableSince("2.0.18.0")
public class SkillDamageEvent extends Event {
    @Getter
    private final LivingHurtEvent event;

    public SkillDamageEvent(LivingHurtEvent event) {
        this.event = event;
    }

    public LivingEntity getEntity() {
        return this.event.getEntity();
    }

    public DamageSource getSource() {
        return this.event.getSource();
    }

    public float getAmount() {
        return this.event.getAmount();
    }

    public void setAmount(float amount) {
        this.event.setAmount(amount);
    }

    public boolean isCanceled() {
        return this.event.isCanceled();
    }

    public void setCanceled(boolean cancel) {
        this.event.setCanceled(cancel);
    }

    public Result getResult() {
        return this.event.getResult();
    }

    public void setResult(Result value) {
        this.event.setResult(value);
    }

    /**
     * This Event is fired by {@link LivingHurtEvent} before touching the barrier.
     * Canceling this event will cancel {@link LivingHurtEvent} and every event following after it.
     * <p>
     */
    public static class PreCalculation extends SkillDamageEvent {
        public PreCalculation(LivingHurtEvent event) {
            super(event);
        }
    }

    /**
     * This Event is fired by {@link LivingHurtEvent} after {@link PreCalculation} and before {@link PostCalculation}.
     * Canceling this event will cancel the Damage reduction.
     * <p>
     */
    public static class Calculation extends SkillDamageEvent {
        public Calculation(LivingHurtEvent event) {
            super(event);
        }
    }

    /**
     * This Event is fired by {@link LivingHurtEvent} after the damage passed the barrier and right before touching the player.
     * Canceling this event will cancel {@link LivingHurtEvent} and every event following after it.
     * <p>
     */
    public static class PostCalculation extends SkillDamageEvent {
        public PostCalculation(LivingHurtEvent event) {
            super(event);
        }
    }
}