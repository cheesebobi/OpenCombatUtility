package com.lubiekakao1212.opencu;

import com.lubiekakao1212.opencu.common.dispenser.IDispenser;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlatformUtil {

    @ExpectPlatform
    public static IDispenser getDispenser(ItemStack stack) { return null; }

    public static class Network {

        @ExpectPlatform
        public static <T> void sendToAllTracking(T packet, World world, BlockPos pos) {

        }

        @ExpectPlatform
        public static <T> void sendToServer(T packet) {

        }

        @ExpectPlatform
        public static <T> void sendToPlayer(T packet, ServerPlayerEntity player) {

        }

        @ExpectPlatform
        public static <T> void enqueueEntityUpdate(T message, Entity target, int delay) {

        }
    }

}
