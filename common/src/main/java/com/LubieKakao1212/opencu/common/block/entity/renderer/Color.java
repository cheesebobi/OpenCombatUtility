package com.lubiekakao1212.opencu.common.block.entity.renderer;

public class Color {

    public float r, g, b, a;

    public Color(float red, float green, float blue, float alpha) {
        this.r = red;
        this.g = green;
        this.b = blue;
        this.a = alpha;
    }

    public static Color lerp(Color a, Color b, float t) {
        return new Color(
                lerp(a.r, b.r, t),
                lerp(a.g, b.g, t),
                lerp(a.b, b.b, t),
                lerp(a.a, b.a, t)
        );
    }

    private static float lerp(float a, float b, float t) {
        return a + t * (b - a);
    }


}
