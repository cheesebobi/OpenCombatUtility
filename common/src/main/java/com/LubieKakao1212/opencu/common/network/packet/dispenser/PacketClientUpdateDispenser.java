package com.lubiekakao1212.opencu.common.network.packet.dispenser;

import com.lubiekakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public record PacketClientUpdateDispenser(BlockPos position, ItemStack newDispenser) {

    public static void execute(PacketClientUpdateDispenser packet) {
        var player = MinecraftClient.getInstance().player;
        assert player != null;
        World level =  player.world;

        BlockEntity te = level.getBlockEntity(packet.position);

        if(te instanceof BlockEntityModularFrame) {
            ((BlockEntityModularFrame) te).setCurrentDispenserItem(packet.newDispenser);
        }
    }
}
