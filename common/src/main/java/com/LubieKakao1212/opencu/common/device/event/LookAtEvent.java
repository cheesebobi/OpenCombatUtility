package com.LubieKakao1212.opencu.common.device.event;

import net.minecraft.entity.ai.goal.BreakDoorGoal;
import org.joml.Vector3d;

public class LookAtEvent implements IEventData {

    public final Vector3d target;

    public final boolean isWorldSpace;

    public LookAtEvent(Vector3d target, boolean isWorldSpace) {
        this.target = target;
        this.isWorldSpace = isWorldSpace;
    }

}
