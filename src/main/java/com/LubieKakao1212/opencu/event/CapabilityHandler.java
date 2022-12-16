package com.LubieKakao1212.opencu.event;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.init.CUItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class CapabilityHandler {

    public static final ItemStack dispenser = new ItemStack(Blocks.DISPENSER);
    public static final ItemStack dropper = new ItemStack(Blocks.DROPPER);

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();

        if(dispenser != null) {
            if(ItemStack.isSame(stack, CapabilityHandler.dispenser)) {
                event.addCapability(new ResourceLocation(OpenCUMod.MODID, "dispenser"), CUItems.VANILLA_DISPENSER.Create(null));
            }
        }
        if(dropper != null) {
            if(ItemStack.isSame(stack, CapabilityHandler.dropper)) {
                event.addCapability(new ResourceLocation(OpenCUMod.MODID, "dispenser"), CUItems.VANILLA_DROPPER.Create(null));
            }
        }
    }   

}
