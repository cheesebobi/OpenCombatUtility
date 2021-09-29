package com.LubieKakao1212.opencu.event;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.capability.provider.VanillaDispenserProvider;
import com.LubieKakao1212.opencu.capability.provider.VanillaDropperProvider;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class CapabilityHandler {

    public static final ItemStack dispenser = new ItemStack(Blocks.DISPENSER);
    public static final ItemStack dropper = new ItemStack(Blocks.DROPPER);

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();

        if(dispenser != null) {
            if(ItemStack.areItemsEqual(stack, CapabilityHandler.dispenser)) {
                event.addCapability(new ResourceLocation(OpenCUMod.MODID, "dispenser"), new VanillaDispenserProvider());
            }
        }
        if(dropper != null) {
            if(ItemStack.areItemsEqual(stack, CapabilityHandler.dropper)) {
                event.addCapability(new ResourceLocation(OpenCUMod.MODID, "dispenser"), new VanillaDropperProvider());
            }
        }
    }

}
