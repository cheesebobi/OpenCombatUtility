package com.LubieKakao1212.opencu.init;

import com.LubieKakao1212.opencu.gui.OmnidispenserScreen;
import net.minecraft.client.gui.screens.MenuScreens;

public class CUGuis {

    public static void inti() {
        MenuScreens.register(CUMenu.OMNI_DISPENSER, OmnidispenserScreen::new);
    }


}
