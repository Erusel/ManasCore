package com.github.manasmods.manascore.neoforge;

import com.github.manasmods.manascore.api.world.entity.EntityEvents;
import com.github.manasmods.manascore.utils.Changeable;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber
public class NeoForgeCommonEventInvoker {
    private NeoForgeCommonEventInvoker() {
    }

    @SubscribeEvent
    static void onLivingChangeTarget(final LivingChangeTargetEvent e) {
        Changeable<LivingEntity> changeableTarget = Changeable.of(e.getNewTarget());
        if (EntityEvents.LIVING_CHANGE_TARGET.invoker().changeTarget(e.getEntity(), changeableTarget).isFalse()) {
            e.setCanceled(true);
        } else {
            e.setNewTarget(changeableTarget.get());
        }
    }

    @SubscribeEvent
    static void onLivingDamage(final LivingDamageEvent.Pre e) {
        DamageContainer container = e.getContainer();
        Changeable<Float> amount = Changeable.of(container.getNewDamage());
        if (EntityEvents.LIVING_DAMAGE.invoker().damage(e.getEntity(), container.getSource(), amount).isFalse()) {
            container.setNewDamage(0);
        } else container.setNewDamage(amount.get());
    }

    @SubscribeEvent
    static void onProjectileHit(final ProjectileImpactEvent e) {
        Changeable<EntityEvents.ProjectileHitResult> result = Changeable.of(EntityEvents.ProjectileHitResult.DEFAULT);
        EntityEvents.PROJECTILE_HIT.invoker().hit(e.getRayTraceResult(), e.getProjectile(), result);
        if (result.get().equals(EntityEvents.ProjectileHitResult.PASS)) e.setCanceled(true);
    }
}
