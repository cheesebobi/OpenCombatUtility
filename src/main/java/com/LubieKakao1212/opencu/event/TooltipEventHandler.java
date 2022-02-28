package com.LubieKakao1212.opencu.event;

import com.LubieKakao1212.opencu.capability.dispenser.DispenserCapability;
import com.LubieKakao1212.opencu.capability.dispenser.IDispenser;
import com.LubieKakao1212.opencu.item.CUMultiItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

@Mod.EventBusSubscriber
public class TooltipEventHandler {

    @SubscribeEvent
    public static void AddTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        if(stack.getItem() instanceof CUMultiItem) {
            List<String> tooltip = event.getToolTip();
            if(stack.hasCapability(DispenserCapability.DISPENSER_CAPABILITY, null)) {
                IDispenser dispenser = stack.getCapability(DispenserCapability.DISPENSER_CAPABILITY, null);
                String speed;
                if(dispenser.getAlignmentSpeed() >= (180 - 0.1)) {
                    speed = TextFormatting.GREEN+"Instant";
                } else {
                    speed = String.format("%.1f\u00B0/t", dispenser.getAlignmentSpeed());
                }
                replaceProperty(tooltip, "speed", speed);
                replaceProperty(tooltip, "spread", String.format("%.0f\u00B0-%.0f\u00B0", dispenser.getMinSpread(), dispenser.getMaxSpread()));
                replaceProperty(tooltip, "force", String.format("%.1f-%.1f", dispenser.getMinForce(), dispenser.getMaxForce()));
            }
        }
    }

    private static void replaceProperty(List<String> tooltip, String property, String value) {
        for(int i = 0; i < tooltip.size(); i++) {
            String line = tooltip.get(i);
            String regex = String.format("\\{%s\\}", property);
            tooltip.set(i, line.replaceAll(regex, value));
        }
    }

}
