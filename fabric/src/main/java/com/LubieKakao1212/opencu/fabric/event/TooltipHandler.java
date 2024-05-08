package com.LubieKakao1212.opencu.fabric.event;

import com.LubieKakao1212.opencu.common.dispenser.DispenserTooltip;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public class TooltipHandler {

    public static void init() {
        ItemTooltipCallback.EVENT.register(DispenserTooltip::addTooltip);
    }
}
