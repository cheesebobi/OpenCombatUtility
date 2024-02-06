package com.LubieKakao1212.opencu.pulse;

import com.LubieKakao1212.qulib.nbt.JomlNBT;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;
import org.joml.Vector3d;

public class PulseData implements INBTSerializable<CompoundTag> {
    public Vector3d direction;
    public double radius;
    public double force;

    public PulseData() {
        this.direction = new Vector3d();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.put("direction", JomlNBT.writeVector3(direction));
        tag.putDouble("radius", radius);
        tag.putDouble("force", force);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        direction = JomlNBT.readVector3(compoundTag, "direction");
        radius = compoundTag.getDouble("radius");
        force = compoundTag.getDouble("force");
    }
}
