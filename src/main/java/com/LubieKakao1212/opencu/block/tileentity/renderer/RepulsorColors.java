package com.LubieKakao1212.opencu.block.tileentity.renderer;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.block.BlockRepulsor;
import com.LubieKakao1212.opencu.block.CUBlocks;
import com.LubieKakao1212.opencu.block.tileentity.TileEntityRepulsor;
import mezz.jei.util.MathUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;

public class RepulsorColors {

    private float lerp(float a, float b, float t) {
        return (a*t) + (b * (1-t));
    }

    private int lerpColor(int c1, int c2, float t) {
        int b1 = (c1 & 0xff000000) >> 24;
        int b2 = (c2 & 0xff000000) >> 24;
        int g1 = (c1 & 0x00ff0000) >> 16;
        int g2 = (c2 & 0x00ff0000) >> 16;
        int r1 = (c1 & 0x0000ff00) >> 8;
        int r2 = (c2 & 0x0000ff00) >> 8;
        int a1 = (c1 & 0x000000ff) >> 0;
        int a2 = (c2 & 0x000000ff) >> 0;
        int a = (int)lerp((float)a1, (float)a2, t);
        int r = (int)lerp((float)r1, (float)r2, t);
        int g = (int)lerp((float)g1, (float)g2, t);
        int b = (int)lerp((float)b1, (float)b2, t);
        return a | r << 8 | g << 16 | b << 24;
    }

    private static int offlineColor = 0x80aaffff;
}
