package com.LubieKakao1212.opencu.pulse;

import com.LubieKakao1212.qulib.libs.joml.Vector3d;
import com.LubieKakao1212.qulib.util.joml.Vector3dUtil;
import net.minecraft.world.entity.Entity;

public class StasisPulse extends EntityPulse {

    private static final float epsilon = 0.001f;
    private static final float epsilonSqr = epsilon * epsilon;

    @Override
    public void setBaseForce(double baseForce) {
        super.setBaseForce(
                Math.min(
                        Math.max(
                                Math.abs(baseForce),
                                0.),
                        1.));
    }

    @Override
    public void execute() {
        filter();

        for(Entity e : entityList) {
            Vector3d movement = Vector3dUtil.of(e.getDeltaMovement());

            if(movement.lengthSquared() < epsilonSqr) {
                addVelocity(e, movement.mul(-1.));
                continue;
            }

            addVelocity(e, movement.mul(-this.baseForce));
        }
    }
}
