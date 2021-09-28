package com.LubieKakao1212.opencu.network.packet.dispenser;

import cofh.core.util.helpers.NBTHelper;
import com.LubieKakao1212.opencu.block.tileentity.TileEntityOmniDispenser;
import com.LubieKakao1212.opencu.network.packet.EntityAddVelocityPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdateDispenserPacket implements IMessage {

    private BlockPos position;
    private ItemStack newDispenser;

    public UpdateDispenserPacket() { }

    public UpdateDispenserPacket(BlockPos position, ItemStack newDispenser) {
        this.position = position;
        this.newDispenser = newDispenser;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        position = new BlockPos(
            buf.readInt(),
            buf.readInt(),
            buf.readInt()
        );
        newDispenser = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(position.getX());
        buf.writeInt(position.getY());
        buf.writeInt(position.getZ());
        ByteBufUtils.writeItemStack(buf, newDispenser);
    }

    public BlockPos getPosition() {
        return position;
    }

    public ItemStack getNewDispenser() {
        return newDispenser;
    }

    public static class Handler implements IMessageHandler<UpdateDispenserPacket, IMessage> {
        public Handler() {}

        @Override
        public IMessage onMessage(UpdateDispenserPacket message, MessageContext ctx) {
            World world =  Minecraft.getMinecraft().player.world;

            TileEntity te = world.getTileEntity(message.getPosition());

            if(te instanceof TileEntityOmniDispenser) {
                ((TileEntityOmniDispenser) te).setCurrentDispenserItem(message.newDispenser);
            }

            return null;
        }
    }
}
