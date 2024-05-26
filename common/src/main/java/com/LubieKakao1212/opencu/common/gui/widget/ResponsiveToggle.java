package com.LubieKakao1212.opencu.common.gui.widget;

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

    private final Supplier<Boolean> state;
    private final Consumer<Boolean> onPressed;

    private final int spriteU, spriteV, toggleOffsetU, hoverOffsetV;

    private final Tooltip activeTooltip;
    private final Tooltip inactiveTooltip;

    public ResponsiveToggle(int x, int y, int width, int height, int spriteU, int spriteV, int toggleOffsetU, int hoverOffsetV, Text message, Supplier<Boolean> state, Consumer<Boolean> onPressed, String tooltipKey) {
        super(x, y, width, height, message);
        this.state = state;
        this.onPressed = onPressed;
        this.spriteU = spriteU;
        this.spriteV = spriteV;
        this.toggleOffsetU = toggleOffsetU;
        this.hoverOffsetV = hoverOffsetV;

        activeTooltip = Tooltip.of(Text.translatable(tooltipKey + activeSuffix));
        inactiveTooltip = Tooltip.of(Text.translatable(tooltipKey + inactiveSuffix));
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        onPressed.accept(state.get());
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        var u = spriteU;
        var v = spriteV;

        var active = this.state.get();

        if(active)
        {
            u += toggleOffsetU;
        }
        if(hovered) {
            v += hoverOffsetV;
        }

        drawTexture(matrices, getX(), getY(), u, v, getWidth(), getHeight());

        setTooltip((active ? activeTooltip : inactiveTooltip));
    }

    @Override
    public void setTooltip(@Nullable Tooltip tooltip) {
        super.setTooltip(tooltip);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }
}
