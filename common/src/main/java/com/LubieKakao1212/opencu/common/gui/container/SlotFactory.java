package com.lubiekakao1212.opencu.common.gui.container;

import net.minecraft.screen.slot.Slot;

@FunctionalInterface
public interface SlotFactory {

    Slot get(int x, int y);

}
