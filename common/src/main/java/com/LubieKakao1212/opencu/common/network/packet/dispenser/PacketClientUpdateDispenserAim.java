package com.LubieKakao1212.opencu.common.network.packet.dispenser;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.lubiekakao1212.qulib.math.Aim;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.joml.Quaterniond;

public record PacketClientUpdateDispenserAim(boolean hard, BlockPos position, float pitch, float yaw) {

    public static PacketClientUpdateDispenserAim create(BlockPos pos, Aim aim, boolean hard) {
        return new PacketClientUpdateDispenserAim(
                hard,
                pos,
                (float)aim.getPitch(),
                (float)aim.getYaw()
        );
    }

}
