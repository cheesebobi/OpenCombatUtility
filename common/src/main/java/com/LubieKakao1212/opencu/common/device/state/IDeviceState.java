package com.LubieKakao1212.opencu.common.device.state;

import com.LubieKakao1212.opencu.common.peripheral.device.IDeviceApi;
import net.minecraft.nbt.NbtCompound;

public interface IDeviceState {

    /**
     * Returns a cc api for this state instance.
     * Do not call unless CC:Tweaked is present
     * @return
     */
    IDeviceApi getApi();

    NbtCompound serialize();

    void deserialize(NbtCompound nbt);

}
