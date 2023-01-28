package com.LubieKakao1212.opencu.event;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.capability.dispenser.DispenserConfigurable;
import com.LubieKakao1212.opencu.capability.dispenser.DispenserConstant;
import com.LubieKakao1212.opencu.capability.dispenser.DispenserMappings;
import com.LubieKakao1212.opencu.capability.dispenser.IDispenser;
import com.LubieKakao1212.opencu.capability.provider.DispenserProvider;
import com.LubieKakao1212.opencu.capability.util.CapabilityInitializer;
import com.LubieKakao1212.opencu.config.OpenCUConfig;
import com.LubieKakao1212.opencu.init.CUDispensers;
import com.LubieKakao1212.opencu.init.CUItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;

@Mod.EventBusSubscriber()
public class CapabilityHandler {

    public static final Supplier<ICapabilityProvider> VANILLA_DISPENSER = () -> new DispenserProvider(new DispenserConstant(
            CUDispensers.VANILLA_DISPENSER,
            (float) OpenCUConfig.omniDispenser.vanilla.rotationSpeed / 20.f,
            OpenCUConfig.omniDispenser.vanilla.spread,
            OpenCUConfig.omniDispenser.vanilla.force,
            OpenCUConfig.omniDispenser.vanilla.base_energy
    ));

    public static final Supplier<ICapabilityProvider> VANILLA_DROPPER = () -> new DispenserProvider(new DispenserConstant(
            CUDispensers.VANILLA_DROPPER,
            (float) OpenCUConfig.omniDispenser.vanilla.rotationSpeed / 20.f,
            OpenCUConfig.omniDispenser.vanilla.spread,
            OpenCUConfig.omniDispenser.vanilla.force,
            OpenCUConfig.omniDispenser.vanilla.base_energy
    ));

    public static final Supplier<ICapabilityProvider> TIER2_DISPENSER = () -> new DispenserProvider(new DispenserConfigurable(
            CUDispensers.TIER2_DISPENSER,
            (float) OpenCUConfig.omniDispenser.tier2.rotationSpeed / 20.f,
            OpenCUConfig.omniDispenser.tier2.spread,
            OpenCUConfig.omniDispenser.tier2.spread_max,
            OpenCUConfig.omniDispenser.tier2.force,
            OpenCUConfig.omniDispenser.tier2.base_energy
    ));

    public static final Supplier<ICapabilityProvider> TIER3_DISPENSER = () -> new DispenserProvider(new DispenserConfigurable(
            CUDispensers.TIER3_DISPENSER,
            (float) OpenCUConfig.omniDispenser.tier3.rotationSpeed / 20.f,
            OpenCUConfig.omniDispenser.tier3.spread,
            OpenCUConfig.omniDispenser.tier3.spread_max,
            OpenCUConfig.omniDispenser.tier3.force,
            OpenCUConfig.omniDispenser.tier3.base_energy
    ));

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();

        CompoundTag tag = stack.getTag();

        if(tag != null) {
            CompoundTag dispenserTag =  tag.getCompound("Dispenser");

            ResourceLocation mappingLocation = new ResourceLocation(dispenserTag.getString("Mapping"));

            DispenserMappings mappings = CUDispensers.getRegistry().getValue(mappingLocation);

            if(mappings != null) {
                boolean configurable = false;
                double minSpread = OpenCUConfig.omniDispenser.vanilla.spread;
                double maxSpread = OpenCUConfig.omniDispenser.vanilla.spread;
                double minForce = OpenCUConfig.omniDispenser.vanilla.force;
                double maxForce = OpenCUConfig.omniDispenser.vanilla.force;

                double alignmentSpeed = OpenCUConfig.omniDispenser.vanilla.rotationSpeed;

                if(dispenserTag.contains("Spread", Tag.TAG_ANY_NUMERIC)) {
                    minSpread = dispenserTag.getDouble("Spread");
                    maxSpread = minSpread;
                }
                else {
                    ListTag list = dispenserTag.getList("Spread", Tag.TAG_ANY_NUMERIC);

                    if(list.size() == 2) {
                        minSpread = list.getDouble(0);
                        maxSpread = list.getDouble(1);
                        configurable = true;
                    }
                }

                if(dispenserTag.contains("Force", Tag.TAG_ANY_NUMERIC)) {
                    minForce = dispenserTag.getDouble("Force");
                    maxForce = minForce;
                }
                else {
                    ListTag list = dispenserTag.getList("Force", Tag.TAG_ANY_NUMERIC);

                    if(list.size() == 2) {
                        minForce = list.getDouble(0);
                        maxForce = list.getDouble(1);
                        configurable = true;
                    }
                }

                if(dispenserTag.contains("Speed", Tag.TAG_ANY_NUMERIC)) {
                    alignmentSpeed = dispenserTag.getDouble("Speed");
                }

                IDispenser dispenser;

                //TODO Energy
                if(configurable) {
                    dispenser = new DispenserConfigurable(mappings, (float)alignmentSpeed / 20.f, minSpread, maxSpread, maxForce, 1.);
                }else {
                    dispenser = new DispenserConstant(mappings, (float)alignmentSpeed / 20.f, minSpread, minForce, 1.);
                }

                event.addCapability(new ResourceLocation(OpenCUMod.MODID, "dispenser"), new DispenserProvider(dispenser));
                return;
            }
        }

        if(stack.getItem() == Items.DISPENSER) {
            event.addCapability(new ResourceLocation(OpenCUMod.MODID, "dispenser"), VANILLA_DISPENSER.get());
        }
        else if(stack.getItem() == Items.DROPPER) {
            event.addCapability(new ResourceLocation(OpenCUMod.MODID, "dispenser"), VANILLA_DROPPER.get());
        }
        else if(stack.getItem() == CUItems.DISPENSER_T2) {
            event.addCapability(new ResourceLocation(OpenCUMod.MODID, "dispenser"), TIER2_DISPENSER.get());
        }
        else if(stack.getItem() == CUItems.DISPENSER_T3) {
            event.addCapability(new ResourceLocation(OpenCUMod.MODID, "dispenser"), TIER3_DISPENSER.get());
        }
    }
}
