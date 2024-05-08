package com.LubieKakao1212.opencu.common.gui.container;

import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.slot.Slot;

@FunctionalInterface
public interface SlotProvider {

    Slot createSlot(int idx, int x, int y);

    static SlotProvider of(Inventory inv) {
        return (idx, x, y) -> new Slot(inv, idx, x, y);
    }

    static SlotProvider dummy(int slotCount) {
        return of(new SimpleInventory(slotCount));
    }



}
