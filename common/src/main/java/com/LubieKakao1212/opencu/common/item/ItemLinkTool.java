package com.LubieKakao1212.opencu.common.item;

import com.LubieKakao1212.opencu.common.device.event.DistributingWorldEventNode;
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
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemLinkTool extends Item {

    public ItemLinkTool(Settings settings) {
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
            bind(tag, world, pos);
            return ActionResult.SUCCESS;
        }

        if(tag.contains("ocu:boundPos", NbtElement.COMPOUND_TYPE)) {
            var boundPos = NbtHelper.toBlockPos(tag.getCompound("ocu:boundPos"));
            var be = CUBlockEntities.modularFrame().get(world, boundPos);
            if(be == null) {
                return ActionResult.FAIL;
            }

            var result = be.getEventDistributor().toggleTarget(pos);

            player.sendMessage(result.description(pos));

            if(result.isSuccess) {
                return ActionResult.SUCCESS;
            }
            else {
                return ActionResult.FAIL;
            }
        }

        return ActionResult.PASS;
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
