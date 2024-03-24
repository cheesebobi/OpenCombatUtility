package com.LubieKakao1212.opencu.common.network.packet.projectile;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.world.World;

public record PacketClientUpdateFireball(int entityId, float powX, float powY, float powZ) {

    public static void execute(PacketClientUpdateFireball packetIn) {
        var player = MinecraftClient.getInstance().player;
        assert player != null;
        World level =  player.world;

        Entity entity = level.getEntityById(packetIn.entityId);

        if(entity instanceof AbstractFireballEntity) {
            AbstractFireballEntity fireball = ((AbstractFireballEntity) entity);
            fireball.powerX = packetIn.powX;
            fireball.powerY = packetIn.powY;
            fireball.powerZ = packetIn.powZ;
        }
    }

}
