package com.LubieKakao1212.opencu.network;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.network.packet.EntityAddVelocityPacket;
import com.LubieKakao1212.opencu.network.packet.dispenser.UpdateDispenserAimPacket;
import com.LubieKakao1212.opencu.network.packet.dispenser.UpdateDispenserPacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {

    private static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(OpenCUMod.MODID);

    public static void init() {
        int id = 0;
        INSTANCE.registerMessage(EntityAddVelocityPacket.Handler.class, EntityAddVelocityPacket.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(UpdateDispenserPacket.Handler.class, UpdateDispenserPacket.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(UpdateDispenserAimPacket.Handler.class, UpdateDispenserAimPacket.class, id++, Side.CLIENT);
    }

    public static void sendTo(EntityPlayerMP player, IMessage message) {
        INSTANCE.sendTo(message, player);
    }

    public static void sendToALl(IMessage message) {
        INSTANCE.sendToAll(message);
    }

    public static void sendToAllTracking(IMessage message, BlockPos pos, int dimension) {
        INSTANCE.sendToAllTracking(message, new NetworkRegistry.TargetPoint(dimension, pos.getX(), pos.getY(), pos.getZ(), -1));
    }



}
