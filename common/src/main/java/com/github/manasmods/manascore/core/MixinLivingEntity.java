package com.github.manasmods.manascore.core;

import com.github.manasmods.manascore.api.world.entity.EntityEvents;
import com.github.manasmods.manascore.attribute.ManasCoreAttributeUtils;
import com.github.manasmods.manascore.attribute.ManasCoreAttributes;
import com.github.manasmods.manascore.utils.Changeable;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V", shift = Shift.BEFORE))
    void onPreTick(CallbackInfo ci) {
        EntityEvents.LIVING_PRE_TICK.invoker().tick((LivingEntity) (Object) this);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;refreshDirtyAttributes()V", shift = Shift.AFTER))
    void onPostTick(CallbackInfo ci) {
        EntityEvents.LIVING_POST_TICK.invoker().tick((LivingEntity) (Object) this);
    }

    @ModifyVariable(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F", shift = Shift.BEFORE), argsOnly = true)
    float modifyDamage(float amount, @Local DamageSource damageSource) {
        //Adds Critical Attack system for non-player mobs
        if (damageSource.getDirectEntity() instanceof Mob mob) {
            double chance = mob.getAttribute(ManasCoreAttributes.CRIT_CHANCE) != null ? mob.getAttributeValue(ManasCoreAttributes.CRIT_CHANCE) : 0;
            if (mob.getRandom().nextFloat() <= chance / 100.0) {
                double multiplier = mob.getAttribute(ManasCoreAttributes.CRIT_MULTIPLIER) != null ? mob.getAttributeValue(ManasCoreAttributes.CRIT_MULTIPLIER) : 1.5;
                amount *= (float) multiplier;
                ManasCoreAttributeUtils.triggerCriticalAttackEffect((LivingEntity) (Object) this, mob);
            }
        }

        Changeable<Float> changeable = Changeable.of(amount);
        if (EntityEvents.LIVING_HURT.invoker().hurt((LivingEntity) (Object) this, damageSource, changeable).isFalse()) return 0.0F;
        return changeable.get();
    }

    @Inject(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F", shift = Shift.BEFORE), cancellable = true)
    void cancelActuallyHurt(DamageSource damageSource, float damageAmount, CallbackInfo ci) {
        if (damageAmount <= 0F) ci.cancel();
    }
}
