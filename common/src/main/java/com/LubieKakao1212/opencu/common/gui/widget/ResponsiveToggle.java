package com.LubieKakao1212.opencu.common.gui.widget;

import com.mojang.datafixers.types.Func;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import javax.tools.Tool;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ResponsiveToggle extends ClickableWidget {

    private static final String activeSuffix = ".active";
    private static final String inactiveSuffix = ".inactive";

    private final Supplier<Integer> state;
    private final Consumer<Integer> onPressed;

    private final int spriteU, spriteV, toggleOffsetU, hoverOffsetV;

    private final Tooltip[] stateTooltips;
    private final Tooltip defaultTooltip;

    //private final Tooltip defaultTooltip;

    public static ResponsiveToggle dualState(int x, int y, int width, int height, int spriteU, int spriteV, int toggleOffsetU, int hoverOffsetV, Supplier<Boolean> state, Consumer<Boolean> onPressed, String tooltipKey) {
        return new ResponsiveToggle(x, y, width, height, spriteU, spriteV, toggleOffsetU, hoverOffsetV,
                Text.empty(),
                () -> state.get() ? 1 : 0,
                (s) -> onPressed.accept(s > 0),
                Tooltip.of(Text.empty()),
                Tooltip.of(Text.translatable(tooltipKey + inactiveSuffix)),
                Tooltip.of(Text.translatable(tooltipKey + activeSuffix)));
    }

    public static ResponsiveToggle multiState(int x, int y, int width, int height, int spriteU, int spriteV, int toggleOffsetU, int hoverOffsetV, Supplier<Integer> state, Consumer<Integer> onPressed, Tooltip defaultTooltip, Tooltip... tooltips) {
        return new ResponsiveToggle(x, y, width, height, spriteU, spriteV, toggleOffsetU, hoverOffsetV,
                Text.empty(),
                state,
                onPressed,
                defaultTooltip != null ? defaultTooltip : Tooltip.of(Text.empty()),
                tooltips);
    }

    private ResponsiveToggle(int x, int y, int width, int height, int spriteU, int spriteV, int toggleOffsetU, int hoverOffsetV, Text message, Supplier<Integer> state, Consumer<Integer> onPressed, Tooltip defaultTooltip, Tooltip... tooltips) {
        super(x, y, width, height, message);
        this.state = state;
        this.onPressed = onPressed;
        this.spriteU = spriteU;
        this.spriteV = spriteV;
        this.toggleOffsetU = toggleOffsetU;
        this.hoverOffsetV = hoverOffsetV;

        this.defaultTooltip = defaultTooltip;
        this.stateTooltips = tooltips;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        onPressed.accept(state.get());
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        var u = spriteU;
        var v = spriteV;

        var state = this.state.get();

        u += toggleOffsetU * state;

        if(hovered) {
            v += hoverOffsetV;
        }

        drawTexture(matrices, getX(), getY(), u, v, getWidth(), getHeight());

        var tooltip = defaultTooltip;

        if(state >= 0 && state < stateTooltips.length) {
            tooltip = stateTooltips[state];
        }

        setTooltip(tooltip);
    }

    @Override
    public void setTooltip(@Nullable Tooltip tooltip) {
        super.setTooltip(tooltip);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }
}
