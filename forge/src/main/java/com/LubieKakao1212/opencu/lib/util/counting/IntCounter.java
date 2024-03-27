package com.lubiekakao1212.opencu.lib.util.counting;

public class IntCounter implements ICounter {

    private int count;

    public IntCounter(int count) {
        this.count = count;
    }

    @Override
    public boolean decrement() {
        return count-- <= 0;
    }

    @Override
    public int count() {
        return count;
    }

}
