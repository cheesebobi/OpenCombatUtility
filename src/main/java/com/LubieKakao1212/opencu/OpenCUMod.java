package com.LubieKakao1212.opencu;

import com.LubieKakao1212.opencu.dependencies.cc.init.CCInit;
import com.LubieKakao1212.opencu.init.CURegister;
import com.LubieKakao1212.opencu.network.NetworkHandler;
import com.LubieKakao1212.opencu.proxy.ClientProxy;
import com.LubieKakao1212.opencu.proxy.Proxy;
import com.LubieKakao1212.opencu.proxy.ServerProxy;
import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

//TODO Add config gui
@Mod(OpenCUMod.MODID)
public class OpenCUMod
{
    public static final String MODID = "opencu";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final Proxy PROXY = DistExecutor.unsafeRunForDist(() -> ()-> new ClientProxy(), () -> () -> new ServerProxy());

    public OpenCUMod() {

        NetworkHandler.init();

        CURegister.init();

        //For when ComputerCraft will be optional
        if(ModList.get().isLoaded("computercraft")) {
            CCInit.init();
        }
    }
}
