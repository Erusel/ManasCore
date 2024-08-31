package com.github.manasmods.manascore.skill;

import com.github.manasmods.manascore.ManasCore;
import com.github.manasmods.manascore.api.skill.*;
import com.github.manasmods.manascore.api.world.entity.EntityEvents;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;

import java.util.List;
import java.util.Optional;

public class SkillRegistry {
    private static final ResourceLocation registryId = ResourceLocation.fromNamespaceAndPath(ManasCore.MOD_ID, "skills");
    public static final Registrar<ManasSkill> SKILLS = RegistrarManager.get(ManasCore.MOD_ID).<ManasSkill>builder(registryId)
            .syncToClients()
            .build();
    public static final ResourceKey<Registry<ManasSkill>> KEY = (ResourceKey<Registry<ManasSkill>>) SKILLS.key();


    public static void init() {
        InteractionEvent.RIGHT_CLICK_BLOCK.register((player, hand, pos, face) -> {
            if (player.level().isClientSide()) return EventResult.pass();
            Skills storage = SkillAPI.getSkillsFrom(player);

            for (ManasSkillInstance instance : List.copyOf(storage.getLearnedSkills())) {
                Optional<ManasSkillInstance> optional = storage.getSkill(instance.getSkill());
                if (optional.isEmpty()) continue;

                if (!optional.get().canInteractSkill(player)) continue;
                optional.get().onRightClickBlock(player, hand, pos, face);
            }

            return EventResult.pass();
        });

        EntityEvents.LIVING_CHANGE_TARGET.register((entity, changeableTarget) -> {
            LivingEntity owner = changeableTarget.get();
            if (owner == null) return EventResult.pass();

            Skills storage = SkillAPI.getSkillsFrom(owner);
            for (ManasSkillInstance instance : List.copyOf(storage.getLearnedSkills())) {
                Optional<ManasSkillInstance> optional = storage.getSkill(instance.getSkill());
                if (optional.isEmpty()) continue;

                if (!optional.get().canInteractSkill(owner)) continue;
                if (!optional.get().onBeingTargeted(changeableTarget, entity)) return EventResult.interruptFalse();
            }

            return EventResult.pass();
        });

        EntityEvents.LIVING_ATTACK.register((entity, source, amount) -> {
            Skills storage = SkillAPI.getSkillsFrom(entity);

            for (ManasSkillInstance instance : List.copyOf(storage.getLearnedSkills())) {
                Optional<ManasSkillInstance> optional = storage.getSkill(instance.getSkill());
                if (optional.isEmpty()) continue;

                if (!optional.get().canInteractSkill(entity)) continue;
                if (!optional.get().onBeingDamaged(entity, source, amount)) return EventResult.interruptFalse();
            }

            return EventResult.pass();
        });

        SkillEvents.SKILL_DAMAGE_PRE_CALCULATION.register((storage, target, source, amount) -> {
            if (!(source.getEntity() instanceof LivingEntity owner)) return EventResult.pass();

           for (ManasSkillInstance instance : List.copyOf(storage.getLearnedSkills())) {
                Optional<ManasSkillInstance> optional = storage.getSkill(instance.getSkill());
                if (optional.isEmpty()) continue;

                if (!optional.get().canInteractSkill(owner)) continue;
                if (!optional.get().onDamageEntity(owner, target, source, amount)) return EventResult.interruptFalse();
            }

            return EventResult.pass();
        });

        SkillEvents.SKILL_DAMAGE_POST_CALCULATION.register((storage, target, source, amount) -> {
            if (!(source.getEntity() instanceof LivingEntity owner)) return EventResult.pass();

            for (ManasSkillInstance instance : List.copyOf(storage.getLearnedSkills())) {
                Optional<ManasSkillInstance> optional = storage.getSkill(instance.getSkill());
                if (optional.isEmpty()) continue;

                if (!optional.get().canInteractSkill(owner)) continue;
                if (!optional.get().onTouchEntity(owner, target, source, amount)) return EventResult.interruptFalse();
            }

            return EventResult.pass();
        });

        EntityEvents.LIVING_DAMAGE.register((entity, source, amount) -> {
            Skills storage = SkillAPI.getSkillsFrom(entity);

            for (ManasSkillInstance instance : List.copyOf(storage.getLearnedSkills())) {
                Optional<ManasSkillInstance> optional = storage.getSkill(instance.getSkill());
                if (optional.isEmpty()) continue;

                if (!optional.get().canInteractSkill(entity)) continue;
                if (!optional.get().onTakenDamage(entity, source, amount)) return EventResult.interruptFalse();
            }

            return EventResult.pass();
        });

        EntityEvent.LIVING_DEATH.register((entity, source) -> {
            Skills storage = SkillAPI.getSkillsFrom(entity);

            for (ManasSkillInstance instance : List.copyOf(storage.getLearnedSkills())) {
                Optional<ManasSkillInstance> optional = storage.getSkill(instance.getSkill());
                if (optional.isEmpty()) continue;

                if (!optional.get().canInteractSkill(entity)) continue;
                if (!optional.get().onDeath(entity, source)) return EventResult.interruptFalse();
            }

            return EventResult.pass();
        });

        PlayerEvent.PLAYER_RESPAWN.register((newPlayer, conqueredEnd, removalReason) -> {
            Skills storage = SkillAPI.getSkillsFrom(newPlayer);
            for (ManasSkillInstance instance : List.copyOf(storage.getLearnedSkills())) {
                Optional<ManasSkillInstance> optional = storage.getSkill(instance.getSkill());
                if (optional.isEmpty()) continue;

                if (!optional.get().canInteractSkill(newPlayer)) continue;
                optional.get().onRespawn(newPlayer, conqueredEnd);
            }
        });

        EntityEvents.PROJECTILE_HIT.register((result, projectile, hitResultChangeable) -> {
            if (!(result instanceof EntityHitResult hitResult)) return;
            if (!(hitResult.getEntity() instanceof LivingEntity hitEntity)) return;
            Skills storage = SkillAPI.getSkillsFrom(hitEntity);

            for (ManasSkillInstance instance : List.copyOf(storage.getLearnedSkills())) {
                Optional<ManasSkillInstance> optional = storage.getSkill(instance.getSkill());
                if (optional.isEmpty()) continue;

                if (!optional.get().canInteractSkill(hitEntity)) continue;
                optional.get().onProjectileHit(hitEntity, hitResult, projectile, hitResultChangeable);
            }
        });
    }

    private SkillRegistry() {
    }
}
