package com.LubieKakao1212.opencu.block.tileentity.renderer;

import com.LubieKakao1212.opencu.block.tileentity.TileEntityOmniDispenser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import org.joml.Quaterniond;
import org.lwjgl.util.vector.Quaternion;

public class RendererOmniDispenser extends TileEntitySpecialRenderer<TileEntityOmniDispenser> {

    @Override
    public void render(TileEntityOmniDispenser te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        //super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        ItemStack displayStack = te.getCurrentDispenserItem();
        if(displayStack != null) {
            GlStateManager.pushMatrix();
                //te.getCurrentAction().aim().mix();
                Quaterniond last = te.getLastAction().aim();
                Quaterniond current = te.getCurrentAction().aim();

                Quaterniond partial = last.slerp(current, partialTicks);

                //partial = GlmMath.axisMirror(partial, EnumFacing.Axis.Y);

                GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
                //GlStateManager.scale(1., 1., 1.);
                GlStateManager.rotate(180, 0, 1.f, 0);
                GlStateManager.rotate(new Quaternion((float)partial.x(), (float)-partial.y(), (float)partial.z(), (float)-partial.w()));
                Minecraft.getMinecraft().getRenderItem().renderItem(displayStack, ItemCameraTransforms.TransformType.FIXED);
            GlStateManager.popMatrix();
        }
    }
}
