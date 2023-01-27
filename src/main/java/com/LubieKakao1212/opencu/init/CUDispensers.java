package com.LubieKakao1212.opencu.init;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.capability.dispenser.DispenserMappings;
import com.LubieKakao1212.opencu.capability.dispenser.vanilla.VanillaDispenserMappings;
import com.LubieKakao1212.opencu.capability.dispenser.vanilla.VanillaDropperMappings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.*;

import java.util.function.Supplier;

@ObjectHolder(OpenCUMod.MODID)
public class CUDispensers {

    @ObjectHolder(ID.VANILLA_DISPENSER)
    public static DispenserMappings VANILLA_DISPENSER;

    @ObjectHolder(ID.VANILLA_DROPPER)
    public static DispenserMappings VANILLA_DROPPER;

    @ObjectHolder(ID.TIER2_DISPENSER)
    public static DispenserMappings TIER2_DISPENSER;

    @ObjectHolder(ID.TIER3_DISPENSER)
    public static DispenserMappings TIER3_DISPENSER;

    private static DeferredRegister<DispenserMappings> MAPPINGS = DeferredRegister.create(ID.DISPENSER_REGISTRY_KEY, OpenCUMod.MODID);

    private static Supplier<IForgeRegistry<DispenserMappings>> registry;

    public static void init() {
        registry = MAPPINGS.makeRegistry(DispenserMappings.class, () -> new RegistryBuilder<DispenserMappings>().onBake((reg, man) -> {
            for(DispenserMappings mapping : reg) {
                mapping.init();
            }
        }));

        MAPPINGS.register(ID.VANILLA_DISPENSER, () -> new VanillaDispenserMappings());
        MAPPINGS.register(ID.VANILLA_DROPPER, () -> new VanillaDropperMappings());

        MAPPINGS.register(ID.TIER2_DISPENSER, () -> new VanillaDispenserMappings());
        MAPPINGS.register(ID.TIER3_DISPENSER, () -> new VanillaDispenserMappings());

        CURegister.register(MAPPINGS);
    }

    public static IForgeRegistry<DispenserMappings> getRegistry() {
        return registry.get();
    }


    private static class ID {
        public static ResourceLocation DISPENSER_REGISTRY_KEY = new ResourceLocation(OpenCUMod.MODID, "dispenser");

        public static final String VANILLA_DISPENSER = "dispenser";
        public static final String VANILLA_DROPPER = "dropper";
        public static final String TIER2_DISPENSER = "tier2";
        public static final String TIER3_DISPENSER = "tier3";
    }
}
