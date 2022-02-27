package com.LubieKakao1212.opencu.lib.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import org.joml.Quaterniond;

public class JomlNBT {

    public static Quaterniond readQuaternion(NBTTagList nbt) {
        return new Quaterniond(nbt.getDoubleAt(0), nbt.getDoubleAt(1), nbt.getDoubleAt(2), nbt.getDoubleAt(3));
    }

    public static Quaterniond readQuaternion(NBTTagCompound nbt, String key) {
        return readQuaternion(nbt.getTagList(key, Constants.NBT.TAG_FLOAT));
    }

    public static NBTTagList writeQuaternion(Quaterniond value) {
        NBTTagList nbt = new NBTTagList();
        nbt.appendTag(new NBTTagDouble(value.x()));
        nbt.appendTag(new NBTTagDouble(value.y()));
        nbt.appendTag(new NBTTagDouble(value.z()));
        nbt.appendTag(new NBTTagDouble(value.w()));
        return nbt;
    }


}
