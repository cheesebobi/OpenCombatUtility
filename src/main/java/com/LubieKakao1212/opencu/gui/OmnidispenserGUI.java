package com.LubieKakao1212.opencu.gui;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.block.tileentity.TileEntityOmniDispenser;
import com.LubieKakao1212.opencu.gui.container.OmnidispenserContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class OmnidispenserGUI extends GuiContainer {

    private static final ResourceLocation mainTexture = new ResourceLocation(OpenCUMod.MODID, "textures/gui/omnidispenser_gui.png");

    public OmnidispenserGUI(TileEntityOmniDispenser te, EntityPlayer player) {
        super(new OmnidispenserContainer(te, player.inventory));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(mainTexture);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, 176, 166);
    }
}
