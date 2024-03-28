package com.LubieKakao1212.opencu.common.network.packet.dispenser;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.common.compat.valkyrienskies.VS2SoftUtil;
import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.joml.Vector3d;

public record PacketServerRequestDispenserUpdate(BlockPos position) {

}
