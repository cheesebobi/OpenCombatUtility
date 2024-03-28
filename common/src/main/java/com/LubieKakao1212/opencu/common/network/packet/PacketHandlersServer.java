package com.LubieKakao1212.opencu.common.network.packet;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.common.compat.valkyrienskies.VS2SoftUtil;
import com.LubieKakao1212.opencu.common.network.packet.dispenser.PacketServerRequestDispenserUpdate;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.joml.Vector3d;

public class PacketHandlersServer {

    public static void handle(PacketServerRequestDispenserUpdate packetIn, ServerPlayerEntity sender) {
        World level = sender.getWorld();

        var position = packetIn.position();

        if(VS2SoftUtil.getDistanceSqr(level, new Vector3d(sender.getX(), sender.getY(), sender.getY()), new Vector3d(position.getX(), position.getY(), position.getZ())) < (64 * 64)) {
            BlockEntity be = level.getBlockEntity(position);

            if(be instanceof BlockEntityModularFrame) {
                ((BlockEntityModularFrame) be).sendDispenserUpdateTo(sender);
            }
        }else
        {
            OpenCUModCommon.LOGGER.warn("Potentially malicious packet received, skipping");
        }
    }

}
