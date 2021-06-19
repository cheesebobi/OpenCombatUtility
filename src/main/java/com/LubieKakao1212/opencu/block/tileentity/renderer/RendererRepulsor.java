package com.LubieKakao1212.opencu.block.tileentity.renderer;

import com.LubieKakao1212.opencu.block.tileentity.TileEntityRepulsor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.model.animation.FastTESR;

public class RendererRepulsor extends FastTESR<TileEntityRepulsor> {

    private static final AxisAlignedBB coreBounds = new AxisAlignedBB(0,0,0,0,0,0).grow(0.825 / 2).offset(0.5, 0.5, 0.5);
    private static TextureAtlasSprite lanternSprite = null;

    public RendererRepulsor() {
    }

    public static final Color offlineColor = new Color(0.25f, 0.5f,0.5f, 1f);
    public static final Color onlineColor = new Color(0f, 1f,1f, 1f);

    @Override
    public void renderTileEntityFast(TileEntityRepulsor te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
        float animProgress = (te.pulseTicksLeft - partialTicks) / TileEntityRepulsor.pulseTicks;

        if(animProgress < 0)
        {
            animProgress = 0;
        }
        if(lanternSprite == null)
        {
            lanternSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/sea_lantern");
        }

        FastTESRUtil.drawCube(buffer, coreBounds.offset(x, y, z), lanternSprite, Color.lerp(offlineColor, onlineColor, animProgress));
    }
}
