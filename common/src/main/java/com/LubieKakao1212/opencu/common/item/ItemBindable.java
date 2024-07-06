package com.LubieKakao1212.opencu.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class ItemBindable extends Item {
    public ItemBindable(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        var stack = user.getStackInHand(hand);
        if(!user.isSneaking() || ! isBound(stack)) {
            return TypedActionResult.pass(stack);
        }

        if(!world.isClient) {
            var tag = stack.getOrCreateNbt();
            unbind(tag, world, user, null);
            stack.setNbt(tag);
            user.setCurrentHand(hand);
        }
        return TypedActionResult.success(stack);
    }

    protected void tryBind(ItemStack stack, World world, PlayerEntity player, BlockPos targetPos) {
        var data = stack.getOrCreateNbt();

        if(shouldBind(stack, world, player, targetPos)) {
            bind(data, world, player, targetPos);
        }
        else if(isBound(stack)) {
            unbind(data, world, player, targetPos);
        }
    }

    protected void unbind(NbtCompound data, World world, PlayerEntity player, BlockPos targetPos) {
        data.remove("ocu:boundPos");
        data.remove("ocu:boundWorld");
        player.sendMessage(Text.translatable("message.opencu.bind.unbind"));
    }

    protected void bind(NbtCompound data, World world, PlayerEntity player, BlockPos targetPos) {
        data.put("ocu:boundPos", NbtHelper.fromBlockPos(targetPos));
        data.putString("ocu:boundWorld", world.getDimensionKey().getValue().toString());
        player.sendMessage(Text.translatable("message.opencu.bind.bind", targetPos.toShortString()));
    }

    protected abstract boolean shouldBind(ItemStack stack, World world, PlayerEntity player, BlockPos targetPos);

    protected boolean isBound(ItemStack stack) {
        var data = stack.getOrCreateNbt();
        return data.contains("ocu:boundPos", NbtElement.COMPOUND_TYPE) &&
                data.contains("ocu:boundWorld", NbtElement.STRING_TYPE);
    }
}
