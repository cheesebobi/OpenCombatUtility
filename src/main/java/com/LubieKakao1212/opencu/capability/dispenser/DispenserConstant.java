package com.LubieKakao1212.opencu.capability.dispenser;

public class DispenserConstant extends DispenserBase {

    private double constantSpread;
    private double constantForce;

    public DispenserConstant(DispenserMappings mappings, float alignmentSpeed, double constantSpread, double constantForce, double baseEnergy) {
        super(mappings, alignmentSpeed, baseEnergy);
        this.constantForce = constantForce;
        this.constantSpread = constantSpread;
    }

    @Override
    public double getSpread() {
        return constantSpread;
    }

    @Override
    public double getForce() {
        return constantForce;
    }

    @Override
    public boolean hasConfigurableForce() {
        return false;
    }

    @Override
    public boolean hasConfigurableSpread() {
        return false;
    }

    @Override
    public double getMaxSpread() {
        return constantSpread;
    }

    @Override
    public double getMinSpread() {
        return constantSpread;
    }

    @Override
    public double getMaxForce() {
        return constantForce;
    }

    @Override
    public double getMinForce() {
        return constantForce;
    }
}
