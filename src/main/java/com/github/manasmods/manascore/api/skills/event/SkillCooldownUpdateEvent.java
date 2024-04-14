package com.github.manasmods.manascore.api.skills.event;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import lombok.Getter;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import org.jetbrains.annotations.ApiStatus;

/**
 * This Event is fired right before {@link ManasSkillInstance#decreaseCoolDown} is invoked.
 * Cancel this event to prevent the {@link ManasSkillInstance#decreaseCoolDown} invocation.
 */
@ApiStatus.AvailableSince("2.0.18.0")
@Cancelable
public class SkillCooldownUpdateEvent extends SkillEvent {
    @Getter
    private final LivingEntity entity;
    @Getter
    private final int currentCooldown;
    public SkillCooldownUpdateEvent(ManasSkillInstance skillInstance, LivingEntity entity, int currentCooldown) {
        super(skillInstance);
        this.entity = entity;
        this.currentCooldown = currentCooldown;
    }
}