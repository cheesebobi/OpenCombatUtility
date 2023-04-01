package com.LubieKakao1212.opencu.init;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.capability.dispenser.DispenserMappings;
import com.LubieKakao1212.opencu.capability.dispenser.vanilla.VanillaDispenserMappings;
import com.LubieKakao1212.opencu.capability.dispenser.vanilla.VanillaDropperMappings;
import com.LubieKakao1212.opencu.pulse.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

@ObjectHolder(OpenCUMod.MODID)
public class CUPulse {

    @ObjectHolder(ID.REPULSOR)
    public static EntityPulseType REPULSOR;

    @ObjectHolder(ID.VECTOR)
    public static EntityPulseType VECTOR;

    @ObjectHolder(ID.STASIS)
    public static EntityPulseType STASIS;

    private static DeferredRegister<EntityPulseType> PULSES = DeferredRegister.create(ID.REPULSOR_REGISTRY_KEY, OpenCUMod.MODID);

    private static Supplier<IForgeRegistry<EntityPulseType>> registry;

    public static void init() {
        registry = PULSES.makeRegistry(EntityPulseType.class, RegistryBuilder::new);

        PULSES.register(ID.REPULSOR, () -> new EntityPulseType.Builder(RepulsorPulse::new)
                .build());
        PULSES.register(ID.VECTOR, () -> new EntityPulseType.Builder(VectorPulse::new)
                .build());
        PULSES.register(ID.STASIS, () -> new EntityPulseType.Builder(StasisPulse::new)
                .forceTransformer(ForceTransformer.scale(0.95f))
                .build());

        CURegister.register(PULSES);
    }

    public static IForgeRegistry<EntityPulseType> getRegistry() {
        return registry.get();
    }

    private static class ID {
        public static ResourceLocation REPULSOR_REGISTRY_KEY = new ResourceLocation(OpenCUMod.MODID, "repulsor");

        public static final String REPULSOR = "repulsor";
        public static final String VECTOR = "vector";
        public static final String STASIS = "stasis";
    }
}
