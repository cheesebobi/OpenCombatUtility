package com.LubieKakao1212.opencu.common.network.packet;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

public record PacketClientPlayerScaleVelocity(float scale) {

    public static void execute(PacketClientPlayerScaleVelocity packetIn) {
        var scale = packetIn.scale;
        PlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;
        player.setVelocity(player.getVelocity().multiply(scale, scale, scale));

        if(player.getVelocity().y > 0) {
            player.fallDistance = 0;
        }
    }


}
