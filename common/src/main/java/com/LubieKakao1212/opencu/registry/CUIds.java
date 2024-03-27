package com.lubiekakao1212.opencu.registry;

import com.lubiekakao1212.opencu.common.OpenCUModCommon;
import net.minecraft.util.Identifier;

public class CUIds {

    public static final Identifier MODULAR_FRAME = new Identifier(OpenCUModCommon.MODID, Str.MODULAR_FRAME);
    public static final Identifier REPULSOR = new Identifier(OpenCUModCommon.MODID, Str.REPULSOR);

    public static final Identifier DISPENSER_MAPPINGS = new Identifier(OpenCUModCommon.MODID, Str.DISPENSER);
    public static final Identifier DROPPER_MAPPINGS = new Identifier(OpenCUModCommon.MODID, Str.DROPPER);


    public static final Identifier PULSE_REPULSOR = new Identifier(OpenCUModCommon.MODID, Str.REPULSOR);
    public static final Identifier PULSE_VECTOR = new Identifier(OpenCUModCommon.MODID, Str.VECTOR);
    public static final Identifier PULSE_STASIS = new Identifier(OpenCUModCommon.MODID, Str.STASIS);


    public static class Str {
        public static final String MODULAR_FRAME = "modular_frame";
        public static final String REPULSOR = "repulsor";
        public static final String DISPENSER = "dispenser";
        public static final String DROPPER = "dropper";
        public static final String VECTOR = "vector";
        public static final String STASIS = "stasis";
    }

}
