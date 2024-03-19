package com.LubieKakao1212.opencu.gui.container;

import net.minecraft.screen.slot.Slot;

@FunctionalInterface
public interface SlotFactory {

    Slot get(int x, int y);

}
