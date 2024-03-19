package com.LubieKakao1212.opencu.event;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.capability.dispenser.DispenserConfigurable;
import com.LubieKakao1212.opencu.capability.dispenser.DispenserConstant;
import com.LubieKakao1212.opencu.capability.dispenser.DispenserMappings;
import com.LubieKakao1212.opencu.capability.dispenser.IDispenser;
import com.LubieKakao1212.opencu.capability.provider.DispenserProvider;
import com.LubieKakao1212.opencu.config.OpenCUConfigCommon;
import com.LubieKakao1212.opencu.init.CUDispensers;
import com.LubieKakao1212.opencu.init.CUItems;
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
            CUDispensers.VANILLA_DISPENSER.get(),
            (float) OpenCUConfigCommon.DISPENSER.vanillaDispenser.getRotationSpeed() / 20.f,
            OpenCUConfigCommon.DISPENSER.vanillaDispenser.getSpread(),
            OpenCUConfigCommon.DISPENSER.vanillaDispenser.getForce(),
            OpenCUConfigCommon.DISPENSER.vanillaDispenser.getBaseEnergy()
    ));

    public static final Supplier<ICapabilityProvider> VANILLA_DROPPER = () -> new DispenserProvider(new DispenserConstant(
            CUDispensers.VANILLA_DROPPER.get(),
            (float) OpenCUConfigCommon.DISPENSER.vanillaDispenser.getRotationSpeed() / 20.f,
            OpenCUConfigCommon.DISPENSER.vanillaDispenser.getSpread(),
            OpenCUConfigCommon.DISPENSER.vanillaDispenser.getForce(),
            OpenCUConfigCommon.DISPENSER.vanillaDispenser.getBaseEnergy()
    ));

    public static final Supplier<ICapabilityProvider> TIER2_DISPENSER = () -> new DispenserProvider(new DispenserConfigurable(
            CUDispensers.TIER2_DISPENSER.get(),
            (float) OpenCUConfigCommon.DISPENSER.dispenserT2.getRotationSpeed() / 20.f,
            OpenCUConfigCommon.DISPENSER.dispenserT2.getSpread(),
            OpenCUConfigCommon.DISPENSER.dispenserT2.getMaxSpread(),
            OpenCUConfigCommon.DISPENSER.dispenserT2.getForce(),
            OpenCUConfigCommon.DISPENSER.dispenserT2.getBaseEnergy()
    ));

    public static final Supplier<ICapabilityProvider> TIER3_DISPENSER = () -> new DispenserProvider(new DispenserConfigurable(
            CUDispensers.TIER3_DISPENSER.get(),
            (float) OpenCUConfigCommon.DISPENSER.dispenserT3.getRotationSpeed() / 20.f,
            OpenCUConfigCommon.DISPENSER.dispenserT3.getSpread(),
            OpenCUConfigCommon.DISPENSER.dispenserT3.getMaxSpread(),
            OpenCUConfigCommon.DISPENSER.dispenserT3.getForce(),
            OpenCUConfigCommon.DISPENSER.dispenserT3.getBaseEnergy()
    ));

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();

        NbtCompound tag = stack.getNbt();

        if(tag != null) {
            NbtCompound dispenserTag =  tag.getCompound("Dispenser");

            Identifier mappingLocation = new Identifier(dispenserTag.getString("Mapping"));

            DispenserMappings mappings = CUDispensers.getRegistry().getValue(mappingLocation);

            if(mappings != null) {
                boolean configurable = false;
                double minSpread = OpenCUConfigCommon.DISPENSER.vanillaDispenser.getSpread();
                double maxSpread = minSpread;
                double force = OpenCUConfigCommon.DISPENSER.vanillaDispenser.getForce();

                double alignmentSpeed = OpenCUConfigCommon.DISPENSER.vanillaDispenser.getRotationSpeed();

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

                IDispenser dispenser;

                //TODO Energy
                if(configurable) {
                    dispenser = new DispenserConfigurable(mappings, (float)alignmentSpeed / 20.f, minSpread, maxSpread, force, 1.);
                }else {
                    dispenser = new DispenserConstant(mappings, (float)alignmentSpeed / 20.f, minSpread, force, 1.);
                }

                event.addCapability(new Identifier(OpenCUMod.MODID, "dispenser"), new DispenserProvider(dispenser));
                return;
            }
        }

        if(stack.getItem() == Items.DISPENSER) {
            event.addCapability(new Identifier(OpenCUMod.MODID, "dispenser"), VANILLA_DISPENSER.get());
        }
        else if(stack.getItem() == Items.DROPPER) {
            event.addCapability(new Identifier(OpenCUMod.MODID, "dispenser"), VANILLA_DROPPER.get());
        }
        else if(stack.getItem() == CUItems.DISPENSER_T2.get()) {
            event.addCapability(new Identifier(OpenCUMod.MODID, "dispenser"), TIER2_DISPENSER.get());
        }
        else if(stack.getItem() == CUItems.DISPENSER_T3.get()) {
            event.addCapability(new Identifier(OpenCUMod.MODID, "dispenser"), TIER3_DISPENSER.get());
        }
    }
}
