package com.lubiekakao1212.opencu.forge.packet;

import com.lubiekakao1212.opencu.common.network.packet.PacketClientPlayerAddVelocity;
import com.lubiekakao1212.opencu.common.network.packet.PacketClientPlayerScaleVelocity;
import com.lubiekakao1212.opencu.common.network.packet.dispenser.PacketClientUpdateDispenser;
import com.lubiekakao1212.opencu.common.network.packet.dispenser.PacketClientUpdateDispenserAim;
import com.lubiekakao1212.opencu.common.network.packet.dispenser.PacketServerRequestDispenserUpdate;
import com.lubiekakao1212.opencu.common.network.packet.projectile.PacketClientUpdateFireball;
import net.minecraft.network.PacketByteBuf;

public class PacketSerialize {

    public static void toBytes(PacketClientPlayerAddVelocity packet, PacketByteBuf buffer) {
        buffer.writeInt(packet.x());
        buffer.writeInt(packet.y());
        buffer.writeInt(packet.z());
    }

    public static void toBytes(PacketClientPlayerScaleVelocity packet, PacketByteBuf buffer) {
        buffer.writeFloat(packet.scale());
    }

    public static void toBytes(PacketClientUpdateDispenser packet, PacketByteBuf buffer) {
        buffer.writeBlockPos(packet.position());
        buffer.writeItemStack(packet.newDispenser());
    }

    public static void toBytes(PacketClientUpdateDispenserAim packet, PacketByteBuf buffer) {
        buffer.writeBoolean(packet.hard());
        buffer.writeBlockPos(packet.position());
        buffer.writeFloat(packet.qx());
        buffer.writeFloat(packet.qy());
        buffer.writeFloat(packet.qz());
        buffer.writeFloat(packet.qw());
    }

    public static void toBytes(PacketServerRequestDispenserUpdate packet, PacketByteBuf buffer) {
        buffer.writeBlockPos(packet.position());
    }

    public static void toBytes(PacketClientUpdateFireball packet, PacketByteBuf buffer) {
        buffer.writeInt(packet.entityId());
        buffer.writeFloat(packet.powX());
        buffer.writeFloat(packet.powY());
        buffer.writeFloat(packet.powZ());
    }

    public static class ClientPlayerAddVelocity {
        public static PacketClientPlayerAddVelocity fromBytes(PacketByteBuf buffer) {
            return new PacketClientPlayerAddVelocity(buffer.readInt(), buffer.readInt(), buffer.readInt());
        }
    }

    public static class ClientPlayerScaleVelocity {
        public static PacketClientPlayerScaleVelocity fromBytes(PacketByteBuf buffer) {
            return new PacketClientPlayerScaleVelocity(buffer.readFloat());
        }
    }

    public static class ClientUpdateDispenser {
        public static PacketClientUpdateDispenser fromBytes(PacketByteBuf buffer) {
            return new PacketClientUpdateDispenser(buffer.readBlockPos(), buffer.readItemStack());
        }
    }

    public static class ClientUpdateDispenserAim {
        public static PacketClientUpdateDispenserAim fromBytes(PacketByteBuf buffer) {
            return new PacketClientUpdateDispenserAim(buffer.readBoolean(), buffer.readBlockPos(),
                    buffer.readFloat(),
                    buffer.readFloat(),
                    buffer.readFloat(),
                    buffer.readFloat());
        }
    }

    public static class ServerRequestDispenserUpdate {
        public static PacketServerRequestDispenserUpdate fromBytes(PacketByteBuf buffer) {
            return new PacketServerRequestDispenserUpdate(buffer.readBlockPos());
        }
    }

    public static class ClientUpdateFireball {
        public static PacketClientUpdateFireball fromBytes(PacketByteBuf buffer) {
            return new PacketClientUpdateFireball(buffer.readInt(),
                    buffer.readFloat(),
                    buffer.readFloat(),
                    buffer.readFloat());
        }
    }


}
