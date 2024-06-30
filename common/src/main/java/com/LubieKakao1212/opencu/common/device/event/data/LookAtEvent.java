package com.LubieKakao1212.opencu.common.device.event.data;

import com.LubieKakao1212.opencu.common.device.event.data.IEventData;
import org.joml.Vector3d;

public class LookAtEvent implements IEventData {

    public final Vector3d target;

    public final boolean isWorldSpace;

    public LookAtEvent(Vector3d target, boolean isWorldSpace) {
        this.target = target;
        this.isWorldSpace = isWorldSpace;
    }

}
