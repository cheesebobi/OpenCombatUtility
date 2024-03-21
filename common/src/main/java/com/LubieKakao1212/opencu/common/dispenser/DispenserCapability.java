package com.LubieKakao1212.opencu.common.dispenser;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class DispenserCapability {

    public static Capability<IDispenser> DISPENSER_CAPABILITY = CapabilityManager.get(new CapabilityToken<IDispenser>() { });

    public static void init() {
        /*CapabilityManager.INSTANCE.register(IDispenser.class, new Storage(), () -> { return new DispenserConstant(DispenserRegistry.VANILLA_DISPENSER,
                (float) OpenCUConfig.omniDispenser.vanilla.rotationSpeed * MathUtil.degToRad / 20.f,
                OpenCUConfig.omniDispenser.vanilla.spread,
                OpenCUConfig.omniDispenser.vanilla.force,
                OpenCUConfig.omniDispenser.vanilla.base_energy
                ); });*/
    }

}
