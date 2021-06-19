package com.LubieKakao1212.opencu.block.tileentity.renderer;

import com.LubieKakao1212.opencu.OpenCUMod;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public class TESRUtil {

    public static final int BRIGHTNESS = 13 << 4;

    public static void drawCube(Tessellator tes, BufferBuilder bb, TextureAtlasSprite sprite, Color tint)
    {
        GlStateManager.pushMatrix();
            GlStateManager.translate(0,0,1f);
            drawHalfCube(tes, bb, sprite, tint, false);
            GlStateManager.rotate(90, 1, 0, 0);
            GlStateManager.rotate(90, 0, 1, 0);
            GlStateManager.rotate(90, 0, 0, 1);
            GlStateManager.translate(-1,-1f,1f);
            drawHalfCube(tes, bb, sprite, tint, false);
        GlStateManager.popMatrix();
    }

    public static void drawHalfCube(Tessellator tes, BufferBuilder bb, TextureAtlasSprite sprite, Color tint, boolean invert)
    {
        GlStateManager.pushMatrix();
            drawQuad(tes, bb, sprite, tint, invert);
            GlStateManager.rotate(90, 0, 1, 0);
            drawQuad(tes, bb, sprite, tint, !invert);
            GlStateManager.rotate(90, 1, 0, 0);
            drawQuad(tes, bb, sprite, tint, invert);
        GlStateManager.popMatrix();
    }

    public static void drawQuad(Tessellator tes, BufferBuilder bb, TextureAtlasSprite sprite, Color tint, boolean invert)
    {
        float p = 1f;
        float n = 0f;

        if (invert) {
            bb.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
            bb.pos(p, n, 0);
            bb.color(tint.r, tint.g, tint.b, tint.a);
            bb.tex(sprite.getMaxU(), sprite.getMinV());
            //bb.normal(0, 0, 1);
            bb.lightmap(BRIGHTNESS, BRIGHTNESS);
            bb.endVertex();

            bb.pos(n, n, 0);
            bb.color(tint.r, tint.g, tint.b, tint.a);
            bb.tex(sprite.getMinU(), sprite.getMinV());
            //bb.normal(0, 0, 1);
            bb.lightmap(BRIGHTNESS, BRIGHTNESS);
            bb.endVertex();

            bb.pos(n, p, 0);
            bb.color(tint.r, tint.g, tint.b, tint.a);
            bb.tex(sprite.getMinU(), sprite.getMaxV());
            //bb.normal(0, 0, 1);
            bb.lightmap(BRIGHTNESS, BRIGHTNESS);
            bb.endVertex();

            bb.pos(p, p, 0);
            bb.color(tint.r, tint.g, tint.b, tint.a);
            bb.tex(sprite.getMaxU(), sprite.getMaxV());
            //bb.normal(0, 0, 1);
            bb.lightmap(BRIGHTNESS, BRIGHTNESS);
            bb.endVertex();
            tes.draw();
        }else
        {
            bb.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
            bb.pos(n, n, 0);
            bb.color(tint.r, tint.g, tint.b, tint.a);
            bb.tex(sprite.getMinU(), sprite.getMinV());
            //bb.normal(0, 0, -1);
            bb.lightmap(BRIGHTNESS, BRIGHTNESS);
            bb.endVertex();

            bb.pos(p, n, 0);
            bb.color(tint.r, tint.g, tint.b, tint.a);
            bb.tex(sprite.getMaxU(), sprite.getMinV());
            //bb.normal(0, 0, -1);
            bb.lightmap(BRIGHTNESS, BRIGHTNESS);
            bb.endVertex();

            bb.pos(p, p, 0);
            bb.color(tint.r, tint.g, tint.b, tint.a);
            bb.tex(sprite.getMaxU(), sprite.getMaxV());
            //bb.normal(0, 0, -1);
            bb.lightmap(BRIGHTNESS, BRIGHTNESS);
            bb.endVertex();

            bb.pos(n, p, 0);
            bb.color(tint.r, tint.g, tint.b, tint.a);
            bb.tex(sprite.getMinU(), sprite.getMaxV());
            //bb.normal(0, 0, -1);
            bb.lightmap(BRIGHTNESS, BRIGHTNESS);
            bb.endVertex();

            tes.draw();
        }
    }

}
