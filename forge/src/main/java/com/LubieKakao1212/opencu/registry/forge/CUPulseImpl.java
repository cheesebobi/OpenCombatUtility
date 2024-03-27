package com.lubiekakao1212.opencu.registry.forge;

import com.lubiekakao1212.opencu.common.OpenCUModCommon;
import com.lubiekakao1212.opencu.common.pulse.EntityPulseType;
import com.lubiekakao1212.opencu.common.pulse.Pulses;
import com.lubiekakao1212.opencu.registry.CUIds;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraftforge.registries.*;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class CUPulseImpl {

    public static RegistryObject<EntityPulseType> REPULSOR;

    private static final DeferredRegister<EntityPulseType> PULSES = DeferredRegister.create(CUIds.REPULSOR, OpenCUModCommon.MODID);

    private static Supplier<IForgeRegistry<EntityPulseType>> registry;

    public static void init() {
        registry = PULSES.makeRegistry(
                () -> new RegistryBuilder<EntityPulseType>().onAdd(CUPulseImpl::onAdd));

        REPULSOR = PULSES.register(CUIds.Str.REPULSOR, () -> new EntityPulseType.Builder(Pulses::repulsorPulse)
                .build());
        PULSES.register(CUIds.Str.VECTOR, () -> new EntityPulseType.Builder(Pulses::vectorPulse)
                .build());
        PULSES.register(CUIds.Str.STASIS, () -> new EntityPulseType.Builder(Pulses::stasisPulse)
                .build());

        CURegister.register(PULSES);
    }

    private static void onAdd(IForgeRegistryInternal<EntityPulseType> owner, RegistryManager stage, int id, RegistryKey<EntityPulseType> key, EntityPulseType obj, @Nullable EntityPulseType oldObj) {
        obj.setRegistryKey(key.getValue());
    }

    public static EntityPulseType defaultPulse() {
        return REPULSOR.get();
    }

    @Nullable
    public static EntityPulseType get(Identifier id) {
        return registry.get().getValue(id);
    }
}
