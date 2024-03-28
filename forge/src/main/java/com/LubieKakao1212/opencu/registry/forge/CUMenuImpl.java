package com.LubieKakao1212.opencu.registry.forge;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.gui.container.ModularFrameMenu;
import com.LubieKakao1212.opencu.forge.proxy.Proxy;
import com.LubieKakao1212.opencu.registry.CUIds;
import com.LubieKakao1212.opencu.registry.CUMenu;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CUMenuImpl {
    public static RegistryObject<ScreenHandlerType<ModularFrameMenu>> MODULAR_FRAME;

    private static final DeferredRegister<ScreenHandlerType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, OpenCUModCommon.MODID);

    static {
        MODULAR_FRAME = MENUS.register(CUIds.MODULAR_FRAME.getPath(), () -> IForgeMenuType.create((id, inv, data) -> new ModularFrameMenu(id, inv, Proxy.getLevel(), data.readBlockPos())));
    }

    public static void init() {
        CURegister.register(MENUS);
    }

    public static ScreenHandlerType<ModularFrameMenu> modularFrame() {
        return MODULAR_FRAME.get();
    }
}
