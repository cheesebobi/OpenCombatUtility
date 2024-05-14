package com.LubieKakao1212.opencu.forge.event;

import com.LubieKakao1212.opencu.common.device.DispenserTooltip;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class TooltipEventHandler {

    private static final String forceKey = "info.opencu.dispenser.force";
    private static final String speedKey = "info.opencu.dispenser.speed";
    private static final String spreadKey = "info.opencu.dispenser.spread";
    private static final String instantSuffix = ".instant";
    private static final String constantSuffix = ".constant";
    private static final String configurableSuffix = ".configurable";

    @SubscribeEvent
    public static void addTooltip(ItemTooltipEvent event) {
        /*ItemStack stack = event.getItemStack();

        //if(stack.getItem() instanceof CUMultiItem) {
        List<Text> tooltip = event.getToolTip();
        LazyOptional<IDispenser> dispenser = stack.getCapability(CUCapabilities.DISPENSER, null);

        dispenser.ifPresent((dis) -> {
        });*/

        DispenserTooltip.addTooltip(event.getItemStack(), event.getFlags(), event.getToolTip());
    }

}
