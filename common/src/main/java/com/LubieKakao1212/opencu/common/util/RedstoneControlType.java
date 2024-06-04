package com.LubieKakao1212.opencu.common.util;

import net.minecraft.text.Text;

public enum RedstoneControlType {
    LOW(0, Text.translatable("info.opencu.redstone.low")),
    HIGH(1, Text.translatable("info.opencu.redstone.high")),
    PULSE(2, Text.translatable("info.opencu.redstone.pulse")),
    DISABLED(3, Text.translatable("info.opencu.redstone.disabled"));

    public final int order;

    public final Text tooltip;

    RedstoneControlType(int order, Text tooltip) {
        this.order = order;
        this.tooltip = tooltip;
    }

    public RedstoneControlType cycleNext() {
        return switch (this) {
            case LOW -> HIGH;
            case HIGH -> PULSE;
            case PULSE -> DISABLED;
            case DISABLED -> LOW;
        };
    }

    public static RedstoneControlType fromIndex(int index) {
        return switch (index) {
            case 0 -> LOW;
            case 1 -> HIGH;
            case 2 -> PULSE;
            case 3 -> DISABLED;
            default -> throw new IllegalArgumentException("Index must be non-negative and less than 4");
        };
    }
}
