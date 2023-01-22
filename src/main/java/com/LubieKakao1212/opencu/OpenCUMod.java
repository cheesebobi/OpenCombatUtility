package com.LubieKakao1212.opencu;

import com.LubieKakao1212.opencu.init.CURegister;
import com.LubieKakao1212.opencu.network.NetworkHandler;
import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

//TODO Add config gui
@Mod(OpenCUMod.MODID)
public class OpenCUMod
{
    public static final String MODID = "opencu";

    public static final Logger LOGGER = LogUtils.getLogger();

    public OpenCUMod() {

        NetworkHandler.init();

        CURegister.init();
    }
}
