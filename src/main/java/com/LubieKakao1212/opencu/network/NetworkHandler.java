package com.LubieKakao1212.opencu.network;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.lib.util.counting.CounterList;
import com.LubieKakao1212.opencu.lib.util.counting.ICounter;
import com.LubieKakao1212.opencu.network.packet.EntityAddVelocityPacket;
import com.LubieKakao1212.opencu.network.packet.dispenser.RequestDispenserUpdatePacket;
import com.LubieKakao1212.opencu.network.packet.dispenser.UpdateDispenserAimPacket;
import com.LubieKakao1212.opencu.network.packet.dispenser.UpdateDispenserPacket;
import com.LubieKakao1212.opencu.network.packet.projectile.UpdateFireballPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

@Mod.EventBusSubscriber
public class NetworkHandler {

    private static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(OpenCUMod.MODID);

    //public static List<DelayedMessage> messages = new ArrayList<>();
    public static final CounterList<DelayedMessage> messages = new CounterList<DelayedMessage>();

    public static void init() {
        int id = 0;
        INSTANCE.registerMessage(EntityAddVelocityPacket.Handler.class, EntityAddVelocityPacket.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(UpdateDispenserPacket.Handler.class, UpdateDispenserPacket.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(UpdateDispenserAimPacket.Handler.class, UpdateDispenserAimPacket.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(UpdateFireballPacket.Handler.class, UpdateFireballPacket.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(RequestDispenserUpdatePacket.Handler.class, RequestDispenserUpdatePacket.class, id++, Side.SERVER);
    }

    public static void sendToALl(IMessage message) {
        INSTANCE.sendToAll(message);
    }

    public static void sendToAllTracking(IMessage message, BlockPos pos, int dimension) {
        INSTANCE.sendToAllTracking(message, new NetworkRegistry.TargetPoint(dimension, pos.getX(), pos.getY(), pos.getZ(), -1));
    }

    public static void sendToAllTracking(IMessage message, Entity entity) {
        INSTANCE.sendToAllTracking(message, entity);
    }

    public static void sendTo(EntityPlayerMP player, IMessage message) {
        INSTANCE.sendTo(message, player);
    }

    public static void sendToServer(IMessage message) {
        INSTANCE.sendToServer(message);
    }

    public static void enqueueEntityUpdate(IMessage message, Entity target, int delay) {
        messages.add(new EntityMessage(message, delay, target));
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public static void serverTick(TickEvent.ServerTickEvent event) {
        messages.tick();
    }

    private static abstract class DelayedMessage implements ICounter {

        protected IMessage message;
        private int delay;

        public DelayedMessage(IMessage message, int delay) {
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

        private Entity target;

        public EntityMessage(IMessage message, int delay, Entity target) {
            super(message, delay);
            this.target = target;
        }

        @Override
        protected void send() {
            sendToAllTracking(message, target);
        }
    }
}
