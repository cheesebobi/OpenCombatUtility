package com.LubieKakao1212.opencu.pulse;

@FunctionalInterface
public interface ForceTransformer {

    double transform(double force);

    static ForceTransformer scale(double scale) {
        return (force) -> force * scale;
    }

    static ForceTransformer identity() {
        return (force) -> force;
    }
}
