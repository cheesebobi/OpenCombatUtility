package com.LubieKakao1212.opencu.block.tileentity.renderer;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.block.CUBlocks;
import com.LubieKakao1212.opencu.block.tileentity.TileEntityRepulsor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
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

        renderModel(te, animProgress);

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    private void renderModel(TileEntityRepulsor te, float progress) {
        //GlStateManager.pushMatrix();
        RenderHelper.disableStandardItemLighting();

            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();

            //IBlockState stateGlow = CUBlocks.repulsor.getDefaultState().withProperty(BlockRepulsor.PART_NUMBER, 0);
            IBlockState stateFrame = CUBlocks.repulsor.getDefaultState();//.withProperty(BlockRepulsor.PART_NUMBER, 1);

            //IBakedModel modelGlow = dispatcher.getModelForState(stateGlow);
            IBakedModel modelFrame = dispatcher.getModelForState(stateFrame);

            //GlStateManager.color(0f, 1f, 1f, 0.5f);
            //bufferBuilder.color(0f, 1f, 1f, 0.5f);
            //dispatcher.getBlockModelRenderer().renderModel(te.getWorld(), modelGlow, stateGlow, te.getPos(), bufferBuilder, true);



            GlStateManager.pushMatrix();
                //GlStateManager.scale(2,2,2);
                GlStateManager.translate(0.0625f,0.0625f,0.0625f);
                GlStateManager.scale(0.875f,0.875f,0.875f);
                GlStateManager.enableAlpha();
                TESRUtil.drawCube(tessellator, bufferBuilder, Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/sea_lantern"), Color.lerp(offlineColor, onlineColor, progress));
            GlStateManager.popMatrix();


            /*GlStateManager.translate(0.5f, 0.5f, 0.5f);
            GlStateManager.scale(0.5f, 0.5f, 0.5f);
            bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
            GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());

            dispatcher.getBlockModelRenderer().renderModel(te.getWorld(), modelFrame, stateFrame, te.getPos(), bufferBuilder, true);
            tessellator.draw();*/

        RenderHelper.enableStandardItemLighting();
        //GlStateManager.popMatrix();
    }

}
