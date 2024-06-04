package com.LubieKakao1212.opencu.common.gui;

import com.LubieKakao1212.opencu.NetworkUtil;
import com.LubieKakao1212.opencu.common.gui.container.ModularFrameMenu;
import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.gui.widget.ResponsiveToggle;
import com.LubieKakao1212.opencu.common.network.packet.dispenser.PacketServerToggleRequiresLock;
import com.LubieKakao1212.opencu.common.network.packet.generic.PacketServerCycleRedstoneControl;
import com.LubieKakao1212.opencu.common.util.RedstoneControlType;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModularFrameScreen extends HandledScreen<ModularFrameMenu> {

    private static final Identifier mainTexture = new Identifier(OpenCUModCommon.MODID, "textures/gui/omnidispenser_gui.png");

    public ModularFrameScreen(ModularFrameMenu container, PlayerInventory inv, Text titleIn) {
        super(container, inv, titleIn);
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        //var aimLockToggle = new ToggleButtonWidget(161, 7, 10, 10, false);
        //aimLockToggle.setTextureUV(178, 69, 12,12, mainTexture);
        //aimLockToggle.onClick();
        var lockButton = ResponsiveToggle.dualState(
                x + 149, y + 6,
                10, 10,
                188, 68,
                -11,11,
                handler::isRequiresLock,
                (state) -> NetworkUtil.sendToServer(new PacketServerToggleRequiresLock(handler.targetPosition())),
                "info.opencu.frame.gui.lock");
        addDrawableChild(lockButton);

        var redstoneControlButton = ResponsiveToggle.multiState(
                x + 160, y + 6,
                10, 10,
                177, 90,
                11, 11,
                handler::getRedstoneControlTypeIndex,
                (state) -> NetworkUtil.sendToServer(new PacketServerCycleRedstoneControl(handler.targetPosition())),
                Tooltip.of(Text.empty()),
                Tooltip.of(RedstoneControlType.LOW.tooltip),
                Tooltip.of(RedstoneControlType.HIGH.tooltip),
                Tooltip.of(RedstoneControlType.PULSE.tooltip),
                Tooltip.of(RedstoneControlType.DISABLED.tooltip)
        );
        addDrawableChild(redstoneControlButton);
    }

    @Override
    public void render(MatrixStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(poseStack);
        //this.drawBackground(poseStack, partialTick, mouseX, mouseY);
        super.render(poseStack, mouseX, mouseY, partialTick);
        this.drawMouseoverTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(MatrixStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, mainTexture);

        drawTexture(poseStack, x, y,0,0, backgroundWidth, backgroundHeight);
    }
}
