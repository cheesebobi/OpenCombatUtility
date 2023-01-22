package com.LubieKakao1212.opencu.network.packet.dispenser;

import com.LubieKakao1212.opencu.block.entity.BlockEntityOmniDispenser;
import com.LubieKakao1212.opencu.network.IOCUPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Quaterniond;

import java.util.function.Supplier;

public class UpdateDispenserAimPacket implements IOCUPacket {

    private BlockPos position;
    private Quaterniond aim;

    public UpdateDispenserAimPacket() { }

    public UpdateDispenserAimPacket(BlockPos position, Quaterniond aim) {
        this.position = position;
        this.aim = aim;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(position.getX());
        buf.writeInt(position.getY());
        buf.writeInt(position.getZ());
        buf.writeDouble(aim.x());
        buf.writeDouble(aim.y());
        buf.writeDouble(aim.z());
        buf.writeDouble(aim.w());
    }

    public static UpdateDispenserAimPacket fromBytes(ByteBuf buf) {
        return new UpdateDispenserAimPacket(
                new BlockPos(
                        buf.readInt(),
                        buf.readInt(),
                        buf.readInt()
                ),
                new Quaterniond(
                        buf.readDouble(),
                        buf.readDouble(),
                        buf.readDouble(),
                        buf.readDouble()
                )
        );
    }


    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Level world = Minecraft.getInstance().player.level;

            BlockEntity te = world.getBlockEntity(position);

            if (te instanceof BlockEntityOmniDispenser) {
                ((BlockEntityOmniDispenser) te).setCurrentAction(aim);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
