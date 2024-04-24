package com.github.manasmods.manascore.capability.skill;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.manascore.api.skills.event.UnlockSkillEvent;
import lombok.extern.log4j.Log4j2;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
@Log4j2
public class TestSkill extends ManasSkill {
    protected static final String TEST = "e83b2e47-ef49-4af5-b5da-4fd14a5c8777";
    public TestSkill(){
        MinecraftForge.EVENT_BUS.addListener(this::unlock);
        this.addHeldAttributeModifier(Attributes.MOVEMENT_SPEED, TEST, 1.0F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    public boolean canTick(ManasSkillInstance instance, LivingEntity entity) {
        return instance.isToggled();
    }

    public void onToggleOn(ManasSkillInstance instance, LivingEntity entity) {
        log.debug("Toggled On");
    }

    public void onToggleOff(ManasSkillInstance instance, LivingEntity entity) {
        log.debug("Toggled Off");
    }

    public void onPressed(ManasSkillInstance instance, LivingEntity entity) {
        log.debug("I'm pressed");
    }

    public boolean onHeld(ManasSkillInstance instance, LivingEntity living, int heldTicks) {
        log.debug("Held for {} ticks", heldTicks);
        return true;
    }

    public void onRelease(ManasSkillInstance instance, LivingEntity entity, int heldTicks) {
        log.debug("I'm released after {} ticks", heldTicks);
    }

    public void onTick(ManasSkillInstance instance, LivingEntity living) {
        if (living instanceof Player player && player.isSecondaryUseActive())
            log.debug("You're sneaky");
    }

    public void onScroll(ManasSkillInstance instance, LivingEntity living, double delta) {
        log.debug("Scroll delta: {}", delta);
    }

    public void onLearnSkill(ManasSkillInstance instance, LivingEntity living, UnlockSkillEvent e) {
        log.debug("Learnt test skill");
    }

    public void onRightClickBlock(ManasSkillInstance instance, LivingEntity entity, BlockHitResult hitResult) {
        log.debug("Block: {}", entity.getLevel().getBlockState(hitResult.getBlockPos()).getBlock().getName());
    }

    public void onBeingTargeted(ManasSkillInstance instance, LivingEntity target, LivingChangeTargetEvent event) {
        if (event.getEntity() instanceof Spider)
            log.debug("Targeted by {}", event.getEntity().getName());
    }

    public void onBeingDamaged(ManasSkillInstance instance, LivingAttackEvent event) {
        if (event.getSource().equals(DamageSource.CACTUS)) {
            log.debug("No cactus touchy");
            event.setCanceled(true);
        }
    }

    public void onDamageEntity(ManasSkillInstance instance, LivingEntity entity, LivingHurtEvent event) {
        if (event.getEntity() instanceof Creeper creeper) {
            creeper.kill();
            log.debug("No creeper");
        }
    }

    public void onTouchEntity(ManasSkillInstance instance, LivingEntity entity, LivingHurtEvent event) {
        instance.setMastery(instance.getMastery() + 1);
        log.debug("My mastery is {}", instance.getMastery());
    }

    public void onTakenDamage(ManasSkillInstance instance, LivingDamageEvent event) {
        float amount = event.getAmount() / 2;
        event.getEntity().heal(amount);
        log.debug("Healed {} by {} health", event.getEntity().getName().getString(), amount);
    }

    public void onProjectileHit(ManasSkillInstance instance, LivingEntity living, ProjectileImpactEvent event) {
        if (event.getProjectile() instanceof ThrownTrident) {
            log.debug("Dodged");
            event.setCanceled(true);
        }
    }

    public void onDeath(ManasSkillInstance instance, LivingDeathEvent event) {
        log.debug("Welcome to the phantom realm");
    }

    public void onRespawn(ManasSkillInstance instance, PlayerEvent.PlayerRespawnEvent event) {
        log.debug("Welcome to the living realm");
    }

    private void unlock(final LivingEntityUseItemEvent.Finish e){
        if (e.getEntity() instanceof ServerPlayer player) {
            if (e.getItem().is(Items.APPLE)) {
                if (SkillAPI.getSkillsFrom(player).learnSkill(this)) {
                    log.debug("Unlocked example Test skill for player {}", player.getName().getString());
                }
            }
        }
    }
}
