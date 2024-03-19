package com.LubieKakao1212.opencu.network.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.DistExecutor;

public class ClientPlayerScaleVelocity implements DistExecutor.SafeRunnable {

    private double scale;

    public ClientPlayerScaleVelocity(double scale) {
        this.scale = scale;
    }


    @Override
    public void run() {
        PlayerEntity player = MinecraftClient.getInstance().player;
        player.setVelocity(player.getVelocity().multiply(scale, scale, scale));

        if(player.getVelocity().y > 0){
            player.fallDistance = 0;
        }
    }
}
