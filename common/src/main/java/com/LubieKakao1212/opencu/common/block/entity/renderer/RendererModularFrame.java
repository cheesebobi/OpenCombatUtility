package com.lubiekakao1212.opencu.common.block.entity.renderer;

import com.lubiekakao1212.opencu.common.block.entity.BlockEntityModularFrame;
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

        ItemStack displayStack = blockEntity.getCurrentDispenserItem();
        if(displayStack != null && !displayStack.isEmpty()) {

            float step = partialTick;
            if(blockEntity.clientAge == blockEntity.clientPrevFrameAge) {
                step -= blockEntity.clientPrevFramePartialTick;
            }
            blockEntity.clientPrevFramePartialTick = partialTick;
            blockEntity.clientPrevFrameAge = blockEntity.clientAge;

            Quaterniond current = blockEntity.getCurrentAction().aim();
            Quaterniond last = blockEntity.getLastAction().aim();

            Quaterniond partial = current;

            if(blockEntity.deltaAngle > 0) {
                //Quaterniond partial = last.slerp(current, partialTick, new Quaterniond());
                partial = QuaterniondExtensionsKt.step(last, current, blockEntity.deltaAngle * step, new Quaterniond());
                blockEntity.setLastAction(partial);
            }

            partial.y = -partial.y;
            partial.w = -partial.w;

            poseStack.push();
            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.multiply(y180);
            poseStack.multiply(new Quaternionf().set(partial));
            MinecraftClient.getInstance().getItemRenderer().renderItem(displayStack, ModelTransformationMode.FIXED, packedLight, packedOverlay, poseStack, bufferSource, blockEntity.getWorld(), 0);
            poseStack.pop();
        }
    }

}
