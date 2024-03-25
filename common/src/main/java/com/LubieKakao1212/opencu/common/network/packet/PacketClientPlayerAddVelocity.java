package com.lubiekakao1212.opencu.common.network.packet;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

public record PacketClientPlayerAddVelocity(int x, int y, int z) {
    public static final double precision = 64000.0D;

    public static PacketClientPlayerAddVelocity create(double motionXIn, double motionYIn, double motionZIn) {
        double d0 = 3.9D;

        if (motionXIn < -d0)
        {
            motionXIn = -d0;
        }

        if (motionYIn < -d0)
        {
            motionYIn = -d0;
        }

        if (motionZIn < -d0)
        {
            motionZIn = -d0;
        }

        if (motionXIn > d0)
        {
            motionXIn = d0;
        }

        if (motionYIn > d0)
        {
            motionYIn = d0;
        }

        if (motionZIn > d0)
        {
            motionZIn = d0;
        }

        return new PacketClientPlayerAddVelocity(
                (int)(motionXIn * precision),
                (int)(motionYIn * precision),
                (int)(motionZIn * precision));
    }

    public static void execute(PacketClientPlayerAddVelocity packetIn) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;
        player.setVelocity(player.getVelocity().add(packetIn.x / precision, packetIn.y / precision, packetIn.z / precision));

        if(player.getVelocity().y > 0){
            player.fallDistance = 0;
        }
    }
}
