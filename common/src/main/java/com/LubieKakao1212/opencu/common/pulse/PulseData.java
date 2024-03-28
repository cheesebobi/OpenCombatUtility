package com.LubieKakao1212.opencu.common.pulse;

import com.lubiekakao1212.qulib.math.extensions.Vector3dExtensionsKt;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.joml.Vector3d;

public class PulseData {
    public Vector3d direction;
    public double radius;
    public double force;

    public PulseData() {
        this.direction = new Vector3d();
    }

    public NbtCompound serialize() {
        NbtCompound tag = new NbtCompound();
        tag.put("direction", Vector3dExtensionsKt.serializeNBT(direction));
        tag.putDouble("radius", radius);
        tag.putDouble("force", force);
        return tag;
    }

    public void deserialize(NbtCompound compoundTag) {
        direction = Vector3dExtensionsKt.deserializeNBT(new Vector3d(), compoundTag.getList("direction", NbtElement.DOUBLE_TYPE));
        radius = compoundTag.getDouble("radius");
        force = compoundTag.getDouble("force");
    }
}
