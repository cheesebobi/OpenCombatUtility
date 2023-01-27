package com.LubieKakao1212.opencu.proxy;

import com.LubieKakao1212.opencu.OpenCUMod;
import net.minecraft.world.level.Level;

public class Proxy {

    protected Level getLevelImpl() {
        return null;
    }

    public static Level getLevel() {
        return OpenCUMod.PROXY.getLevelImpl();
    }


}