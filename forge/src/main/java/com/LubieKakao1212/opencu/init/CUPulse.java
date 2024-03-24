package com.LubieKakao1212.opencu.init;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.common.pulse.EntityPulseType;
import com.LubieKakao1212.opencu.common.pulse.Pulses;
import com.LubieKakao1212.opencu.pulse.*;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraftforge.registries.*;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class CUPulse {

    public static RegistryObject<EntityPulseType> REPULSOR;

    private static DeferredRegister<EntityPulseType> PULSES = DeferredRegister.create(ID.REPULSOR_REGISTRY_KEY, OpenCUMod.MODID);

    private static Supplier<IForgeRegistry<EntityPulseType>> registry;

    public static void init() {
        registry = PULSES.makeRegistry(
                () -> new RegistryBuilder<EntityPulseType>().onAdd(CUPulse::onAdd));

        REPULSOR = PULSES.register(ID.REPULSOR, () -> new EntityPulseType.Builder(Pulses::repulsorPulse)
                .build());
        PULSES.register(ID.VECTOR, () -> new EntityPulseType.Builder(Pulses::vectorPulse)
                .build());
        PULSES.register(ID.STASIS, () -> new EntityPulseType.Builder(Pulses::stasisPulse)
                .build());

        CURegister.register(PULSES);
    }

    public static IForgeRegistry<EntityPulseType> getRegistry() {
        return registry.get();
    }

    private static void onAdd(IForgeRegistryInternal<EntityPulseType> owner, RegistryManager stage, int id, RegistryKey<EntityPulseType> key, EntityPulseType obj, @Nullable EntityPulseType oldObj) {
        obj.setRegistryKey(key.getValue());
    }

    private static class ID {
        public static final String REPULSOR_REGISTRY_KEY_str = OpenCUMod.MODID + ":repulsor";
        public static final Identifier REPULSOR_REGISTRY_KEY = new Identifier(REPULSOR_REGISTRY_KEY_str);

        public static final String REPULSOR = "repulsor";
        public static final String REPULSOR_ID = OpenCUMod.MODID + ":" + REPULSOR;

        public static final String VECTOR = "vector";
        public static final String VECTOR_ID = OpenCUMod.MODID + ":" + VECTOR;

        public static final String STASIS = "stasis";
        public static final String STASIS_ID = OpenCUMod.MODID + ":" + STASIS;
    }
}
