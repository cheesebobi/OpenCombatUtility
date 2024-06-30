package com.LubieKakao1212.opencu.common.device.event.data;

import com.LubieKakao1212.opencu.common.device.event.data.IEventData;
import com.lubiekakao1212.qulib.math.Aim;

public class SetAimEvent implements IEventData {

    public final Aim aim;

    public SetAimEvent(Aim aim) {
        this.aim = aim;
    }

}
