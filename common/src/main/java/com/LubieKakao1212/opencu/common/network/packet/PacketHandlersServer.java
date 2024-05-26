package com.LubieKakao1212.opencu.common.network.packet;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.common.compat.valkyrienskies.VS2SoftUtil;
import com.LubieKakao1212.opencu.common.network.packet.dispenser.PacketServerRequestDispenserUpdate;
import com.LubieKakao1212.opencu.common.network.packet.dispenser.PacketServerToggleRequiresLock;
import com.lubiekakao1212.qulib.math.mc.Vector3m;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.joml.Vector3d;

public class PacketHandlersServer {

    public static void handle(PacketServerRequestDispenserUpdate packetIn, ServerPlayerEntity sender) {
        World world = sender.getWorld();

        var position = packetIn.position();

        if(VS2SoftUtil.getDistanceSqr(world, new Vector3d(sender.getX(), sender.getY(), sender.getY()), new Vector3d(position.getX(), position.getY(), position.getZ())) < (64 * 64)) {
            BlockEntity be = world.getBlockEntity(position);

            if(be instanceof BlockEntityModularFrame) {
                ((BlockEntityModularFrame) be).sendDispenserUpdateTo(sender);
            }
        }
        else {
            OpenCUModCommon.LOGGER.warn("Potentially malicious packet received, skipping");
        }
    }

    public static void handle(PacketServerToggleRequiresLock packetIn, ServerPlayerEntity sender) {
        var world = sender.getWorld();

        var position = packetIn.position();

        if(VS2SoftUtil.getDistanceSqr(world, new Vector3m(sender.getPos()), new Vector3m(position)) < (64 * 64)) {
            BlockEntity be = world.getBlockEntity(position);

            if(be instanceof BlockEntityModularFrame frame) {
                frame.setRequiresLock(!frame.isRequiresLock());
            }
        }
        else {
            OpenCUModCommon.LOGGER.warn("Potentially malicious packet received, skipping");
        }

    }

}
