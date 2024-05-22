package com.LubieKakao1212.opencu.common.item;

import com.LubieKakao1212.opencu.registry.CUBlockEntities;
import com.lubiekakao1212.qulib.math.mc.Vector3m;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.joml.Vector3d;

public class ItemAimTool extends Item {

    private static final double maxDistance = 16;
    private static final double maxDistanceSq = maxDistance * maxDistance;

    public ItemAimTool(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var stack = context.getStack();
        var pos = context.getBlockPos();
        var world = context.getWorld();
        var player = context.getPlayer();

        if(world.isClient) {
            return ActionResult.FAIL;
        }

        var tag = stack.getOrCreateNbt();

        if(player.isSneaking()) {
            bind(tag, world, pos);
            return ActionResult.SUCCESS;
        }

        if(tag.contains("ocu:boundPos", NbtElement.COMPOUND_TYPE)) {
            var boundPos = NbtHelper.toBlockPos(tag.getCompound("ocu:boundPos"));
            var dstSq = pos.getSquaredDistance(boundPos);

            if(boundPos.equals(pos) || dstSq > maxDistanceSq) {
                return ActionResult.FAIL;
            }

            var be = CUBlockEntities.modularFrame().get(world, boundPos);

            if(be != null) {
                be.aimAt(new Vector3m(pos.subtract(boundPos)));
            }
        }

        return ActionResult.CONSUME;
    }

    private void bind(NbtCompound data, World world, BlockPos targetPos) {
        var be = CUBlockEntities.modularFrame().get(world, targetPos);

        if(be != null) {
            data.put("ocu:boundPos", NbtHelper.fromBlockPos(targetPos));
        } else {
            unbind(data);
        }
    }

    private void unbind(NbtCompound data) {
        data.remove("ocu:boundPos");
    }

}
