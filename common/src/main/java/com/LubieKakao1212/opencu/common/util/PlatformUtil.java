package com.LubieKakao1212.opencu.common.util;

import com.LubieKakao1212.opencu.common.dispenser.IDispenser;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlatformUtil {

    @ExpectPlatform
    public static IDispenser getDispenser(ItemStack stack) { return null; }

    public static class Network {

        @ExpectPlatform
        public static <T> void sendToAllTracking(T packet, World world, BlockPos pos) {

        }


    }

}
