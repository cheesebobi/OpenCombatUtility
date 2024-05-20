package com.LubieKakao1212.opencu.forge.event;

import com.LubieKakao1212.opencu.OpenCUConfigCommon;
import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.device.DispenserConfigurable;
import com.LubieKakao1212.opencu.common.device.DispenserConstant;
import com.LubieKakao1212.opencu.common.device.ShotMappings;
import com.LubieKakao1212.opencu.common.device.IFramedDevice;
import com.LubieKakao1212.opencu.forge.capability.provider.DispenserProvider;
import com.LubieKakao1212.opencu.registry.CUDispensers;
import com.LubieKakao1212.opencu.registry.forge.CUItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;

@Mod.EventBusSubscriber()
public class CapabilityHandler {

    public static final Supplier<ICapabilityProvider> VANILLA_DISPENSER = () -> new DispenserProvider(new DispenserConstant(
            CUDispensers.VANILLA_DISPENSER,
            (float) OpenCUConfigCommon.vanillaDispenserDevice().rotationSpeed() / 20.f,
            OpenCUConfigCommon.vanillaDispenserDevice().spread(),
            OpenCUConfigCommon.vanillaDispenserDevice().force(),
            OpenCUConfigCommon.vanillaDispenserDevice().baseEnergy()
    ));

    public static final Supplier<ICapabilityProvider> VANILLA_DROPPER = () -> new DispenserProvider(new DispenserConstant(
            CUDispensers.VANILLA_DROPPER,
            (float) OpenCUConfigCommon.vanillaDispenserDevice().rotationSpeed() / 20.f,
            OpenCUConfigCommon.vanillaDispenserDevice().spread(),
            OpenCUConfigCommon.vanillaDispenserDevice().force(),
            OpenCUConfigCommon.vanillaDispenserDevice().baseEnergy()
    ));

    public static final Supplier<ICapabilityProvider> TIER2_DISPENSER = () -> new DispenserProvider(new DispenserConfigurable(
            CUDispensers.VANILLA_DISPENSER,
            (float) OpenCUConfigCommon.goldenDispenserDevice().rotationSpeed() / 20.f,
            OpenCUConfigCommon.goldenDispenserDevice().spread(),
            OpenCUConfigCommon.goldenDispenserDevice().maxSpread(),
            OpenCUConfigCommon.goldenDispenserDevice().force(),
            OpenCUConfigCommon.goldenDispenserDevice().baseEnergy()
    ));

    public static final Supplier<ICapabilityProvider> TIER3_DISPENSER = () -> new DispenserProvider(new DispenserConfigurable(
            CUDispensers.VANILLA_DISPENSER,
            (float) OpenCUConfigCommon.diamondDispenserDevice().rotationSpeed() / 20.f,
            OpenCUConfigCommon.diamondDispenserDevice().spread(),
            OpenCUConfigCommon.diamondDispenserDevice().maxSpread(),
            OpenCUConfigCommon.diamondDispenserDevice().force(),
            OpenCUConfigCommon.diamondDispenserDevice().baseEnergy()
    ));

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();

        NbtCompound tag = stack.getNbt();

        if(tag != null) {
            NbtCompound dispenserTag =  tag.getCompound("Dispenser");

            Identifier mappingLocation = new Identifier(dispenserTag.getString("Mapping"));

            ShotMappings mappings = CUDispensers.getDispenser(mappingLocation);//CUDispensers.getRegistry().getValue(mappingLocation);

            if(mappings != null) {
                boolean configurable = false;
                double minSpread = OpenCUConfigCommon.vanillaDispenserDevice().spread();
                double maxSpread = minSpread;
                double force = OpenCUConfigCommon.vanillaDispenserDevice().force();

                double alignmentSpeed = OpenCUConfigCommon.vanillaDispenserDevice().rotationSpeed();

                if(dispenserTag.contains("Spread", NbtElement.NUMBER_TYPE)) {
                    minSpread = dispenserTag.getDouble("Spread");
                    maxSpread = minSpread;
                }
                else {
                    NbtList list = dispenserTag.getList("Spread", NbtElement.NUMBER_TYPE);

                    if(list.size() == 2) {
                        minSpread = list.getDouble(0);
                        maxSpread = list.getDouble(1);
                        configurable = true;
                    }
                }

                if(dispenserTag.contains("Force", NbtElement.NUMBER_TYPE)) {
                    force = dispenserTag.getDouble("Force");
                }

                if(dispenserTag.contains("Speed", NbtElement.NUMBER_TYPE)) {
                    alignmentSpeed = dispenserTag.getDouble("Speed");
                }

                IFramedDevice dispenser;

                //TODO Energy
                if(configurable) {
                    dispenser = new DispenserConfigurable(mappings, (float)alignmentSpeed / 20.f, minSpread, maxSpread, force, 1.);
                }else {
                    dispenser = new DispenserConstant(mappings, (float)alignmentSpeed / 20.f, minSpread, force, 1.);
                }

                event.addCapability(new Identifier(OpenCUModCommon.MODID, "dispenser"), new DispenserProvider(dispenser));
                return;
            }
        }

        if(stack.getItem() == Items.DISPENSER) {
            event.addCapability(new Identifier(OpenCUModCommon.MODID, "dispenser"), VANILLA_DISPENSER.get());
        }
        else if(stack.getItem() == Items.DROPPER) {
            event.addCapability(new Identifier(OpenCUModCommon.MODID, "dispenser"), VANILLA_DROPPER.get());
        }
        else if(stack.getItem() == CUItems.DISPENSER_T2.get()) {
            event.addCapability(new Identifier(OpenCUModCommon.MODID, "dispenser"), TIER2_DISPENSER.get());
        }
        else if(stack.getItem() == CUItems.DISPENSER_T3.get()) {
            event.addCapability(new Identifier(OpenCUModCommon.MODID, "dispenser"), TIER3_DISPENSER.get());
        }
    }
}
