package com.LubieKakao1212.opencu.common.util;

public enum Direction2d {
    UP(0, 1),
    DOWN(0, -1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    public final int x;
    public final int y;

    Direction2d(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
