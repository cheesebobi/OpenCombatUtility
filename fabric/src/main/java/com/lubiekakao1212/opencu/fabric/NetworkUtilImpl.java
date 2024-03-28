package com.LubieKakao1212.opencu.fabric;

import com.LubieKakao1212.opencu.common.network.packet.PacketClientPlayerAddVelocity;
import com.LubieKakao1212.opencu.common.network.packet.PacketClientPlayerScaleVelocity;
import com.LubieKakao1212.opencu.common.network.packet.PacketHandlersServer;
import com.LubieKakao1212.opencu.common.network.packet.dispenser.PacketClientUpdateDispenser;
import com.LubieKakao1212.opencu.common.network.packet.dispenser.PacketClientUpdateDispenserAim;
import com.LubieKakao1212.opencu.common.network.packet.dispenser.PacketServerRequestDispenserUpdate;
import com.LubieKakao1212.opencu.common.network.packet.projectile.PacketClientUpdateFireball;
import com.LubieKakao1212.opencu.registry.CUIds;
import io.wispforest.owo.network.OwoNetChannel;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import static com.LubieKakao1212.opencu.common.network.packet.PacketHandlersClient.handle;

public class NetworkUtilImpl {

    private static final OwoNetChannel CHANNEL = OwoNetChannel.create(CUIds.MAIN);

    public static void init() {
        CHANNEL.registerClientboundDeferred(PacketClientPlayerAddVelocity.class);
        CHANNEL.registerClientboundDeferred(PacketClientPlayerScaleVelocity.class);

        CHANNEL.registerClientboundDeferred(PacketClientUpdateFireball.class);

        CHANNEL.registerClientboundDeferred(PacketClientUpdateDispenser.class);
        CHANNEL.registerClientboundDeferred(PacketClientUpdateDispenserAim.class);

        CHANNEL.registerServerbound(PacketServerRequestDispenserUpdate.class, (pkt, acc) -> PacketHandlersServer.handle(pkt, acc.player()));
    }

    public static void clientInit() {
        CHANNEL.registerClientbound(PacketClientPlayerAddVelocity.class, (pkt, acc) -> handle(pkt));
        CHANNEL.registerClientbound(PacketClientPlayerScaleVelocity.class, (pkt, acc) -> handle(pkt));

        CHANNEL.registerClientbound(PacketClientUpdateFireball.class, (pkt, acc) -> handle(pkt));

        CHANNEL.registerClientbound(PacketClientUpdateDispenser.class, (pkt, acc) -> handle(pkt));
        CHANNEL.registerClientbound(PacketClientUpdateDispenserAim.class, (pkt, acc) -> handle(pkt));
    }

    public static <T extends Record> void sendToAllTracking(T packet, ServerWorld world, BlockPos pos) {
        CHANNEL.serverHandle(world, pos).send(packet);
    }

    public static <T extends Record> void sendToServer(T packet) {
        CHANNEL.clientHandle().send(packet);
    }

    public static <T extends Record> void sendToPlayer(T packet, ServerPlayerEntity player) {
        CHANNEL.serverHandle(player).send(packet);
    }

    public static <T extends Record> void sendToAllTracking(T packet, Entity target) {
        sendToAllTracking(packet, (ServerWorld) target.getWorld(), target.getBlockPos());
    }
}
