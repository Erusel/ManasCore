package com.github.manasmods.manascore.api.attribute;

import lombok.experimental.UtilityClass;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Optional;
import java.util.UUID;

@UtilityClass
public class ManasCoreAttributeHelper {
    /**
     * Safe way to apply {@link AttributeModifier} to an {@link LivingEntity}
     *
     * @param entity    Target Entity
     * @param attribute Target Attribute
     * @param modifier  Attribute modifier
     */
    public static void setModifier(final LivingEntity entity, final Holder<Attribute> attribute, final AttributeModifier modifier) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance == null) return;

        double oldMaxHealth = attribute == Attributes.MAX_HEALTH ? entity.getMaxHealth() : 0; // Store old max health or 0
        Optional.ofNullable(instance.getModifier(modifier.id())) //Get old modifier if present
                .ifPresent(modifier1 -> instance.removeModifier(modifier1.id())); //Remove old modifier

        instance.addPermanentModifier(modifier); //Add modifier

        if (attribute == Attributes.MAX_HEALTH) {
            double newMaxHealth = entity.getMaxHealth();
            if (newMaxHealth > oldMaxHealth) {
                double healAmount = (newMaxHealth - oldMaxHealth);
                entity.heal((float) healAmount); //Heal entity by the amount of gained health
            } else {
                if (entity.getHealth() > newMaxHealth) {
                    entity.setHealth((float) newMaxHealth); //Reduce the entity health to the new max value
                }
            }
        }
    }

    /**
     * @param entity                      Target Entity
     * @param attribute                   Target Attribute
     * @param attributeModifierLocation   A unique ResourceLocation to identify this {@link AttributeModifier}
     * @param amount                      Will be used to calculate the final value of the {@link Attribute}
     * @param attributeOperation          Mathematical operation type which is used to calculate the final value of the {@link Attribute}
     */
    public static void addModifier(final LivingEntity entity, final Holder<Attribute> attribute, final ResourceLocation attributeModifierLocation, final double amount,
                                   final AttributeModifier.Operation attributeOperation) {
        setModifier(entity, attribute, new AttributeModifier(attributeModifierLocation, amount, attributeOperation));
    }

    /**
     * Safe way to remove {@link AttributeModifier} from Entities
     *
     * @param entity                    Target Entity
     * @param attribute                 Target Attribute
     * @param attributeModifierLocation Unique modifier ResourceLocation
     */
    public static void removeModifier(final LivingEntity entity, final Holder<Attribute> attribute, final ResourceLocation attributeModifierLocation) {
        Optional.ofNullable(entity.getAttribute(attribute)).ifPresent(attributeInstance -> attributeInstance.removeModifier(attributeModifierLocation));
    }

    /**
     * Safe way to remove {@link AttributeModifier} from Entities
     *
     * @param entity    Target Entity
     * @param attribute Target Attribute
     * @param modifier  Modifier
     */
    public static void removeModifier(final LivingEntity entity, final Holder<Attribute> attribute, final AttributeModifier modifier) {
        removeModifier(entity, attribute, modifier.id());
    }
}
