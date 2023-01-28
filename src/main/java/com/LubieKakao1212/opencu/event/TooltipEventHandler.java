package com.LubieKakao1212.opencu.event;

import com.LubieKakao1212.opencu.capability.dispenser.DispenserCapability;
import com.LubieKakao1212.opencu.capability.dispenser.IDispenser;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
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

    private static final String configurableKey = "info.opencu.generic.configurable";
    private static final String constantKey = "info.opencu.generic.constant";

    @SubscribeEvent
    public static void AddTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        //if(stack.getItem() instanceof CUMultiItem) {
        List<Component> tooltip = event.getToolTip();
        LazyOptional<IDispenser> dispenser = stack.getCapability(DispenserCapability.DISPENSER_CAPABILITY, null);

        dispenser.ifPresent((dis) -> {
            Component speedValue;
            if(dis.getAlignmentSpeed() >= (180 - 0.1)) {
                speedValue = new TextComponent("Instant").withStyle(ChatFormatting.GREEN);
            } else {
                speedValue = new TextComponent(String.format("%.1f\u00B0/t", dis.getAlignmentSpeed()));
            }
            tooltip.add(new TranslatableComponent(speedKey).append(speedValue));
            tooltip.add(getConfigurableProperty(dis.hasConfigurableSpread(), spreadKey,
                    getValueOrRange(dis.hasConfigurableSpread(), "%.0f\u00B0", dis.getMinSpread(), dis.getMaxSpread())));
            tooltip.add(getConfigurableProperty(dis.hasConfigurableForce(), forceKey,
                    getValueOrRange(dis.hasConfigurableForce(), "%.1f", dis.getMinForce(), dis.getMaxForce())));
        });
    }

    private static Component getConfigurableProperty(boolean configurable, String translationKey, Component value) {

        TranslatableComponent root;

        if(configurable) {
            root = new TranslatableComponent(configurableKey);
        }else {
            root = new TranslatableComponent(constantKey);
        }

        return root.append(new TranslatableComponent(translationKey)).append(value);
    }

    private static Component getValueOrRange(boolean toggle, String valueFormat, double minValue, double maxValue) {
        if(toggle) {
            return new TextComponent(String.format(valueFormat+"-"+valueFormat, minValue, maxValue));
        }else {
            return new TextComponent(String.format(valueFormat, minValue));
        }
    }


}
