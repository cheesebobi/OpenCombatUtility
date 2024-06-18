package com.LubieKakao1212.opencu.common.util;

public class Pulser {

    private long timer;

    private final long delay;

    public Pulser(long delay) {
        this.delay = delay;
    }

    public boolean shouldActivate() {
        return (timer + 1) == delay;
    }

    public boolean tick(boolean active) {
        if(active) {
            timer++;
        } else {
            timer = 0;
        }

        return shouldActivate();
    }

}
