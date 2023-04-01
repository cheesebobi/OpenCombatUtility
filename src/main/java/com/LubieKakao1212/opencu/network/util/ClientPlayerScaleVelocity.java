package com.LubieKakao1212.opencu.network.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.DistExecutor;

public class ClientPlayerScaleVelocity implements DistExecutor.SafeRunnable {

    private double scale;

    public ClientPlayerScaleVelocity(double scale) {
        this.scale = scale;
    }


    @Override
    public void run() {
        Player player = Minecraft.getInstance().player;
        player.setDeltaMovement(player.getDeltaMovement().multiply(scale, scale, scale));

        if(player.getDeltaMovement().y > 0){
            player.fallDistance = 0;
        }
    }
}
