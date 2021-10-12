package com.LubieKakao1212.opencu.capability.dispenser;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

public class DispenserConfigurable extends DispenserBase {

    private double maxForce;
    private double maxSpread;

    private double force = 0;

    private double spread = 0;

    public DispenserConfigurable(DispenserMappings mappings, float alignmentSpeed, double maxSpread, double maxForce, double baseEnergy) {
        super(mappings, alignmentSpeed, baseEnergy);
        this.maxForce = maxForce;
        this.maxSpread = maxSpread;
    }

    @Override
    public double getSpread() {
        return spread;
    }

    @Override
    public double getForce() {
        return force;
    }

    @Override
    public String trySetForce(double force) {
        if(force > maxForce) {
            return "Force to high.";
        }
        if(force < 0) {
            return "Force to low.";
        }
        this.force = force;
        return null;
    }

    @Override
    public String trySetSpread(double spread) {
        if(spread > maxSpread) {
            return "Spread to high.";
        }
        if(spread < 0) {
            return "Spread to low.";
        }
        this.spread = spread;
        return null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setDouble("force", force);
        nbt.setDouble("spread", spread);

        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        if(nbt.hasKey("force", Constants.NBT.TAG_ANY_NUMERIC)) {
            this.force = nbt.getDouble("force");
        }
        if(nbt.hasKey("force", Constants.NBT.TAG_ANY_NUMERIC)) {
            this.spread = nbt.getDouble("spread");
        }
    }
}
