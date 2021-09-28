package com.LubieKakao1212.opencu.network.packet.dispenser;

import com.LubieKakao1212.opencu.block.tileentity.TileEntityOmniDispenser;
import glm.quat.Quat;
import glm.vec._2.Vec2;
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

public class UpdateDispenserAimPacket implements IMessage {


    private BlockPos position;
    private Quat aim;

    public UpdateDispenserAimPacket() { }

    public UpdateDispenserAimPacket(BlockPos position, Quat aim) {
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
        aim = new Quat(
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat()
        );
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(position.getX());
        buf.writeInt(position.getY());
        buf.writeInt(position.getZ());
        buf.writeFloat(aim.w);
        buf.writeFloat(aim.x);
        buf.writeFloat(aim.y);
        buf.writeFloat(aim.z);
    }

    public BlockPos getPosition() {
        return position;
    }

    public Quat getAim() {
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
