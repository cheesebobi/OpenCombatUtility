package com.LubieKakao1212.opencu.init;

import com.LubieKakao1212.opencu.gui.OmnidispenserScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class CUGuis {

    public static void inti() {
        HandledScreens.register(CUMenu.OMNI_DISPENSER.get(), OmnidispenserScreen::new);
    }


}
