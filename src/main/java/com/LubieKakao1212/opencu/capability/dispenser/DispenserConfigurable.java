package com.LubieKakao1212.opencu.capability.dispenser;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class DispenserConfigurable extends DispenserBase {

    private double maxForce;
    private double minSpread;
    private double maxSpread;

    private double force = 0;

    private double spread = 0;

    public DispenserConfigurable(DispenserMappings mappings, float alignmentSpeed, double minSpread, double maxSpread, double maxForce, double baseEnergy) {
        super(mappings, alignmentSpeed, baseEnergy);
        this.maxForce = maxForce;
        this.maxSpread = maxSpread;
        this.minSpread = minSpread;
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
    public boolean hasConfigurableForce() {
        return true;
    }

    @Override
    public boolean hasConfigurableSpread() {
        return true;
    }

    @Override
    public double getMaxSpread() {
        return maxSpread;
    }

    @Override
    public double getMinSpread() {
        return minSpread;
    }

    @Override
    public double getMaxForce() {
        return maxForce;
    }

    @Override
    public double getMinForce() {
        return 0;
    }

    @Override
    public String trySetSpread(double spread) {
        if(spread > maxSpread) {
            return "Spread to high.";
        }
        if(spread < minSpread) {
            return "Spread to low.";
        }
        this.spread = spread;
        return null;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();

        nbt.putDouble("force", force);
        nbt.putDouble("spread", spread);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if(nbt.contains("force", Tag.TAG_ANY_NUMERIC)) {
            this.force = nbt.getDouble("force");
        }
        if(nbt.contains("force", Tag.TAG_ANY_NUMERIC)) {
            this.spread = nbt.getDouble("spread");
        }
    }
}
