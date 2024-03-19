package com.LubieKakao1212.opencu.network.packet.projectile;

import com.LubieKakao1212.opencu.network.IOCUPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.World;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateFireballPacket implements IOCUPacket {

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

    public void toBytes(PacketByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }

    public static UpdateFireballPacket fromBytes(PacketByteBuf buf) {
        return new UpdateFireballPacket(
                buf.readInt(),
                buf.readDouble(),
                buf.readDouble(),
                buf.readDouble()
        );
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            World level =  MinecraftClient.getInstance().player.world;

            Entity entity = level.getEntityById(entityId);

            if(entity instanceof AbstractFireballEntity) {
                AbstractFireballEntity fireball = ((AbstractFireballEntity) entity);
                fireball.powerX = x;
                fireball.powerY = y;
                fireball.powerZ = z;
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
