package com.LubieKakao1212.opencu.registry.fabric;

import com.LubieKakao1212.opencu.common.pulse.EntityPulseType;
import com.LubieKakao1212.opencu.common.pulse.Pulses;
import com.LubieKakao1212.opencu.registry.CUIds;
import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public class CUPulseImpl implements AutoRegistryContainer<EntityPulseType> {

    public static final Registry<EntityPulseType> PULSE_TYPES = FabricRegistryBuilder.createSimple(RegistryKey.<EntityPulseType>ofRegistry(CUIds.PULSE_TYPE)).buildAndRegister();

    public static final EntityPulseType REPULSOR = new EntityPulseType.Builder(Pulses::repulsorPulse).build();
    public static final EntityPulseType VECTOR = new EntityPulseType.Builder(Pulses::vectorPulse).build();
    public static final EntityPulseType STASIS = new EntityPulseType.Builder(Pulses::stasisPulse).build();


    public static EntityPulseType defaultPulse() {
        return REPULSOR;
    }

    @Nullable
    public static EntityPulseType get(Identifier id) {
        return PULSE_TYPES.get(id);
    }


    @Override
    public Registry<EntityPulseType> getRegistry() {
        return PULSE_TYPES;
    }

    @Override
    public Class<EntityPulseType> getTargetFieldType() {
        return EntityPulseType.class;
    }

    @Override
    public void postProcessField(String namespace, EntityPulseType value, String identifier, Field field) {
        AutoRegistryContainer.super.postProcessField(namespace, value, identifier, field);
        value.setRegistryKey(new Identifier(namespace, identifier));
    }
}
