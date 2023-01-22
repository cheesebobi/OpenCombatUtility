package com.LubieKakao1212.opencu.gui;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.gui.container.OmnidispenserMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class OmnidispenserScreen extends AbstractContainerScreen<OmnidispenserMenu> {

    private static final ResourceLocation mainTexture = new ResourceLocation(OpenCUMod.MODID, "textures/gui/omnidispenser_gui.png");

    public OmnidispenserScreen(OmnidispenserMenu container, Inventory inv, Component titleIn) {
        super(container, inv, titleIn);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBg(poseStack, partialTick, mouseX, mouseY);
        super.render(poseStack, mouseX, mouseY, partialTick);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, mainTexture);

        blit(poseStack, leftPos, topPos,0,0, imageWidth, imageHeight);
    }
}
