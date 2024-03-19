package com.LubieKakao1212.opencu.network;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.lib.util.counting.CounterList;
import com.LubieKakao1212.opencu.lib.util.counting.ICounter;
import com.LubieKakao1212.opencu.network.packet.PlayerAddVelocityPacket;
import com.LubieKakao1212.opencu.network.packet.PlayerScaleVelocityPacket;
import com.LubieKakao1212.opencu.network.packet.dispenser.RequestDispenserUpdatePacket;
import com.LubieKakao1212.opencu.network.packet.dispenser.UpdateDispenserAimPacket;
import com.LubieKakao1212.opencu.network.packet.dispenser.UpdateDispenserPacket;
import com.LubieKakao1212.opencu.network.packet.projectile.UpdateFireballPacket;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod.EventBusSubscriber
public class NetworkHandler {

    private static final String version = "1";

    private static SimpleChannel CHANNEL;

    private static final CounterList<DelayedMessage> messages = new CounterList<DelayedMessage>();

    public static void init() {

        CHANNEL = NetworkRegistry.ChannelBuilder.named(new Identifier( OpenCUMod.MODID, "network" ))
                .networkProtocolVersion(() -> version)
                .clientAcceptedVersions(version::equals)
                .serverAcceptedVersions(version::equals)
                .simpleChannel();

        int id = 0;
        CHANNEL.messageBuilder(PlayerAddVelocityPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PlayerAddVelocityPacket::toBytes)
                .decoder(PlayerAddVelocityPacket::fromBytes)
                .consumerNetworkThread(PlayerAddVelocityPacket::handle)
                .add();

        CHANNEL.messageBuilder(PlayerScaleVelocityPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PlayerScaleVelocityPacket::toBytes)
                .decoder(PlayerScaleVelocityPacket::fromBytes)
                .consumerNetworkThread(PlayerScaleVelocityPacket::handle)
                .add();


        CHANNEL.messageBuilder(UpdateFireballPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(UpdateFireballPacket::toBytes)
                .decoder(UpdateFireballPacket::fromBytes)
                .consumerNetworkThread(UpdateFireballPacket::handle)
                .add();

        CHANNEL.messageBuilder(RequestDispenserUpdatePacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(RequestDispenserUpdatePacket::toBytes)
                .decoder(RequestDispenserUpdatePacket::fromBytes)
                .consumerNetworkThread(RequestDispenserUpdatePacket::handle)
                .add();

        CHANNEL.messageBuilder(UpdateDispenserAimPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(UpdateDispenserAimPacket::toBytes)
                .decoder(UpdateDispenserAimPacket::fromBytes)
                .consumerNetworkThread(UpdateDispenserAimPacket::handle)
                .add();

        //main dispenser update packet
        CHANNEL.messageBuilder(UpdateDispenserPacket.FromServer.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(UpdateDispenserPacket.FromServer::toBytes)
                .decoder(UpdateDispenserPacket.FromServer::fromBytes)
                .consumerNetworkThread(UpdateDispenserPacket.FromServer::handle)
                .add();

        //client to server request packet
        CHANNEL.messageBuilder(UpdateDispenserPacket.FromClient.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(UpdateDispenserPacket.FromClient::toBytes)
                .decoder(UpdateDispenserPacket.FromClient::fromBytes)
                .consumerNetworkThread(UpdateDispenserPacket.FromClient::handle)
                .add();
    }

    public static void sendToALl(IOCUPacket message) {
        CHANNEL.send(PacketDistributor.ALL.noArg(), message);
    }

    public static void sendToAllTracking(IOCUPacket message, World level, BlockPos pos) {
        sendToAllTracking(message, level.getWorldChunk(pos));
    }

    public static void sendToAllTracking(IOCUPacket message, WorldChunk chunk) {
        CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), message);
    }

    public static void sendToAllTracking(IOCUPacket message, Entity entity) {
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), message);
    }

    public static void sendTo(ServerPlayerEntity player, IOCUPacket message) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static void sendToServer(IOCUPacket message) {
        CHANNEL.sendToServer(message);
    }

    public static void enqueueEntityUpdate(IOCUPacket message, Entity target, int delay) {
        messages.add(new EntityMessage(message, delay, target));
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public static void serverTick(TickEvent.ServerTickEvent event) {
        messages.tick();
    }


    private static abstract class DelayedMessage implements ICounter {

        protected IOCUPacket message;
        private int delay;

        public DelayedMessage(IOCUPacket message, int delay) {
            this.message = message;
            this.delay = delay;
        }

        @Override
        public boolean decrement() {
            if(delay-- <= 0) {
                send();
                return true;
            }
            return false;
        }

        @Override
        public int count() {
            return delay;
        }

        protected abstract void send();
    }

    private static class EntityMessage extends DelayedMessage {

        private final Entity target;

        public EntityMessage(IOCUPacket message, int delay, Entity target) {
            super(message, delay);
            this.target = target;
        }

        @Override
        protected void send() {
            sendToAllTracking(message, target);
        }
    }
}
