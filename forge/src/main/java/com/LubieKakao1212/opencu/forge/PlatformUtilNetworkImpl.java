package com.lubiekakao1212.opencu.forge;

import com.lubiekakao1212.opencu.common.OpenCUModCommon;
import com.lubiekakao1212.opencu.common.network.packet.PacketClientPlayerAddVelocity;
import com.lubiekakao1212.opencu.common.network.packet.PacketClientPlayerScaleVelocity;
import com.lubiekakao1212.opencu.common.network.packet.dispenser.PacketClientUpdateDispenser;
import com.lubiekakao1212.opencu.common.network.packet.dispenser.PacketClientUpdateDispenserAim;
import com.lubiekakao1212.opencu.common.network.packet.dispenser.PacketServerRequestDispenserUpdate;
import com.lubiekakao1212.opencu.common.network.packet.projectile.PacketClientUpdateFireball;
import com.lubiekakao1212.opencu.forge.packet.PacketSerialize;
import com.lubiekakao1212.opencu.lib.util.counting.CounterList;
import com.lubiekakao1212.opencu.lib.util.counting.ICounter;
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

import java.util.Objects;

@Mod.EventBusSubscriber
public class PlatformUtilNetworkImpl {

    private static final String version = "2";

    private static SimpleChannel CHANNEL;

    private static final CounterList<DelayedMessage<?>> messages = new CounterList<>();

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
                .consumerMainThread((msg, ctx) -> PacketClientPlayerAddVelocity.execute(msg))
                .add();

        CHANNEL.messageBuilder(PacketClientPlayerScaleVelocity.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketSerialize::toBytes)
                .decoder(PacketSerialize.ClientPlayerScaleVelocity::fromBytes)
                .consumerMainThread((msg, ctx) -> PacketClientPlayerScaleVelocity.execute(msg))
                .add();


        CHANNEL.messageBuilder(PacketClientUpdateFireball.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketSerialize::toBytes)
                .decoder(PacketSerialize.ClientUpdateFireball::fromBytes)
                .consumerMainThread((msg, ctx) -> PacketClientUpdateFireball.execute(msg))
                .add();

        CHANNEL.messageBuilder(PacketServerRequestDispenserUpdate.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(PacketSerialize::toBytes)
                .decoder(PacketSerialize.ServerRequestDispenserUpdate::fromBytes)
                .consumerMainThread((msg, ctx) -> PacketServerRequestDispenserUpdate.execute(msg, Objects.requireNonNull(ctx.get().getSender())))
                .add();

        CHANNEL.messageBuilder(PacketClientUpdateDispenserAim.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketSerialize::toBytes)
                .decoder(PacketSerialize.ClientUpdateDispenserAim::fromBytes)
                .consumerMainThread((msg, ctx) -> PacketClientUpdateDispenserAim.execute(msg))
                .add();

        //main dispenser update packet
        CHANNEL.messageBuilder(PacketClientUpdateDispenser.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PacketSerialize::toBytes)
                .decoder(PacketSerialize.ClientUpdateDispenser::fromBytes)
                .consumerMainThread((msg, ctx) -> PacketClientUpdateDispenser.execute(msg))                .add();

//        //client to server request packet
//        CHANNEL.messageBuilder(com.LubieKakao1212.opencu.network.packet.dispenser.UpdateDispenserPacket.FromClient.class, id++, NetworkDirection.PLAY_TO_SERVER)
//                .encoder(PacketSerialize::toBytes)
//                .decoder(PacketSerialize.ClientPlayerAddVelocity::fromBytes)
//                .consumerMainThread((msg, ctx) -> PacketClientPlayerAddVelocity.execute(msg))
//                .add();
    }

    public static <T> void enqueueEntityUpdate(T message, Entity target, int delay) {
        messages.add(new EntityMessage<>(message, delay, target));
    }

    public static <T> void sendToAllTracking(T message, World world, BlockPos pos) {
        sendToAllTracking(message, world.getWorldChunk(pos));
    }

    public static <T> void sendToAllTracking(T message, WorldChunk chunk) {
        CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), message);
    }

    public static <T> void sendToAllTracking(T message, Entity entity) {
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), message);
    }

    public static <T> void sendToServer(T packet) {
        CHANNEL.sendToServer(packet);
    }

    public static <T> void sendToPlayer(T packet, ServerPlayerEntity player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }


    @SubscribeEvent
    @SuppressWarnings("unused")
    public static void serverTick(TickEvent.ServerTickEvent event) {
        messages.tick();
    }

    private static abstract class DelayedMessage<T> implements ICounter {

        protected T message;
        private int delay;

        public DelayedMessage(T message, int delay) {
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

    private static class EntityMessage<T> extends DelayedMessage<T> {

        private final Entity target;

        public EntityMessage(T message, int delay, Entity target) {
            super(message, delay);
            this.target = target;
        }

        @Override
        protected void send() {
            sendToAllTracking(message, target);
        }
    }
}
