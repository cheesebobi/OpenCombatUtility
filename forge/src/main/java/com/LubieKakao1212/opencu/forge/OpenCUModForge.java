package com.LubieKakao1212.opencu.forge;

//import com.LubieKakao1212.opencu.config.OpenCUConfigForge;
import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.forge.compat.CCInit;
import com.LubieKakao1212.opencu.forge.proxy.ClientProxy;
import com.LubieKakao1212.opencu.forge.proxy.Proxy;
import com.LubieKakao1212.opencu.forge.proxy.ServerProxy;
import com.LubieKakao1212.opencu.registry.forge.CURegister;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

//TODO Add config gui
@Mod(OpenCUModCommon.MODID)
public class OpenCUModForge
{
    public static final Proxy PROXY = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    private static boolean hasValkyrienSkies;

    public OpenCUModForge() {

        NetworkUtilImpl.init();

        CURegister.init();

        //ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, OpenCUConfigForge.SPEC, "opencu-common.toml");

        //For when ComputerCraft will be optional
        if(ModList.get().isLoaded("computercraft")) {
            CCInit.init();
        }

        hasValkyrienSkies = ModList.get().isLoaded("valkyrienskies");
    }

    public static boolean hasValkyrienSkies() {
        return hasValkyrienSkies;
    }

}
