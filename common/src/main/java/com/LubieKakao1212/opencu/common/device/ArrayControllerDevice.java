package com.LubieKakao1212.opencu.common.device;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.common.device.event.data.ActivateEvent;
import com.LubieKakao1212.opencu.common.device.event.data.IEventData;
import com.LubieKakao1212.opencu.common.device.state.ArrayControllerDeviceState;
import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import com.lubiekakao1212.qulib.math.Aim;
import com.lubiekakao1212.qulib.random.RandomEx;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ArrayControllerDevice implements IFramedDevice {

    private final RandomEx randomEx = new RandomEx();

    /**
     * @param frame
     * @param state
     * @param world
     * @param pos
     * @param aim
     * @param ctx   used to fetch ammo, use energy, and add leftovers
     */
    @Override
    public void activate(BlockEntityModularFrame frame, IDeviceState state, World world, BlockPos pos, Aim aim, BlockEntityModularFrame.ModularFrameContext ctx) {
        frame.getEventDistributor().handleEvent(new ActivateEvent());
    }

    @Override
    public void tick(BlockEntityModularFrame frame, IDeviceState state, World world, BlockPos pos, Aim aim, BlockEntityModularFrame.ModularFrameContext ctx) {
        frame.aimAt(randomEx.nextOnSphere(1));
    }

    @Override
    public double getPitchAlignmentSpeed() {
        return 15;
    }

    @Override
    public double getYawAlignmentSpeed() {
        return 15;
    }

    @Override
    public IDeviceState getNewState() {
        return new ArrayControllerDeviceState();
    }

    @Override
    public void handleEvent(BlockEntityModularFrame frame, IDeviceState state, IEventData data) {
        frame.getEventDistributor().handleEvent(data);
    }
}
