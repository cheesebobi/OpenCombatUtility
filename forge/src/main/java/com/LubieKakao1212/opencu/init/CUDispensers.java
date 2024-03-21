package com.LubieKakao1212.opencu.init;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.common.dispenser.DispenserMappings;
import com.LubieKakao1212.opencu.common.dispenser.vanilla.VanillaDispenserMappings;
import com.LubieKakao1212.opencu.common.dispenser.vanilla.VanillaDropperMappings;
import net.minecraft.util.Identifier;
import net.minecraftforge.registries.*;

import java.util.function.Supplier;

public class CUDispensers {

    public static RegistryObject<DispenserMappings> VANILLA_DISPENSER;

    public static RegistryObject<DispenserMappings> VANILLA_DROPPER;

    public static RegistryObject<DispenserMappings> TIER2_DISPENSER;

    public static RegistryObject<DispenserMappings> TIER3_DISPENSER;

    private static DeferredRegister<DispenserMappings> MAPPINGS = DeferredRegister.create(ID.DISPENSER_REGISTRY_KEY, OpenCUMod.MODID);

    private static Supplier<IForgeRegistry<DispenserMappings>> registry;

    public static void init() {
        registry = MAPPINGS.makeRegistry(() -> new RegistryBuilder<DispenserMappings>().onBake((reg, man) -> {
            for(DispenserMappings mapping : reg) {
                mapping.init();
            }
        }));

        VANILLA_DISPENSER = MAPPINGS.register(ID.VANILLA_DISPENSER, VanillaDispenserMappings::new);
        VANILLA_DROPPER = MAPPINGS.register(ID.VANILLA_DROPPER, VanillaDropperMappings::new);

        TIER2_DISPENSER = MAPPINGS.register(ID.TIER2_DISPENSER, VanillaDispenserMappings::new);
        TIER3_DISPENSER = MAPPINGS.register(ID.TIER3_DISPENSER, VanillaDispenserMappings::new);

        CURegister.register(MAPPINGS);
    }

    public static IForgeRegistry<DispenserMappings> getRegistry() {
        return registry.get();
    }


    private static class ID {
        public static final String DISPENSER_REGISTRY_KEY_str = OpenCUMod.MODID + ":dispenser";
        public static final Identifier DISPENSER_REGISTRY_KEY = new Identifier(DISPENSER_REGISTRY_KEY_str);

        public static final String VANILLA_DISPENSER = "dispenser";
        public static final String VANILLA_DROPPER = "dropper";
        public static final String TIER2_DISPENSER = "tier2";
        public static final String TIER3_DISPENSER = "tier3";
    }
}
