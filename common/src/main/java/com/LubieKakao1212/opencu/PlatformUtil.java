package com.LubieKakao1212.opencu;

import com.LubieKakao1212.opencu.common.dispenser.IDispenser;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlatformUtil {

    @ExpectPlatform
    public static IDispenser getDispenser(ItemStack stack) { return null; }

}
