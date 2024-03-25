package com.LubieKakao1212.opencu.network.packet.dispenser;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.forge.block.entity.BlockEntityModularFrameImpl;
import com.LubieKakao1212.opencu.compat.valkyrienskies.VS2SoftUtil;
import com.LubieKakao1212.opencu.network.IOCUPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3d;

import java.util.function.Supplier;

public class RequestDispenserUpdatePacket implements IOCUPacket {

    private BlockPos position;

    public RequestDispenserUpdatePacket() { }

    public RequestDispenserUpdatePacket(BlockPos position) {
        this.position = position;
    }


    public void toBytes(PacketByteBuf buf) {
        buf.writeInt(position.getX());
        buf.writeInt(position.getY());
        buf.writeInt(position.getZ());
    }

    public static RequestDispenserUpdatePacket fromBytes(ByteBuf buf) {
        return new RequestDispenserUpdatePacket(new BlockPos(
                buf.readInt(),
                buf.readInt(),
                buf.readInt()
        ));
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity sender = ctx.get().getSender();
            World level = sender.getWorld();

            if(VS2SoftUtil.getDistanceSqr(level, new Vector3d(sender.getX(), sender.getY(), sender.getY()), new Vector3d(position.getX(), position.getY(), position.getZ())) < (64 * 64)) {
                BlockEntity be = level.getBlockEntity(position);

                if(be instanceof BlockEntityModularFrameImpl) {
                    ((BlockEntityModularFrameImpl) be).sendDispenserUpdateTo(sender);
                }
            }else
            {
                OpenCUMod.LOGGER.warn("Potentially malicious packet received, skipping");
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
