package com.LubieKakao1212.opencu.network.packet.dispenser;

import com.LubieKakao1212.opencu.block.entity.BlockEntityOmniDispenser;
import com.LubieKakao1212.opencu.network.IOCUPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateDispenserPacket implements IOCUPacket {

    private BlockPos position;
    private ItemStack newDispenser;

    public UpdateDispenserPacket() { }

    public UpdateDispenserPacket(BlockPos position, ItemStack newDispenser) {
        this.position = position;
        this.newDispenser = newDispenser;
    }

    public static UpdateDispenserPacket fromBytes(FriendlyByteBuf buf) {
        return new UpdateDispenserPacket(
                new BlockPos(
                        buf.readInt(),
                        buf.readInt(),
                        buf.readInt()
                ),
                buf.readItem()
        );
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(position.getX());
        buf.writeInt(position.getY());
        buf.writeInt(position.getZ());
        buf.writeItem(newDispenser);
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
