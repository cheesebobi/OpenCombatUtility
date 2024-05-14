package com.LubieKakao1212.opencu.fabric.event;

import com.LubieKakao1212.opencu.common.device.DispenserTooltip;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientBlockEntityEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;

public class TooltipHandler {

    public static void init() {
        ItemTooltipCallback.EVENT.register(DispenserTooltip::addTooltip);
    }
}
