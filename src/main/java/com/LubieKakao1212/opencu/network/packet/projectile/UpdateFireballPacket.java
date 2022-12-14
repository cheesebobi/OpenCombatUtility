package com.LubieKakao1212.opencu.network.packet.projectile;

import com.LubieKakao1212.opencu.network.IOCUPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Level;
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

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }

    public static UpdateFireballPacket fromBytes(FriendlyByteBuf buf) {
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
            Level level =  Minecraft.getInstance().player.level;

            Entity entity = level.getEntity(entityId);

            if(entity instanceof Fireball) {
                Fireball fireball = ((Fireball) entity);
                fireball.xPower = x;
                fireball.yPower = y;
                fireball.zPower = z;
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
