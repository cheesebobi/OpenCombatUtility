package com.LubieKakao1212.opencu.common.item;

import com.LubieKakao1212.opencu.registry.CUBlockEntities;
import com.lubiekakao1212.qulib.math.mc.Vector3m;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import java.util.List;

public class ItemAimTool extends ItemBindable {

    private static final double maxDistance = 16;
    private static final double maxDistanceSq = maxDistance * maxDistance;

    public ItemAimTool(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        var nbt = stack.getOrCreateNbt();

        if(nbt.contains("ocu:boundPos", NbtElement.COMPOUND_TYPE)) {
            var boundPos = NbtHelper.toBlockPos(nbt.getCompound("ocu:boundPos"));
            tooltip.add(Text.translatable("info.opencu.tool.pos", boundPos.toShortString()));
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var stack = context.getStack();
        var pos = context.getBlockPos();
        var world = context.getWorld();
        var player = context.getPlayer();

        if(world.isClient) {
            return ActionResult.SUCCESS;
        }

        var tag = stack.getOrCreateNbt();
        if(player.isSneaking()) {
            bind(tag, world, player, pos);
            return ActionResult.SUCCESS;
        }

        if(isBound(stack)) {

            if(world.getDimensionKey().toString().equals(tag.getString("ocu:boundWorld"))) {
                player.sendMessage(Text.translatable("message.opencu.aim.fail.interdimensional", pos.toShortString()));
            }

            var boundPos = NbtHelper.toBlockPos(tag.getCompound("ocu:boundPos"));
            var dstSq = pos.getSquaredDistance(boundPos);

            if(boundPos.equals(pos) || dstSq > maxDistanceSq) {
                return ActionResult.FAIL;
            }

            var be = CUBlockEntities.modularFrame().get(world, boundPos);

            if(be != null) {
                be.aimAt(new Vector3m(pos.subtract(boundPos)));
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.CONSUME;
    }

    @Override
    protected boolean shouldBind(ItemStack stack, World world, PlayerEntity player, BlockPos targetPos) {
        var be = CUBlockEntities.modularFrame().get(world, targetPos);
        return be != null;
    }
}
