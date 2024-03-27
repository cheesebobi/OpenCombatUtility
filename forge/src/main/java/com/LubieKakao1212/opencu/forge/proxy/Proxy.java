package com.lubiekakao1212.opencu.forge.proxy;

import com.lubiekakao1212.opencu.OpenCUModForge;
import net.minecraft.world.World;

public class Proxy {

    protected World getLevelImpl() {
        return null;
    }

    public static World getLevel() {
        return OpenCUModForge.PROXY.getLevelImpl();
    }


}