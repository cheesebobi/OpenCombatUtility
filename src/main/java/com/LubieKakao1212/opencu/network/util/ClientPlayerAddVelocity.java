package com.LubieKakao1212.opencu.network.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
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
        Player player = Minecraft.getInstance().player;
        player.setDeltaMovement(player.getDeltaMovement().add(x, y, z));

        if(player.getDeltaMovement().y > 0){
            player.fallDistance = 0;
        }
    }
}
