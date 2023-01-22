package com.LubieKakao1212.opencu.gui.container;

import net.minecraft.world.inventory.Slot;

@FunctionalInterface
public interface SlotFactory {

    Slot get(int x, int y);

}
