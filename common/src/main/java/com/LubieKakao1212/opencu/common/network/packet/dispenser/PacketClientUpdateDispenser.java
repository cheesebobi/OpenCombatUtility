package com.LubieKakao1212.opencu.common.network.packet.dispenser;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public record PacketClientUpdateDispenser(BlockPos position, ItemStack newDispenser) {
}
