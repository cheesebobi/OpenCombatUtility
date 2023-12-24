package com.LubieKakao1212.opencu.network.packet.dispenser;

import com.LubieKakao1212.opencu.block.entity.BlockEntityOmniDispenser;
import com.LubieKakao1212.opencu.network.IOCUPacket;
import com.LubieKakao1212.opencu.proxy.Proxy;
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

    private boolean hardSet;

    public UpdateDispenserAimPacket() { }

    public UpdateDispenserAimPacket(BlockPos position, Quaterniond aim, boolean hardSet) {
        this.position = position;
        this.aim = aim;
        this.hardSet = hardSet;
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

        buf.writeBoolean(hardSet);
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
                ),
                buf.readBoolean()
        );
    }


    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Level world = Proxy.getLevel();

            BlockEntity te = world.getBlockEntity(position);

            if (te instanceof BlockEntityOmniDispenser) {
                ((BlockEntityOmniDispenser) te).setCurrentAction(aim);
                if(hardSet) {
                    //Sets last aim to aim
                    ((BlockEntityOmniDispenser) te).setCurrentAction(aim);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
