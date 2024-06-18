package com.LubieKakao1212.opencu.fabric.event;

import com.LubieKakao1212.opencu.registry.fabric.CUItems;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public class UseHandler {

    public static void init() {
        UseBlockCallback.EVENT.register(UseHandler::handleOcuTool);
    }

    public static ActionResult handleOcuTool(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        var stack = player.getMainHandStack();

        if(stack.getItem() == CUItems.AIM_TOOL ||
                stack.getItem() == CUItems.LINK_TOOL) {
            var iuc = new ItemUsageContext(player, hand, hitResult);
            return stack.useOnBlock(iuc);
        }
        return ActionResult.PASS;
    }

}
