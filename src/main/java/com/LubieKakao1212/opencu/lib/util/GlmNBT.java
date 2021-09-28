package com.LubieKakao1212.opencu.lib.util;

import glm.quat.Quat;
import glm.vec._2.Vec2;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class GlmNBT {

    public static Quat readQuat(NBTTagList nbt) {
        return new Quat(nbt.getFloatAt(0), nbt.getFloatAt(1), nbt.getFloatAt(2), nbt.getFloatAt(3));
    }

    public static Quat readQuat(NBTTagCompound nbt, String key) {
        return readQuat(nbt.getTagList(key, Constants.NBT.TAG_ANY_NUMERIC));
    }

    public static NBTTagList writeQuat(Quat value) {
        NBTTagList nbt = new NBTTagList();
        nbt.appendTag(new NBTTagFloat(value.x));
        nbt.appendTag(new NBTTagFloat(value.y));
        nbt.appendTag(new NBTTagFloat(value.z));
        nbt.appendTag(new NBTTagFloat(value.w));
        return nbt;
    }


}
