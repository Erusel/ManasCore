package io.github.manasmods.manascore.inventory.api;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;

public abstract class AbstractInventoryTab extends Button implements InventoryTab {
    protected static final int TAB_WIDTH = 28;
    protected static final int TAB_HEIGHT = 32;
    protected final Minecraft minecraft;
    @Getter
    @Setter
    private TabPosition position;

    public AbstractInventoryTab(Tooltip tooltip) {
        super(0, 0, TAB_WIDTH, TAB_HEIGHT, Component.empty(), button -> {
            AbstractInventoryTab tab = (AbstractInventoryTab) button;
            tab.sendOpenContainerPacket();
        }, Button.DEFAULT_NARRATION);
        setTooltip(tooltip);
        this.minecraft = Minecraft.getInstance();
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        renderBg(guiGraphics, mouseX, mouseY, partialTicks);
        renderIcon(guiGraphics, mouseX, mouseY, partialTicks);

        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    protected abstract void renderIcon(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks);

    protected void renderBg(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        float yOffset = this.isCurrent() ? TAB_HEIGHT : 0F;
        guiGraphics.blit(this.position.texture(), this.getX(), this.getY(), TAB_WIDTH, TAB_HEIGHT, 0F, yOffset, TAB_WIDTH, TAB_HEIGHT - 1, TAB_WIDTH, TAB_HEIGHT * 2);
    }

    public boolean isCurrent() {
        return this.isCurrentScreen().test(minecraft.screen);
    }
}
