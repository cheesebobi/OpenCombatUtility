package com.lubiekakao1212.opencu.forge.capability.event;

import com.lubiekakao1212.opencu.common.OpenCUModCommon;
import com.lubiekakao1212.opencu.common.peripheral.DispenserPeripheral;
import com.lubiekakao1212.opencu.common.peripheral.RepulsorPeripheral;
import com.lubiekakao1212.opencu.forge.block.entity.BlockEntityModularFrameImpl;
import com.lubiekakao1212.opencu.forge.block.entity.BlockEntityRepulsorImpl;
import com.lubiekakao1212.opencu.forge.capability.provider.PeripheralCapabilityProvider;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityHandler {

    public static final Capability<IPeripheral> PERIPHERAL = CapabilityManager.get(new CapabilityToken<>(){});

    @SubscribeEvent
    public static void attachBlockCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {

        if(event.getObject() instanceof BlockEntityRepulsorImpl) {
            BlockEntityRepulsorImpl repulsor = (BlockEntityRepulsorImpl) event.getObject();
            event.addCapability(new Identifier(OpenCUModCommon.MODID, "peripheral"), new PeripheralCapabilityProvider(() -> new RepulsorPeripheral(repulsor)));
        }

        if(event.getObject() instanceof BlockEntityModularFrameImpl) {
            BlockEntityModularFrameImpl dispenser = (BlockEntityModularFrameImpl) event.getObject();
            event.addCapability(new Identifier(OpenCUModCommon.MODID, "peripheral"), new PeripheralCapabilityProvider(() -> new DispenserPeripheral(dispenser)));
        }

    }

}
