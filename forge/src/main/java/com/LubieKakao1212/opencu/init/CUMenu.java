package com.LubieKakao1212.opencu.init;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.gui.container.OmnidispenserMenu;
import com.LubieKakao1212.opencu.proxy.Proxy;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegistryObject;

@CUInit
public class CUMenu {

    public static RegistryObject<ScreenHandlerType<OmnidispenserMenu>> OMNI_DISPENSER;

    private static final DeferredRegister<ScreenHandlerType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, OpenCUMod.MODID);

    static {
        OMNI_DISPENSER = MENUS.register(ID.OMNI_DISPENSER, () -> IForgeMenuType.create((id, inv, data) -> new OmnidispenserMenu(id, inv, Proxy.getLevel(), data.readBlockPos())));
    }

    public static void init() {
        CURegister.register(MENUS);
    }

    public static class ID {
        public static final String OMNI_DISPENSER = "adv_dispenser.json";
    }
}
