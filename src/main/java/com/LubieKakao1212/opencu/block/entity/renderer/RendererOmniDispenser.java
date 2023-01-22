package com.LubieKakao1212.opencu.block.entity.renderer;

import com.LubieKakao1212.opencu.block.entity.BlockEntityOmniDispenser;
import com.LubieKakao1212.qulib.util.joml.QuaterniondUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaterniond;

public class RendererOmniDispenser implements BlockEntityRenderer<BlockEntityOmniDispenser> {

    private static final Quaternion y180;
    private static final Quaterniond miry;

    static {
        Quaterniond quat = new Quaterniond().fromAxisAngleRad(0, 1.f, 0, Math.PI);
        y180 = QuaterniondUtil.toMojang(quat);

        miry = quat.set(1, -1, 1, -1);
    }


    @Override
    public void render(BlockEntityOmniDispenser dispenser, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        //super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        ItemStack displayStack = dispenser.getCurrentDispenserItem();
        if(displayStack != null) {
            poseStack.pushPose();
            Quaterniond last = dispenser.getLastAction().aim();
            Quaterniond current = dispenser.getCurrentAction().aim();

            Quaterniond partial = last.slerp(current, partialTick).mul(miry);

            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.mulPose(y180);
            poseStack.mulPose(QuaterniondUtil.toMojang(partial));
            Minecraft.getInstance().getItemRenderer().renderStatic(displayStack, ItemTransforms.TransformType.FIXED, packedLight, packedOverlay, poseStack, bufferSource, 0);
            poseStack.popPose();
        }
    }
}
