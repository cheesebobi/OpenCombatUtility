/*
package com.LubieKakao1212.opencu.block.entity.renderer;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3d;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FastTESRUtil {

    public static final int BRIGHTNESS = 13 << 4;

    public static void drawCube(BufferBuilder bb, AxisAlignedBB bounds, TextureAtlasSprite sprite, Color tint)
    {
        //North
        drawQuad(new double[]{
                bounds.minX, bounds.minY, bounds.minZ,
                bounds.minX, bounds.maxY, bounds.minZ,
                bounds.maxX, bounds.maxY, bounds.minZ,
                bounds.maxX, bounds.minY, bounds.minZ
        }, bb, sprite, tint);
        //South
        drawQuad(new double[]{
                bounds.minX, bounds.minY, bounds.maxZ,
                bounds.maxX, bounds.minY, bounds.maxZ,
                bounds.maxX, bounds.maxY, bounds.maxZ,
                bounds.minX, bounds.maxY, bounds.maxZ
        }, bb, sprite, tint);
        //West
        drawQuad(new double[]{
                bounds.minX, bounds.minY, bounds.minZ,
                bounds.minX, bounds.minY, bounds.maxZ,
                bounds.minX, bounds.maxY, bounds.maxZ,
                bounds.minX, bounds.maxY, bounds.minZ
        }, bb, sprite, tint);

        //East
        drawQuad(new double[]{
                bounds.maxX, bounds.minY, bounds.minZ,
                bounds.maxX, bounds.minY, bounds.maxZ,
                bounds.maxX, bounds.maxY, bounds.maxZ,
                bounds.maxX, bounds.maxY, bounds.minZ
        }, bb, sprite, tint);

        //UP
        drawQuad(new double[]{
                bounds.maxX, bounds.maxY, bounds.minZ,
                bounds.maxX, bounds.maxY, bounds.maxZ,
                bounds.minX, bounds.maxY, bounds.maxZ,
                bounds.minX, bounds.maxY, bounds.minZ
        }, bb, sprite, tint);

        //Down
        drawQuad(new double[]{
                bounds.maxX, bounds.minY, bounds.minZ,
                bounds.maxX, bounds.minY, bounds.maxZ,
                bounds.minX, bounds.minY, bounds.maxZ,
                bounds.minX, bounds.minY, bounds.minZ
        }, bb, sprite, tint);
    }

    public static void drawQuad(double[] vertices, BufferBuilder bb, TextureAtlasSprite sprite, Color tint)
    {
        //bb.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        bb.pos(vertices[0], vertices[1], vertices[2]);
        bb.color(tint.r, tint.g, tint.b, tint.a);
        bb.tex(sprite.getMinU(), sprite.getMinV());
        bb.lightmap(BRIGHTNESS, BRIGHTNESS);
        bb.endVertex();

        bb.pos(vertices[3], vertices[4], vertices[5]);
        bb.color(tint.r, tint.g, tint.b, tint.a);
        bb.tex(sprite.getMaxU(), sprite.getMinV());
        bb.lightmap(BRIGHTNESS, BRIGHTNESS);
        bb.endVertex();

        bb.pos(vertices[6], vertices[7], vertices[8]);
        bb.color(tint.r, tint.g, tint.b, tint.a);
        bb.tex(sprite.getMaxU(), sprite.getMaxV());
        bb.lightmap(BRIGHTNESS, BRIGHTNESS);
        bb.endVertex();

        bb.pos(vertices[9], vertices[10], vertices[11]);
        bb.color(tint.r, tint.g, tint.b, tint.a);
        bb.tex(sprite.getMinU(), sprite.getMaxV());
        bb.lightmap(BRIGHTNESS, BRIGHTNESS);
        bb.endVertex();
    }
}
*/
