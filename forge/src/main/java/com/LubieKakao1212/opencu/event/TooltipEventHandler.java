package com.LubieKakao1212.opencu.event;

import com.LubieKakao1212.opencu.common.dispenser.IDispenser;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

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
        ItemStack stack = event.getItemStack();

        //if(stack.getItem() instanceof CUMultiItem) {
        List<Text> tooltip = event.getToolTip();
        LazyOptional<IDispenser> dispenser = stack.getCapability(DispenserCapability.DISPENSER_CAPABILITY, null);

        dispenser.ifPresent((dis) -> {
            if(dis.getAlignmentSpeed() >= (180 - 0.1)) {
                tooltip.add(Text.translatable(speedKey + instantSuffix));
            } else {
                tooltip.add(Text.translatable(speedKey, dis.getAlignmentSpeed()));
            }
            tooltip.add(getProperty(dis.hasConfigurableSpread(), spreadKey, dis.getMinSpread(), dis.getMaxSpread()));
            tooltip.add(getProperty(dis.hasConfigurableForce(), forceKey, dis.getMinForce(), dis.getMaxForce()));
        });
    }

    private static Text getProperty(boolean configurable, String translationKey, double min, double max) {
        if(configurable) {
            return Text.translatable(translationKey + configurableSuffix, min, max);
        }else {
            return Text.translatable(translationKey + constantSuffix, min);
        }
    }


}
