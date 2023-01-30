package com.LubieKakao1212.opencu.network.packet.dispenser;

import com.LubieKakao1212.opencu.block.entity.BlockEntityOmniDispenser;
import com.LubieKakao1212.opencu.network.IOCUPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RequestDispenserUpdatePacket implements IOCUPacket {

    private BlockPos position;

    public RequestDispenserUpdatePacket() { }

    public RequestDispenserUpdatePacket(BlockPos position) {
        this.position = position;
    }


    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(position.getX());
        buf.writeInt(position.getY());
        buf.writeInt(position.getZ());
    }

    public static RequestDispenserUpdatePacket fromBytes(ByteBuf buf) {
        return new RequestDispenserUpdatePacket(new BlockPos(
                buf.readInt(),
                buf.readInt(),
                buf.readInt()
        ));
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            Level level = sender.getLevel();

            BlockEntity be = level.getBlockEntity(position);

            if(be instanceof BlockEntityOmniDispenser) {
                ((BlockEntityOmniDispenser) be).sendDispenserUpdateTo(sender);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
