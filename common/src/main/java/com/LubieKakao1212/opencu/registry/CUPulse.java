package com.lubiekakao1212.opencu.registry;

import com.lubiekakao1212.opencu.common.pulse.EntityPulseType;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class CUPulse {

    @ExpectPlatform
    public static EntityPulseType defaultPulse() {
        return null;
    }

    @Nullable
    @ExpectPlatform
    public static EntityPulseType get(Identifier id) { return null; }

}
