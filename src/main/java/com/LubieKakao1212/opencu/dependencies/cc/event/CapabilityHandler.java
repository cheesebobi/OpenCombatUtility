package com.LubieKakao1212.opencu.dependencies.cc.event;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.block.entity.BlockEntityOmniDispenser;
import com.LubieKakao1212.opencu.block.entity.BlockEntityRepulsor;
import com.LubieKakao1212.opencu.dependencies.cc.capability.DispenserPeripheral;
import com.LubieKakao1212.opencu.dependencies.cc.capability.PeripheralCapabilityProvider;
import com.LubieKakao1212.opencu.dependencies.cc.capability.RepulsorPeripheral;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityHandler {

    public static final Capability<IPeripheral> PERIPHERAL = CapabilityManager.get(new CapabilityToken<>(){});

    @SubscribeEvent
    public static void attachBlockCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {

        if(event.getObject() instanceof BlockEntityRepulsor) {
            BlockEntityRepulsor repulsor = (BlockEntityRepulsor) event.getObject();
            event.addCapability(new ResourceLocation(OpenCUMod.MODID, "peripheral"), new PeripheralCapabilityProvider(() -> new RepulsorPeripheral(repulsor)));
        }

        if(event.getObject() instanceof BlockEntityOmniDispenser) {
            BlockEntityOmniDispenser dispenser = (BlockEntityOmniDispenser) event.getObject();
            event.addCapability(new ResourceLocation(OpenCUMod.MODID, "peripheral"), new PeripheralCapabilityProvider(() -> new DispenserPeripheral(dispenser)));
        }

    }

}
