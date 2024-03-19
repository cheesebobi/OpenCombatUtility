package com.LubieKakao1212.opencu.network.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.DistExecutor;

public class ClientPlayerAddVelocity implements DistExecutor.SafeRunnable {

    private double x, y, z;

    public ClientPlayerAddVelocity(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    @Override
    public void run() {
        PlayerEntity player = MinecraftClient.getInstance().player;
        player.setVelocity(player.getVelocity().add(x, y, z));

        if(player.getVelocity().y > 0){
            player.fallDistance = 0;
        }
    }
}
