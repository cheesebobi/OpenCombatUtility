package com.LubieKakao1212.opencu.common.dispenser;

import com.LubieKakao1212.opencu.PlatformUtil;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public class DispenserTooltip {

    private static final String forceKey = "info.opencu.dispenser.force";
    private static final String speedKey = "info.opencu.dispenser.speed";
    private static final String spreadKey = "info.opencu.dispenser.spread";
    private static final String instantSuffix = ".instant";
    private static final String constantSuffix = ".constant";
    private static final String configurableSuffix = ".configurable";

    public static void addTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip) {

        var dis = PlatformUtil.getDispenser(stack);

        if(dis == null) {
            return;
        }

        if (dis.getAlignmentSpeed() >= (180 - 0.1)) {
            tooltip.add(Text.translatable(speedKey + instantSuffix));
        } else {
            tooltip.add(Text.translatable(speedKey, dis.getAlignmentSpeed()));
        }
        tooltip.add(getProperty(dis.hasConfigurableSpread(), spreadKey, dis.getMinSpread(), dis.getMaxSpread()));
        tooltip.add(getProperty(dis.hasConfigurableForce(), forceKey, dis.getMinForce(), dis.getMaxForce()));
    }

    private static Text getProperty(boolean configurable, String translationKey, double min, double max) {
        if(configurable) {
            return Text.translatable(translationKey + configurableSuffix, min, max);
        }else {
            return Text.translatable(translationKey + constantSuffix, min);
        }
    }

}
