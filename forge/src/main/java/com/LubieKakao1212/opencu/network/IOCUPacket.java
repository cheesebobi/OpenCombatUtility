package com.LubieKakao1212.opencu.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface IOCUPacket {

    void toBytes(PacketByteBuf buf);

    void handle(Supplier<NetworkEvent.Context> ctx);

}
