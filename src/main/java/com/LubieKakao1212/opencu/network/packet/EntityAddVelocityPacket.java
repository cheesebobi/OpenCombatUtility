package com.LubieKakao1212.opencu.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.IOException;

public class EntityAddVelocityPacket implements IMessage {
    public static final double precision = 64000.0D;

    private int motionX, motionY, motionZ;

    public EntityAddVelocityPacket() {

    }

    public EntityAddVelocityPacket(double motionXIn, double motionYIn, double motionZIn) {
        //packet = new SPacketEntityVelocity(e.getEntityId(), e.motionX+vX, e.motionY+vY, e.motionZ+vZ);

        double d0 = 3.9D;

        if (motionXIn < -d0)
        {
            motionXIn = -d0;
        }

        if (motionYIn < -d0)
        {
            motionYIn = -d0;
        }

        if (motionZIn < -d0)
        {
            motionZIn = -d0;
        }

        if (motionXIn > d0)
        {
            motionXIn = d0;
        }

        if (motionYIn > d0)
        {
            motionYIn = d0;
        }

        if (motionZIn > d0)
        {
            motionZIn = d0;
        }

        this.motionX = (int)(motionXIn * precision);
        this.motionY = (int)(motionYIn * precision);
        this.motionZ = (int)(motionZIn * precision);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        motionX = buf.readInt();
        motionY = buf.readInt();
        motionZ = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(motionX);
        buf.writeInt(motionY);
        buf.writeInt(motionZ);
    }

    public static class Handler implements IMessageHandler<EntityAddVelocityPacket, IMessage>
    {
        public Handler() {}

        @Override
        public IMessage onMessage(EntityAddVelocityPacket message, MessageContext ctx) {
            Minecraft.getMinecraft().player.addVelocity(message.motionX / precision, message.motionY / precision, message.motionZ / precision);
            return null;
        }
    }
}
