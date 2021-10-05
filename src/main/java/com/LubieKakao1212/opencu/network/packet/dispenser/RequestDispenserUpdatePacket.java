package com.LubieKakao1212.opencu.network.packet.dispenser;

import com.LubieKakao1212.opencu.block.tileentity.TileEntityOmniDispenser;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RequestDispenserUpdatePacket implements IMessage {

    private BlockPos position;

    public RequestDispenserUpdatePacket() { }

    public RequestDispenserUpdatePacket(BlockPos position) {
        this.position = position;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        position = new BlockPos(
                buf.readInt(),
                buf.readInt(),
                buf.readInt()
        );
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(position.getX());
        buf.writeInt(position.getY());
        buf.writeInt(position.getZ());
    }

    public static class Handler implements IMessageHandler<RequestDispenserUpdatePacket, IMessage> {
        public Handler() {}

        @Override
        public IMessage onMessage(RequestDispenserUpdatePacket message, MessageContext ctx) {
            World world = ctx.getServerHandler().player.getServerWorld();

            TileEntity te = world.getTileEntity(message.position);

            if(te instanceof TileEntityOmniDispenser) {
                ((TileEntityOmniDispenser) te).SendDispenserUpdateTo(ctx.getServerHandler().player);
            }
            return null;
        }
    }
}
