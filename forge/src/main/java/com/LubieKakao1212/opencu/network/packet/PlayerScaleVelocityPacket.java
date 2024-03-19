package com.LubieKakao1212.opencu.network.packet;

import com.LubieKakao1212.opencu.network.IOCUPacket;
import com.LubieKakao1212.opencu.network.util.ClientPlayerAddVelocity;
import com.LubieKakao1212.opencu.network.util.ClientPlayerScaleVelocity;
import net.minecraft.network.PacketByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerScaleVelocityPacket implements IOCUPacket {
    public static final double precision = 64000.0D;

    private double motionScale;

    public PlayerScaleVelocityPacket(double motionScale) {
        this.motionScale = motionScale;
    }

    public void toBytes(PacketByteBuf buf) {
        buf.writeDouble(motionScale);
    }

    public static PlayerScaleVelocityPacket fromBytes(PacketByteBuf buf) {
        return new PlayerScaleVelocityPacket(buf.readDouble());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new ClientPlayerScaleVelocity(motionScale));
        });
        ctx.get().setPacketHandled(true);
    }
}
