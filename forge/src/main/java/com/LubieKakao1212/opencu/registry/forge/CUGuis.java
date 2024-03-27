package com.lubiekakao1212.opencu.registry.forge;

import com.lubiekakao1212.opencu.common.gui.ModularFrameScreen;
import com.lubiekakao1212.opencu.registry.CUMenu;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class CUGuis {

    public static void inti() {
        HandledScreens.register(CUMenu.modularFrame(), ModularFrameScreen::new);
    }


}
