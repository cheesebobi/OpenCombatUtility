package com.LubieKakao1212.opencu.block.entity.renderer;

import com.LubieKakao1212.opencu.block.entity.BlockEntityRepulsor;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class RendererRepulsor implements BlockEntityRenderer<BlockEntityRepulsor> {

    private static final AABB coreBounds = new AABB(0,0,0,0,0,0).inflate(0.825 / 2).move(0.5, 0.5, 0.5);
    private static TextureAtlasSprite lanternSprite = null;

    public static final Color offlineColor = new Color(0.25f, 0.5f,0.5f, 1f);
    public static final Color onlineColor = new Color(0f, 1f,1f, 1f);

    public RendererRepulsor(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(BlockEntityRepulsor repulsor, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        bufferSource.getBuffer(RenderType.solid());

        float animProgress = (repulsor.pulseTicksLeft - partialTick) / BlockEntityRepulsor.pulseTicks;

        if(animProgress < 0)
        {
            animProgress = 0;
        }
        /*if(lanternSprite == null)
        {
            //lanternSprite = Minecraft.getInstance().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/sea_lantern");
        }*/

        ResourceLocation lanternLocation = new ModelResourceLocation("minecraft:sea_lantern#");

        VertexConsumer vc = bufferSource.getBuffer(RenderType.solid());

        SimpleBakedModel model = (SimpleBakedModel) Minecraft.getInstance().getModelManager().getModel(lanternLocation);

        Color finalColor = Color.lerp(offlineColor, onlineColor, animProgress);

        List<Direction> dirs = Lists.newArrayList(Direction.values());

        dirs.add(null);

        float gap = 2f / 16f;
        float scale = 1f - gap * 2f;

        poseStack.translate(gap, gap, gap);
        poseStack.scale(scale, scale, scale);

        for(Direction dir : dirs) {
            for(BakedQuad quad : model.getQuads(null, dir, null)) {
                vc.putBulkData(poseStack.last(), quad, finalColor.r, finalColor.g, finalColor.b, 511, packedOverlay);
            }
        }
    }
}
