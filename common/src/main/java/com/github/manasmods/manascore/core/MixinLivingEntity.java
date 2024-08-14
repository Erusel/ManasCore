package com.github.manasmods.manascore.core;

import com.github.manasmods.manascore.api.world.entity.EntityEvents;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import com.github.manasmods.manascore.attribute.ManasCoreAttributes;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
    @Shadow public abstract float getJumpBoostPower();

    @Inject(method = "getJumpPower", at = @At("RETURN"), cancellable = true)
    protected void getJumpPower(CallbackInfoReturnable<Float> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        Holder<Attribute> attributeHolder = Holder.direct(ManasCoreAttributes.JUMP_STRENGTH.get());

        AttributeInstance instance = entity.getAttribute(attributeHolder);
        if (instance == null) return;

        double newJump = (cir.getReturnValue() - getJumpBoostPower()) / 0.42F * instance.getValue();
        cir.setReturnValue((float) (newJump + getJumpBoostPower()));
    }

    @ModifyArg(method = "causeFallDamage", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;calculateFallDamage(FF)I"), index = 0)
    public float causeFallDamage(float fallDistance, float multiplier) {
        LivingEntity entity = (LivingEntity) (Object) this;
        Holder<Attribute> attributeHolder = Holder.direct(ManasCoreAttributes.JUMP_STRENGTH.get());
        AttributeInstance instance = entity.getAttribute(attributeHolder);
        if (instance == null) return fallDistance;

        float additionalJumpBlock = (float) ((instance.getValue() - 0.42F) / 0.1F);
        return fallDistance - additionalJumpBlock;
    }

    @Inject(method = "jumpInLiquid", at = @At("HEAD"))
    protected void jumpInLiquid(TagKey<Fluid> fluidTag, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        Holder<Attribute> attributeHolder = Holder.direct(ManasCoreAttributes.SWIM_SPEED_ADDITION.get());
        AttributeInstance instance = entity.getAttribute(attributeHolder);
        if (instance == null) return;
        entity.setDeltaMovement(entity.getDeltaMovement().add(0.0, 0.04 * instance.getValue() - 0.04, 0.0));
    }

    @Inject(method = "goDownInWater", at = @At("HEAD"))
    protected void goDownInWater(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        Holder<Attribute> attributeHolder = Holder.direct(ManasCoreAttributes.SWIM_SPEED_ADDITION.get());
        AttributeInstance instance = entity.getAttribute(attributeHolder);
        if (instance == null) return;
        entity.setDeltaMovement(entity.getDeltaMovement().add(0.0, 0.04 - 0.04 * instance.getValue(), 0.0));
    }

    @ModifyArg(method = "travel", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;moveRelative(FLnet/minecraft/world/phys/Vec3;)V", ordinal = 0))
    public float travel(float speed) {
        LivingEntity entity = (LivingEntity) (Object) this;
        Holder<Attribute> attributeHolder = Holder.direct(ManasCoreAttributes.SWIM_SPEED_ADDITION.get());
        AttributeInstance instance = entity.getAttribute(attributeHolder);
        if (instance == null) return speed;
        return (float) (speed * instance.getValue());
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V", shift = Shift.BEFORE))
    void onPreTick(CallbackInfo ci) {
        EntityEvents.LIVING_PRE_TICK.invoker().tick((LivingEntity) (Object) this);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;refreshDirtyAttributes()V", shift = Shift.AFTER))
    void onPostTick(CallbackInfo ci) {
        EntityEvents.LIVING_POST_TICK.invoker().tick((LivingEntity) (Object) this);
    }
}
