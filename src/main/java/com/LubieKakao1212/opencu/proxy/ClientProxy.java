package com.LubieKakao1212.opencu.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;

public class ClientProxy extends Proxy {

    @Override
    public Level getLevelImpl() {
        return Minecraft.getInstance().level;
    }
}
