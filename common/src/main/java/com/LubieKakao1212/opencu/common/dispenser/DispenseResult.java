package com.LubieKakao1212.opencu.common.dispenser;

import net.minecraft.item.ItemStack;

public record DispenseResult(boolean wasSuccessful, ItemStack leftover) {

}
