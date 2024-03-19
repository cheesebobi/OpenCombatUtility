package com.LubieKakao1212.opencu.block.entity.renderer;

import com.LubieKakao1212.opencu.block.entity.BlockEntityOmniDispenser;
import com.lubiekakao1212.qulib.math.extensions.QuaterniondExtensionsKt;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.joml.Quaterniond;
import org.joml.Quaternionf;

public class RendererOmniDispenser implements BlockEntityRenderer<BlockEntityOmniDispenser> {

    private static final Quaternionf y180;
    private static final Quaterniond miry;

    static {
        y180 = new Quaternionf().fromAxisAngleRad(0, 1.f, 0, (float)Math.PI);
        miry = new Quaterniond().set(1, -1, 1, -1);
    }

    public RendererOmniDispenser(BlockEntityRendererFactory.Context ctx) {

    }

    @Override
    public void render(BlockEntityOmniDispenser dispenser, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight, int packedOverlay) {
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
                partial = QuaterniondExtensionsKt.step(last, current, dispenser.deltaAngle * step, new Quaterniond());
                dispenser.setLastAction(partial);
            }

            partial.y = -partial.y;
            partial.w = -partial.w;

            poseStack.push();
            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.multiply(y180);
            poseStack.multiply(new Quaternionf().set(partial));
            MinecraftClient.getInstance().getItemRenderer().renderItem(displayStack, ModelTransformationMode.FIXED, packedLight, packedOverlay, poseStack, bufferSource, dispenser.getWorld(), 0);
            poseStack.pop();
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
