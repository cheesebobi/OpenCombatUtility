package com.LubieKakao1212.opencu.common.device;

import com.LubieKakao1212.opencu.PlatformUtil;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public class DispenserTooltip {

    private static final String degPerTickKey = "info.opencu.generic.dt";
    private static final String instantKey = "§aInstant";

    private static final String speedKey = "info.opencu.frame.speed";
    private static final String pitchSuffix = ".pitch";
    private static final String yawSuffix = ".yaw";

    private static final String forceKey = "info.opencu.dispenser.force";
    private static final String spreadKey = "info.opencu.dispenser.spread";
    private static final String constantSuffix = ".constant";
    private static final String configurableSuffix = ".configurable";

    public static void addTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip) {

        var dis = PlatformUtil.getDispenser(stack);

        if(dis == null) {
            return;
        }

        tooltip.add(getSpeed(dis.getPitchAlignmentSpeed(), pitchSuffix));
        tooltip.add(getSpeed(dis.getYawAlignmentSpeed(), yawSuffix));

        /*tooltip.add(getProperty(false, spreadKey));
        tooltip.add(getProperty(false, forceKey));*/
    }

    private static Text getSpeed(double value, String suffix) {
        var text = Text.translatable(speedKey + suffix);
        if (value >= (180 - 0.1)) {
            return text.append(Text.translatable(instantKey));
        } else {
            return text.append(Text.translatable(degPerTickKey, value));
        }
    }

    private static Text getProperty(boolean configurable, String translationKey, double min, double max) {
        if(configurable) {
            return Text.translatable(translationKey + configurableSuffix, min, max);
        }else {
            return Text.translatable(translationKey + constantSuffix, min);
        }
    }

}
