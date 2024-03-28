package com.LubieKakao1212.opencu.common.network.packet;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.common.network.packet.dispenser.PacketClientUpdateDispenser;
import com.LubieKakao1212.opencu.common.network.packet.dispenser.PacketClientUpdateDispenserAim;
import com.LubieKakao1212.opencu.common.network.packet.projectile.PacketClientUpdateFireball;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.world.World;
import org.joml.Quaterniond;

public class PacketHandlersClient {

    public static void handle(PacketClientPlayerAddVelocity packetIn) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;
        var precision = PacketClientPlayerAddVelocity.precision;
        player.setVelocity(player.getVelocity().add(packetIn.x() / precision, packetIn.y() / precision, packetIn.z() / precision));

        if(player.getVelocity().y > 0){
            player.fallDistance = 0;
        }
    }

    public static void handle(PacketClientPlayerScaleVelocity packetIn) {
        var scale = packetIn.scale();
        PlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;
        player.setVelocity(player.getVelocity().multiply(scale, scale, scale));

        if(player.getVelocity().y > 0) {
            player.fallDistance = 0;
        }
    }

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
            ((BlockEntityModularFrame) te).setCurrentDispenserItem(packet.newDispenser());
        }
    }

    public static void handle(PacketClientUpdateDispenserAim packet) {
        var player = MinecraftClient.getInstance().player;
        assert player != null;
        World world = player.getWorld();

        BlockEntity te = world.getBlockEntity(packet.position());

        var aim = new Quaterniond(packet.qx(), packet.qy(), packet.qz(), packet.qw());

        if (te instanceof BlockEntityModularFrame) {
            ((BlockEntityModularFrame) te).setCurrentAction(aim);
            if(packet.hard()) {
                //Sets last aim to aim
                ((BlockEntityModularFrame) te).setCurrentAction(aim);
            }
        }
    }
}
