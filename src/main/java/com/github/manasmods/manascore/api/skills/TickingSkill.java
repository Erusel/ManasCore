package com.github.manasmods.manascore.api.skills;

import com.github.manasmods.manascore.api.skills.capability.SkillStorage;
import lombok.Getter;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public class TickingSkill {
    private int duration = 0;
    @Getter
    private final ManasSkill skill;
    public TickingSkill(ManasSkill skill) {
        this.skill = skill;
    }

    public boolean tick(SkillStorage storage, LivingEntity entity) {
        Optional<ManasSkillInstance> optional = storage.getSkill(skill);
        if (optional.isEmpty()) return false;

        ManasSkillInstance instance = optional.get();
        if (reachedMaxDuration(instance, entity) || !instance.canInteractSkill(entity)) {
            skill.removeHeldAttributeModifiers(entity);
            return false;
        }

        boolean onHeld = instance.onHeld(entity, this.duration++);
        if (onHeld) instance.getSkill().addHeldAttributeModifiers(instance, entity);
        return onHeld;
    }

    public boolean reachedMaxDuration(ManasSkillInstance instance, LivingEntity entity) {
        int maxDuration = instance.getMaxHeldTime(entity);
        if (maxDuration == -1) return false;
        return duration >= maxDuration;
    }
}
