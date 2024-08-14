package com.github.manasmods.manascore.fabric.core;

import com.github.manasmods.manascore.api.world.entity.EntityEvents;
import com.github.manasmods.manascore.api.world.entity.EntityEvents.ProjectileHitResult;
import com.github.manasmods.manascore.utils.Changeable;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Projectile.class)
public abstract class MixinProjectile {

    @WrapMethod(method = "onHitEntity")
    void onHit(EntityHitResult result, Operation<Void> original) {
        Changeable<ProjectileHitResult> resultChangeable = Changeable.of(ProjectileHitResult.DEFAULT);
        EntityEvents.PROJECTILE_HIT.invoker().hit(result, ((Projectile)((Object)this)), resultChangeable);

        if (!resultChangeable.get().equals(ProjectileHitResult.DEFAULT)) return;
        original.call(result);
    }
}
