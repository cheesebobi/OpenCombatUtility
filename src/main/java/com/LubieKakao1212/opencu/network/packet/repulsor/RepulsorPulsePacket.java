package com.LubieKakao1212.opencu.network.packet.repulsor;

import com.LubieKakao1212.opencu.block.entity.BlockEntityOmniDispenser;
import com.LubieKakao1212.opencu.block.entity.BlockEntityRepulsor;
import com.LubieKakao1212.opencu.network.IOCUPacket;
import com.LubieKakao1212.opencu.proxy.Proxy;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RepulsorPulsePacket implements IOCUPacket {

    protected BlockPos position;

    public RepulsorPulsePacket(BlockPos position) {
        this.position = position;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(position);
    }

    public static RepulsorPulsePacket fromBytes(FriendlyByteBuf buf) {
        return new RepulsorPulsePacket(buf.readBlockPos());
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Level world = Proxy.getLevel();

            BlockEntity te = world.getBlockEntity(position);

            if (te instanceof BlockEntityRepulsor) {
                ((BlockEntityRepulsor) te).setPulseTimer();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
