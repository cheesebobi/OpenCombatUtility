package com.LubieKakao1212.opencu.common.block.entity.renderer;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.lubiekakao1212.qulib.math.Aim;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.joml.Quaterniond;
import org.joml.Quaternionf;

public class RendererModularFrame implements BlockEntityRenderer<BlockEntityModularFrame> {

    private static final Quaternionf y180;
    private static final Quaterniond miry;

    static {
        y180 = new Quaternionf().fromAxisAngleRad(0, 1.f, 0, (float)Math.PI);
        miry = new Quaterniond().set(1, -1, 1, -1);
    }

    public RendererModularFrame(BlockEntityRendererFactory.Context ctx) {

    }

    @Override
    public void render(BlockEntityModularFrame blockEntity, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight, int packedOverlay) {
        //super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        ItemStack displayStack = blockEntity.getCurrentDeviceItem();
        if(displayStack != null && !displayStack.isEmpty()) {

            float step = partialTick;
            if(blockEntity.clientAge == blockEntity.clientPrevFrameAge) {
                step -= blockEntity.clientPrevFramePartialTick;
            }
            blockEntity.clientPrevFramePartialTick = partialTick;
            blockEntity.clientPrevFrameAge = blockEntity.clientAge;

            Aim current = blockEntity.getCurrentAim();
            Aim last = blockEntity.getLastAim();

            Aim partial = new Aim(0, 0).set(last);

            if(!blockEntity.isAligned()) {
                //Quaterniond partial = last.slerp(current, partialTick, new Quaterniond());
                partial.stepPerAxis(current, blockEntity.deltaAnglePitch * step, blockEntity.deltaAngleYaw * step, partial);
                blockEntity.setLastAim(new Aim(0,0).set(partial));
            }

            /*partial.y = -partial.y;
            partial.w = -partial.w;*/

            poseStack.push();
            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.multiply(y180);
            poseStack.multiply(new Quaternionf().set(partial.toQuaternion()));
            MinecraftClient.getInstance().getItemRenderer().renderItem(displayStack, ModelTransformationMode.FIXED, packedLight, packedOverlay, poseStack, bufferSource, blockEntity.getWorld(), 0);
            poseStack.pop();
        }
    }

}
