package com.LubieKakao1212.opencu.block.entity.renderer;

import com.LubieKakao1212.opencu.block.entity.BlockEntityOmniDispenser;
import com.LubieKakao1212.qulib.util.joml.QuaterniondUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import com.LubieKakao1212.qulib.libs.joml.Quaterniond;

public class RendererOmniDispenser implements BlockEntityRenderer<BlockEntityOmniDispenser> {

    private static final Quaternion y180;
    private static final Quaterniond miry;

    static {
        Quaterniond quat = new Quaterniond().fromAxisAngleRad(0, 1.f, 0, Math.PI);
        y180 = QuaterniondUtil.toMojang(quat);

        miry = quat.set(1, -1, 1, -1);
    }

    public RendererOmniDispenser(BlockEntityRendererProvider.Context ctx) {

    }

    @Override
    public void render(BlockEntityOmniDispenser dispenser, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        //super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        ItemStack displayStack = dispenser.getCurrentDispenserItem();
        if(displayStack != null && !displayStack.isEmpty()) {
            poseStack.pushPose();
            Quaterniond last = dispenser.getLastAction().aim();
            Quaterniond current = dispenser.getCurrentAction().aim();

            Quaterniond partial = current.slerp(last, partialTick, new Quaterniond());

            partial.y = -partial.y;
            partial.w = -partial.w;

            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.mulPose(y180);
            poseStack.mulPose(QuaterniondUtil.toMojang(partial));
            Minecraft.getInstance().getItemRenderer().renderStatic(displayStack, ItemTransforms.TransformType.FIXED, packedLight, packedOverlay, poseStack, bufferSource, 0);
            poseStack.popPose();
        }
    }
}
