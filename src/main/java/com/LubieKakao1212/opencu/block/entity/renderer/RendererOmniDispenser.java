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
import org.joml.Quaterniond;

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

            float step = partialTick;
            if(dispenser.clientAge == dispenser.clientPrevFrameAge) {
                step -= dispenser.clientPrevFramePartialTick;
            }
            dispenser.clientPrevFramePartialTick = partialTick;
            dispenser.clientPrevFrameAge = dispenser.clientAge;

            Quaterniond current = dispenser.getCurrentAction().aim();
            Quaterniond last = dispenser.getLastAction().aim();

            Quaterniond partial = current;

            if(dispenser.deltaAngle > 0) {
                //Quaterniond partial = last.slerp(current, partialTick, new Quaterniond());
                partial = QuaterniondUtil.step(last, current, dispenser.deltaAngle * step);
                dispenser.setLastAction(partial);
            }

            partial.y = -partial.y;
            partial.w = -partial.w;

            poseStack.pushPose();
            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.mulPose(y180);
            poseStack.mulPose(QuaterniondUtil.toMojang(partial));
            Minecraft.getInstance().getItemRenderer().renderStatic(displayStack, ItemTransforms.TransformType.FIXED, packedLight, packedOverlay, poseStack, bufferSource, 0);
            poseStack.popPose();
        }
    }

    /**
     * Prevents jitter caused by lerps of same two Quaternons twice
     * @param dispenser
     * @param partialTick
     */
    private Quaterniond getLastPreventJitter(BlockEntityOmniDispenser dispenser, Quaterniond currentAim, float partialTick) {
        /*//Do we already have the next tick
        if(dispenser.clientPrevFramePartialTick > partialTick) {
            //Did we receive a change from the server
            if(dispenser.clientPrevFrameAim == currentAim) {
                dispenser.setCurrentAction(currentAim);
                dispenser.clientPrevFrameAim = currentAim;
                return currentAim;
            }
        }
        dispenser.clientPrevFramePartialTick = partialTick;*/

        return dispenser.getLastAction().aim();
    }

}
