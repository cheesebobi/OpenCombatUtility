package com.LubieKakao1212.opencu.network.packet.dispenser;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.block.entity.BlockEntityOmniDispenser;
import com.LubieKakao1212.opencu.compat.valkyrienskies.VS2SoftUtil;
import com.LubieKakao1212.opencu.network.IOCUPacket;
import com.LubieKakao1212.opencu.network.NetworkHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3d;

import java.util.function.Supplier;

public abstract class UpdateDispenserPacket implements IOCUPacket {

    protected BlockPos position;
    protected ItemStack newDispenser;

    public UpdateDispenserPacket(BlockPos position, ItemStack newDispenser) {
        this.position = position;
        this.newDispenser = newDispenser;
    }


    @Override
    public void toBytes(PacketByteBuf buf) {
        buf.writeInt(position.getX());
        buf.writeInt(position.getY());
        buf.writeInt(position.getZ());
        buf.writeItemStack(newDispenser);
    }


    public static class FromServer extends UpdateDispenserPacket {

        public FromServer(BlockPos position, ItemStack newDispenser) {
            super(position, newDispenser);
        }

        public static UpdateDispenserPacket.FromServer fromBytes(PacketByteBuf buf) {
            return new UpdateDispenserPacket.FromServer(
                    new BlockPos(
                            buf.readInt(),
                            buf.readInt(),
                            buf.readInt()
                    ),
                    buf.readItemStack()
            );
        }

        @Override
        public void handle(Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                World level =  MinecraftClient.getInstance().player.world;

                BlockEntity te = level.getBlockEntity(position);

                if(te instanceof BlockEntityOmniDispenser) {
                    ((BlockEntityOmniDispenser) te).setCurrentDispenserItem(newDispenser);
                }
            });
        }

    }

    public static class FromClient extends UpdateDispenserPacket {

        public FromClient(BlockPos position, ItemStack newDispenser) {
            super(position, newDispenser);
        }

        public static UpdateDispenserPacket.FromClient fromBytes(PacketByteBuf buf) {
            return new UpdateDispenserPacket.FromClient(
                    new BlockPos(
                            buf.readInt(),
                            buf.readInt(),
                            buf.readInt()
                    ),
                    buf.readItemStack()
            );
        }

        @Override
        public void handle(Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity sender = ctx.get().getSender();
                if(VS2SoftUtil.getDistanceSqr(sender.world, new Vector3d(sender.getX(), sender.getY(), sender.getY()), new Vector3d(position.getX(), position.getY(), position.getZ())) < (64 * 64))
                {
                    NetworkHandler.sendToAllTracking(this, sender.world, position);
                }else
                {
                    OpenCUMod.LOGGER.warn("Potentially malicious packet received, skipping");
                }
            });
        }
    }
}
