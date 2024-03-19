package com.LubieKakao1212.opencu.proxy;

import com.LubieKakao1212.opencu.OpenCUMod;
import net.minecraft.world.World;

public class Proxy {

    protected World getLevelImpl() {
        return null;
    }

    public static World getLevel() {
        return OpenCUMod.PROXY.getLevelImpl();
    }


}