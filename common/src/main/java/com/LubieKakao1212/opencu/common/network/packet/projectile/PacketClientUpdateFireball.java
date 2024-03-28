package com.LubieKakao1212.opencu.common.network.packet.projectile;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.world.World;

public record PacketClientUpdateFireball(int entityId, float powX, float powY, float powZ) {

}
