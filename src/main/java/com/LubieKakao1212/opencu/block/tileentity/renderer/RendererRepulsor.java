package com.LubieKakao1212.opencu.block.tileentity.renderer;

import com.LubieKakao1212.opencu.block.tileentity.TileEntityRepulsor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class RendererRepulsor extends TileEntitySpecialRenderer<TileEntityRepulsor> {

    public RendererRepulsor() {

    }

    public static final Color offlineColor = new Color(0.25f, 0.5f,0.5f, 1f);
    public static final Color onlineColor = new Color(0f, 1f,1f, 1f);

    @Override
    public void render(TileEntityRepulsor te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();

        float animProgress = (te.pulseTicksLeft - partialTicks) / TileEntityRepulsor.pulseTicks;

        if(animProgress < 0)
        {
            animProgress = 0;
        }

        renderCore(animProgress);

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    private void renderCore(float progress) {
        RenderHelper.disableStandardItemLighting();

            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();

            GlStateManager.pushMatrix();
                //GlStateManager.scale(2,2,2);
                GlStateManager.translate(0.0625f,0.0625f,0.0625f);
                GlStateManager.scale(0.875f,0.875f,0.875f);
                GlStateManager.enableAlpha();
                TESRUtil.drawCube(tessellator, bufferBuilder, Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/sea_lantern"), Color.lerp(offlineColor, onlineColor, progress));
            GlStateManager.popMatrix();

        RenderHelper.enableStandardItemLighting();
    }

}
