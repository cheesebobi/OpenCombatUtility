package com.LubieKakao1212.opencu.forge;

import com.LubieKakao1212.opencu.NetworkUtil;
import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.network.packet.*;
import com.LubieKakao1212.opencu.common.network.packet.dispenser.PacketClientUpdateDispenser;
import com.LubieKakao1212.opencu.common.network.packet.dispenser.PacketClientUpdateDispenserAim;
import com.LubieKakao1212.opencu.common.network.packet.dispenser.PacketServerRequestDispenserUpdate;
import com.LubieKakao1212.opencu.common.network.packet.projectile.PacketClientUpdateFireball;
import com.LubieKakao1212.opencu.forge.packet.PacketSerialize;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Objects;

@Mod.EventBusSubscriber
public class NetworkUtilImpl {

    private static final String version = "2";

    private static SimpleChannel CHANNEL;

    public static void init() {

        CHANNEL = NetworkRegistry.ChannelBuilder.named(new Identifier(OpenCUModCommon.MODID, "network"))
                .networkProtocolVersion(() -> version)
                .clientAcceptedVersions(version::equals)
                .serverAcceptedVersions(version::equals)
                .simpleChannel();

        int id = 0;
        CHANNEL.messageBuilder(PacketClientPlayerAddVelocity.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketSerialize::toBytes)
                .decoder(PacketSerialize.ClientPlayerAddVelocity::fromBytes)
                .consumerMainThread((msg, ctx) -> PacketHandlersClient.handle(msg))
                .add();

        CHANNEL.messageBuilder(PacketClientPlayerScaleVelocity.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketSerialize::toBytes)
                .decoder(PacketSerialize.ClientPlayerScaleVelocity::fromBytes)
                .consumerMainThread((msg, ctx) -> PacketHandlersClient.handle(msg))
                .add();


        CHANNEL.messageBuilder(PacketClientUpdateFireball.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketSerialize::toBytes)
                .decoder(PacketSerialize.ClientUpdateFireball::fromBytes)
                .consumerMainThread((msg, ctx) -> PacketHandlersClient.handle(msg))
                .add();

        CHANNEL.messageBuilder(PacketServerRequestDispenserUpdate.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(PacketSerialize::toBytes)
                .decoder(PacketSerialize.ServerRequestDispenserUpdate::fromBytes)
                .consumerMainThread((msg, ctx) -> PacketHandlersServer.handle(msg, Objects.requireNonNull(ctx.get().getSender())))
                .add();

        CHANNEL.messageBuilder(PacketClientUpdateDispenserAim.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketSerialize::toBytes)
                .decoder(PacketSerialize.ClientUpdateDispenserAim::fromBytes)
                .consumerMainThread((msg, ctx) -> PacketHandlersClient.handle(msg))
                .add();

        //main dispenser update packet
        CHANNEL.messageBuilder(PacketClientUpdateDispenser.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketSerialize::toBytes)
                .decoder(PacketSerialize.ClientUpdateDispenser::fromBytes)
                .consumerMainThread((msg, ctx) -> PacketHandlersClient.handle(msg))
                .add();

        //Repulsor pulse animation trigger
        CHANNEL.messageBuilder(PacketClientRepulsorPulse.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketSerialize::toBytes)
                .decoder(PacketSerialize.ClientRepulsorPulse::fromBytes)
                .consumerMainThread((msg, ctx) -> PacketHandlersClient.handle(msg))
                .add();

//        //client to server request packet
//        CHANNEL.messageBuilder(com.LubieKakao1212.opencu.network.packet.dispenser.UpdateDispenserPacket.FromClient.class, id++, NetworkDirection.PLAY_TO_SERVER)
//                .encoder(PacketSerialize::toBytes)
//                .decoder(PacketSerialize.ClientPlayerAddVelocity::fromBytes)
//                .consumerMainThread((msg, ctx) -> PacketClientPlayerAddVelocity.execute(msg))
//                .add();
    }

//    public static <T extends Record> void enqueueEntityUpdate(T message, Entity target, int delay) {
//        messages.add(new EntityMessage<>(message, delay, target));
//    }

    public static <T extends Record> void sendToAllTracking(T message, ServerWorld world, BlockPos pos) {
        sendToAllTracking(message, world.getWorldChunk(pos));
    }

    public static <T extends Record> void sendToAllTracking(T message, WorldChunk chunk) {
        CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), message);
    }

    public static <T extends Record> void sendToAllTracking(T message, Entity entity) {
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), message);
    }

    public static <T extends Record> void sendToServer(T packet) {
        CHANNEL.sendToServer(packet);
    }

    public static <T extends Record> void sendToPlayer(T packet, ServerPlayerEntity player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public static void serverTick(TickEvent.ServerTickEvent event) {
        NetworkUtil.tick();
    }
}
