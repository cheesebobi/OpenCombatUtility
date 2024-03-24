package com.LubieKakao1212.opencu.common.block.entity.renderer;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityRepulsor;
import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BasicBakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

import java.util.List;

public class RendererRepulsor implements BlockEntityRenderer<BlockEntityRepulsor> {

    private static final Box coreBounds = new Box(0,0,0,0,0,0).expand(0.825 / 2).offset(0.5, 0.5, 0.5);
    private static Sprite lanternSprite = null;

    public static final Color offlineColor = new Color(0.25f, 0.5f,0.5f, 1f);
    public static final Color onlineColor = new Color(0f, 1f,1f, 1f);

    public RendererRepulsor(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(BlockEntityRepulsor repulsor, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight, int packedOverlay) {
        bufferSource.getBuffer(RenderLayer.getSolid());

        float animProgress = (repulsor.pulseTicksLeft - partialTick) / BlockEntityRepulsor.pulseTicks;

        if(animProgress < 0)
        {
            animProgress = 0;
        }

        var lanternLocation = new ModelIdentifier("minecraft","sea_lantern", "");

        VertexConsumer vc = bufferSource.getBuffer(RenderLayer.getSolid());

        BasicBakedModel model = (BasicBakedModel) MinecraftClient.getInstance().getBakedModelManager().getModel(lanternLocation);

        Color finalColor = Color.lerp(offlineColor, onlineColor, animProgress);

        List<Direction> dirs = Lists.newArrayList(Direction.values());

        dirs.add(null);

        float gap = 1f / 16f;
        float scale = 1f - gap * 2f;

        poseStack.translate(gap, gap, gap);
        poseStack.scale(scale, scale, scale);

        for(Direction dir : dirs) {
            for(BakedQuad quad : model.getQuads(null, dir, null)) {
                vc.quad(poseStack.peek(), quad, finalColor.r, finalColor.g, finalColor.b, 511, packedOverlay);
            }
        }
    }
}
