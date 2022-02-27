package com.LubieKakao1212.opencu.network.packet.dispenser;

import com.LubieKakao1212.opencu.block.tileentity.TileEntityOmniDispenser;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.joml.Quaterniond;

public class UpdateDispenserAimPacket implements IMessage {


    private BlockPos position;
    private Quaterniond aim;

    public UpdateDispenserAimPacket() { }

    public UpdateDispenserAimPacket(BlockPos position, Quaterniond aim) {
        this.position = position;
        this.aim = aim;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        position = new BlockPos(
                buf.readInt(),
                buf.readInt(),
                buf.readInt()
        );
        aim = new Quaterniond(
                buf.readDouble(),
                buf.readDouble(),
                buf.readDouble(),
                buf.readDouble()
        );
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(position.getX());
        buf.writeInt(position.getY());
        buf.writeInt(position.getZ());
        buf.writeDouble(aim.x());
        buf.writeDouble(aim.y());
        buf.writeDouble(aim.z());
        buf.writeDouble(aim.w());
    }

    public BlockPos getPosition() {
        return position;
    }

    public Quaterniond getAim() {
        return aim;
    }

    public static class Handler implements IMessageHandler<UpdateDispenserAimPacket, IMessage> {
        public Handler() {}

        @Override
        public IMessage onMessage(UpdateDispenserAimPacket message, MessageContext ctx) {
            World world =  Minecraft.getMinecraft().player.world;

            TileEntity te = world.getTileEntity(message.getPosition());

            if(te instanceof TileEntityOmniDispenser) {
                ((TileEntityOmniDispenser) te).setCurrentAction(message.aim);
            }
            return null;
        }
    }
}
