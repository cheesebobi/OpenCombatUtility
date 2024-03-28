package com.LubieKakao1212.opencu;

import com.LubieKakao1212.opencu.common.util.counting.CounterList;
import com.LubieKakao1212.opencu.common.util.counting.ICounter;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class NetworkUtil {

    private static final CounterList<DelayedMessage<?>> messages = new CounterList<>();

    @ExpectPlatform
    public static <T extends Record> void sendToAllTracking(T packet, ServerWorld world, BlockPos pos) {

    }

    @ExpectPlatform
    public static <T extends Record> void sendToServer(T packet) {

    }

    @ExpectPlatform
    public static <T extends Record> void sendToPlayer(T packet, ServerPlayerEntity player) {

    }

    @ExpectPlatform
    public static <T extends Record> void sendToAllTracking(T message, Entity target) {

    }

    public static <T extends Record> void enqueueEntityPacket(T message, Entity target, int delay) {
        messages.add(new EntityMessage<>(message, delay, target));
    }

    public static void tick() {
        messages.tick();
    }


    private static abstract class DelayedMessage<T extends Record> implements ICounter {

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

    private static class EntityMessage<T extends Record> extends DelayedMessage<T> {

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
