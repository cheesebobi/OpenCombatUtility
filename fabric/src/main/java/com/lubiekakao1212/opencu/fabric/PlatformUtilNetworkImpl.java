package com.lubiekakao1212.opencu.fabric;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlatformUtilNetworkImpl {
    public static <T> void sendToAllTracking(T packet, World world, BlockPos pos) {
    }

    public static <T> void sendToServer(T packet) {
    }

    public static <T> void sendToPlayer(T packet, ServerPlayerEntity player) {
    }

    public static <T> void enqueueEntityUpdate(T message, Entity target, int delay) {
    }
}
