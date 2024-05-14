package com.LubieKakao1212.opencu.registry;

import com.LubieKakao1212.opencu.common.device.DispenserMappings;
import com.LubieKakao1212.opencu.common.device.vanilla.VanillaDispenserMappings;
import com.LubieKakao1212.opencu.common.device.vanilla.VanillaDropperMappings;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

//TODO Convert to datapack
public class CUDispensers {

    public static final DispenserMappings VANILLA_DISPENSER = new VanillaDispenserMappings();
    public static final DispenserMappings VANILLA_DROPPER = new VanillaDropperMappings();

    private static final Map<Identifier, DispenserMappings> MAPPINGS = new HashMap<>();

    public static void init() {
        MAPPINGS.clear();

        MAPPINGS.put(CUIds.DISPENSER_MAPPINGS, VANILLA_DISPENSER);
        MAPPINGS.put(CUIds.DROPPER_MAPPINGS, VANILLA_DROPPER);

        VANILLA_DISPENSER.init();
        VANILLA_DROPPER.init();
    }

    public static DispenserMappings getDispenser(Identifier identifier) {
        return MAPPINGS.get(identifier);
    }

}
