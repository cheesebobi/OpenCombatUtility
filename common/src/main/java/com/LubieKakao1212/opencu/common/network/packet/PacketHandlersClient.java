package com.LubieKakao1212.opencu.common.network.packet;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityRepulsor;
import com.LubieKakao1212.opencu.common.network.packet.dispenser.PacketClientUpdateDispenser;
import com.LubieKakao1212.opencu.common.network.packet.dispenser.PacketClientUpdateDispenserAim;
import com.LubieKakao1212.opencu.common.network.packet.projectile.PacketClientUpdateFireball;
import com.lubiekakao1212.qulib.math.Aim;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.world.World;

public class PacketHandlersClient {

    public static void handle(PacketClientUpdateFireball packetIn) {
        var player = MinecraftClient.getInstance().player;
        assert player != null;
        World level =  player.world;

        Entity entity = level.getEntityById(packetIn.entityId());

        if(entity instanceof AbstractFireballEntity) {
            AbstractFireballEntity fireball = ((AbstractFireballEntity) entity);
            fireball.powerX = packetIn.powX();
            fireball.powerY = packetIn.powY();
            fireball.powerZ = packetIn.powZ();
        }
    }

    public static void handle(PacketClientUpdateDispenser packet) {
        var player = MinecraftClient.getInstance().player;
        assert player != null;
        World level =  player.world;

        BlockEntity te = level.getBlockEntity(packet.position());

        if(te instanceof BlockEntityModularFrame) {
            ((BlockEntityModularFrame) te).setCurrentDeviceItem(packet.newDispenser());
        }
    }

    public static void handle(PacketClientUpdateDispenserAim packet) {
        var player = MinecraftClient.getInstance().player;
        assert player != null;
        World world = player.getWorld();

        BlockEntity te = world.getBlockEntity(packet.position());

        var aim = new Aim(packet.pitch(), packet.yaw());

        if (te instanceof BlockEntityModularFrame) {
            ((BlockEntityModularFrame) te).setCurrentAim(aim);
            if(packet.hard()) {
                //Sets last aim to aim
                ((BlockEntityModularFrame) te).setCurrentAim(aim);
            }
        }
    }

    public static void handle(PacketClientRepulsorPulse packet) {
        var world = MinecraftClient.getInstance().world;
        assert world != null;

        var be = world.getBlockEntity(packet.position());
        if(!(be instanceof BlockEntityRepulsor)) {
            OpenCUModCommon.LOGGER.warn("No repulsor found at: " + packet.position());
            return;
        }
        ((BlockEntityRepulsor) be).setPulseTimer();
    }

}
