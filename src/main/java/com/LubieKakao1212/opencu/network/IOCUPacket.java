package com.LubieKakao1212.opencu.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface IOCUPacket {

    void toBytes(FriendlyByteBuf buf);

    void handle(Supplier<NetworkEvent.Context> ctx);

}
