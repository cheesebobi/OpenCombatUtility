package com.LubieKakao1212.opencu.common.registry;

import com.LubieKakao1212.opencu.common.pulse.EntityPulseType;
import dev.architectury.injectables.annotations.ExpectPlatform;

public class CUPulse {

    @ExpectPlatform
    public static EntityPulseType defaultPulse() {
        return null;
    }

}
