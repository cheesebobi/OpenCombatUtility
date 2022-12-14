package com.LubieKakao1212.opencu.network.packet;

import com.LubieKakao1212.opencu.network.IOCUPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerAddVelocityPacket implements IOCUPacket {
    public static final double precision = 64000.0D;

    private int motionX, motionY, motionZ;

    public PlayerAddVelocityPacket(double motionXIn, double motionYIn, double motionZIn) {
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

    public PlayerAddVelocityPacket(int motionXIn, int motionYIn, int motionZIn) {
        this.motionX = motionXIn;
        this.motionY = motionYIn;
        this.motionZ = motionZIn;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(motionX);
        buf.writeInt(motionY);
        buf.writeInt(motionZ);
    }

    public static PlayerAddVelocityPacket fromBytes(FriendlyByteBuf buf) {
        return new PlayerAddVelocityPacket(
                buf.readInt(),
                buf.readInt(),
                buf.readInt());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;
            player.setDeltaMovement(player.getDeltaMovement().add(motionX / precision, motionY / precision, motionZ / precision));
        });
        ctx.get().setPacketHandled(true);
    }
}
