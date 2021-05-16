package com.LubieKakao1212.opencu;

import com.LubieKakao1212.opencu.proxy.CommonProxy;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = OpenCUMod.MODID, name = OpenCUMod.NAME, version = OpenCUMod.VERSION)
public class OpenCUMod
{
    public static final String MODID = "opencu";
    public static final String NAME = "Open Combat Utils";
    public static final String VERSION = "1.0";

    public static Logger logger;

    @SidedProxy(clientSide = "com.LubieKakao1212.opencu.proxy.ClientProxy", serverSide = "com.LubieKakao1212.opencu.proxy.ServerProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.init(event);
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }
}
