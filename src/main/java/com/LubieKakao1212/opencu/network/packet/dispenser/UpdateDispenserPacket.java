package com.LubieKakao1212.opencu.network.packet.dispenser;

import com.LubieKakao1212.opencu.block.entity.BlockEntityOmniDispenser;
import com.LubieKakao1212.opencu.network.IOCUPacket;
import com.LubieKakao1212.opencu.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class UpdateDispenserPacket implements IOCUPacket {

    protected BlockPos position;
    protected ItemStack newDispenser;

    public UpdateDispenserPacket(BlockPos position, ItemStack newDispenser) {
        this.position = position;
        this.newDispenser = newDispenser;
    }


    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(position.getX());
        buf.writeInt(position.getY());
        buf.writeInt(position.getZ());
        buf.writeItem(newDispenser);
    }


    public static class FromServer extends UpdateDispenserPacket {

        public FromServer(BlockPos position, ItemStack newDispenser) {
            super(position, newDispenser);
        }

        public static UpdateDispenserPacket.FromServer fromBytes(FriendlyByteBuf buf) {
            return new UpdateDispenserPacket.FromServer(
                    new BlockPos(
                            buf.readInt(),
                            buf.readInt(),
                            buf.readInt()
                    ),
                    buf.readItem()
            );
        }

        @Override
        public void handle(Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                Level level =  Minecraft.getInstance().player.level;

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

        public static UpdateDispenserPacket.FromClient fromBytes(FriendlyByteBuf buf) {
            return new UpdateDispenserPacket.FromClient(
                    new BlockPos(
                            buf.readInt(),
                            buf.readInt(),
                            buf.readInt()
                    ),
                    buf.readItem()
            );
        }

        @Override
        public void handle(Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                NetworkHandler.sendToAllTracking(this, ctx.get().getSender().level, position);
            });
        }
    }
}
