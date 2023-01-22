package com.LubieKakao1212.opencu.block.entity.renderer;

import com.LubieKakao1212.opencu.block.entity.BlockEntityRepulsor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;

public class RendererRepulsor implements BlockEntityRenderer<BlockEntityRepulsor> {

    private static final AABB coreBounds = new AABB(0,0,0,0,0,0).inflate(0.825 / 2).move(0.5, 0.5, 0.5);
    private static TextureAtlasSprite lanternSprite = null;

    public static final Color offlineColor = new Color(0.25f, 0.5f,0.5f, 1f);
    public static final Color onlineColor = new Color(0f, 1f,1f, 1f);

    public RendererRepulsor()
    {
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

        ResourceLocation lanternLocation = new ResourceLocation("opencu:block/repulsor_lantern");

        VertexConsumer vc = bufferSource.getBuffer(RenderType.solid());

        SimpleBakedModel model = (SimpleBakedModel) Minecraft.getInstance().getModelManager().getModel(lanternLocation);

        Color finalColor = Color.lerp(onlineColor, offlineColor, animProgress);

        for(BakedQuad quad : model.getQuads(null, null, null)) {
            vc.putBulkData(poseStack.last(), quad, finalColor.r, finalColor.g, finalColor.b, -1 << 32, packedOverlay);
        }

    }
}
