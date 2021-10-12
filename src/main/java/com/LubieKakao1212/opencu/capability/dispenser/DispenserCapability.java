package com.LubieKakao1212.opencu.capability.dispenser;

import com.LubieKakao1212.opencu.config.OpenCUConfig;
import com.LubieKakao1212.opencu.lib.math.MathUtil;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class DispenserCapability {

    @CapabilityInject(IDispenser.class)
    public static Capability<IDispenser> DISPENSER_CAPABILITY;

    public static void Init() {
        CapabilityManager.INSTANCE.register(IDispenser.class, new Storage(), () -> { return new DispenserConstant(DispenserRegistry.VANILLA_DISPENSER,
                (float) OpenCUConfig.omniDispenser.vanilla.rotationSpeed * MathUtil.degToRad / 20.f,
                OpenCUConfig.omniDispenser.vanilla.spread,
                OpenCUConfig.omniDispenser.vanilla.force,
                OpenCUConfig.omniDispenser.vanilla.base_energy
                ); });
    }

    public static class Storage implements Capability.IStorage<IDispenser> {

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<IDispenser> capability, IDispenser instance, EnumFacing side) {
            return null;
        }

        @Override
        public void readNBT(Capability<IDispenser> capability, IDispenser instance, EnumFacing side, NBTBase nbt) {

        }
    }

}
