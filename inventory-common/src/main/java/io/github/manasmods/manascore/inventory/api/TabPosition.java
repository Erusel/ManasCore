package io.github.manasmods.manascore.inventory.api;

import io.github.manasmods.manascore.inventory.ModuleConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

@AllArgsConstructor
public enum TabPosition {
    LEFT_TOP(ResourceLocation.fromNamespaceAndPath(ModuleConstants.MOD_ID, "textures/gui/tabs/top-left.png"), false),
    TOP(ResourceLocation.fromNamespaceAndPath(ModuleConstants.MOD_ID, "textures/gui/tabs/top.png"), false),
    RIGHT_TOP(ResourceLocation.fromNamespaceAndPath(ModuleConstants.MOD_ID, "textures/gui/tabs/top-right.png"), false),
    LEFT_BOT(ResourceLocation.fromNamespaceAndPath(ModuleConstants.MOD_ID, "textures/gui/tabs/bot-left.png"), true),
    BOT(ResourceLocation.fromNamespaceAndPath(ModuleConstants.MOD_ID, "textures/gui/tabs/bot.png"), true),
    RIGHT_BOT(ResourceLocation.fromNamespaceAndPath(ModuleConstants.MOD_ID, "textures/gui/tabs/bot-right.png"), true);

    private final ResourceLocation tabLocation;
    @Getter
    private final boolean bottom;

    public boolean isTop() {
        return !this.bottom;
    }

    public ResourceLocation texture() {
        return this.tabLocation;
    }
}
