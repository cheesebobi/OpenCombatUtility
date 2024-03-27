package com.lubiekakao1212.opencu.registry;

import com.lubiekakao1212.opencu.common.dispenser.DispenserMappings;
import com.lubiekakao1212.opencu.common.dispenser.vanilla.VanillaDispenserMappings;
import com.lubiekakao1212.opencu.common.dispenser.vanilla.VanillaDropperMappings;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

//TODO Convert to datapack
public class CUDispensers {

    public static final DispenserMappings VANILLA_DISPENSER = new VanillaDispenserMappings();
    public static final DispenserMappings VANILLA_DROPPER = new VanillaDropperMappings();

    private static final Map<Identifier, DispenserMappings> MAPPINGS = new HashMap<>();
    static {
        MAPPINGS.put(CUIds.DISPENSER_MAPPINGS, VANILLA_DISPENSER);
        MAPPINGS.put(CUIds.DROPPER_MAPPINGS, VANILLA_DROPPER);
    }
    public static DispenserMappings getDispenser(Identifier identifier) {
        return MAPPINGS.get(identifier);
    }


}
