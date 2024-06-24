package com.LubieKakao1212.opencu.common.gui.widget;

import com.LubieKakao1212.opencu.common.util.Direction2d;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mutable;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class FillableBarWidget extends ClickableWidget {

    private final FillDirection fillDirection;
    private final int u, v;

    private Supplier<Float> state;

    public FillableBarWidget(int x, int y, int width, int height, int u, int v, FillDirection direction, Supplier<Float> state) {
        super(x, y, width, height, Text.empty());

        this.fillDirection = direction;
        this.u = u;
        this.v = v;

        this.state = state;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        var progress = state.get();

        var w = getWidth();
        var h = getHeight();

        var x = getX();
        var y = getY();

        var x1 = (int)fillDirection.x.get(x, w, progress);
        var y1 = (int)fillDirection.y.get(y, h, progress);

        var u1 = (int)fillDirection.x.get(u, w, progress);
        var v1 = (int)fillDirection.y.get(v, h, progress);

        var w1 = (int)Math.ceil(fillDirection.w.get(x, w, progress));
        var h1 = (int)Math.ceil(fillDirection.h.get(y, h, progress));

        drawTexture(matrices, x1, y1, u1, v1, w1, h1);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

    public enum FillDirection {
        UP(true, false),
        DOWN(true, true),
        LEFT(false, true),
        RIGHT(false, false);

        public final CoordinateFunction x;
        public final CoordinateFunction y;

        public final CoordinateFunction w;
        public final CoordinateFunction h;

        FillDirection(boolean vertical, boolean inverted) {
            this.x = CoordinateFunction.coord(!vertical && inverted);
            this.y = CoordinateFunction.coord(vertical && inverted);
            this.w = CoordinateFunction.size(!vertical);
            this.h = CoordinateFunction.size(vertical);
        }
    }

    @FunctionalInterface
    public interface CoordinateFunction {
        float get(int v, int size, float progress);

        static float identity(int v, int size, float progress) {
            return v;
        }

        static float inverted(int v, int size, float progress) {
            return v + size * (1f - progress);
        }

        static float size(int v, int size, float progress) {
            return size;
        }

        static float fill(int v, int size, float progress) {
            return size * progress;
        }

        static CoordinateFunction coord(boolean invert) {
            return invert ? CoordinateFunction::inverted : CoordinateFunction::identity;
        }

        static CoordinateFunction size(boolean move) {
            return move ? CoordinateFunction::fill : CoordinateFunction::size;
        }

    }
}
