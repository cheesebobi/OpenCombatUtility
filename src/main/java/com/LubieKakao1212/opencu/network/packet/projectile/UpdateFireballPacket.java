package com.LubieKakao1212.opencu.network.packet.projectile;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdateFireballPacket implements IMessage {

    private double x;
    private double y;
    private double z;

    private int entityId;

    public UpdateFireballPacket() { }

    public UpdateFireballPacket(int entityId, double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.entityId = entityId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
        entityId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeInt(entityId);
    }

    public static class Handler implements IMessageHandler<UpdateFireballPacket, IMessage> {
        public Handler() { }

        @Override
        public IMessage onMessage(UpdateFireballPacket message, MessageContext ctx) {
            World world =  Minecraft.getMinecraft().player.world;

            Entity entity = world.getEntityByID(message.entityId);

            if(entity instanceof EntityFireball) {
                EntityFireball fireball = ((EntityFireball) entity);
                fireball.accelerationX = message.x;
                fireball.accelerationY = message.y;
                fireball.accelerationZ = message.z;
            }

            return null;
        }
    }
}
