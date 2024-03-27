package com.lubiekakao1212.opencu.forge.proxy;

import net.minecraft.client.MinecraftClient;
import net.minecraft.world.World;

public class ClientProxy extends Proxy {

    @Override
    public World getLevelImpl() {
        return MinecraftClient.getInstance().world;
    }
}
