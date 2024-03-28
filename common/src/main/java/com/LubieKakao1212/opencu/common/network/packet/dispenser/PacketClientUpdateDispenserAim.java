package com.LubieKakao1212.opencu.common.network.packet.dispenser;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.joml.Quaterniond;

public record PacketClientUpdateDispenserAim(boolean hard, BlockPos position, float qx, float qy, float qz, float qw) {

    public static PacketClientUpdateDispenserAim create(BlockPos pos, Quaterniond aim, boolean hard) {
        return new PacketClientUpdateDispenserAim(
                hard,
                pos,
                (float)aim.x,
                (float)aim.y,
                (float)aim.z,
                (float)aim.w
        );
    }

}
