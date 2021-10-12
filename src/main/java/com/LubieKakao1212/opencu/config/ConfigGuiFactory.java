/*package com.LubieKakao1212.opencu.config;

import com.LubieKakao1212.opencu.OpenCUMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConfigGuiFactory implements IModGuiFactory {


    @Override
    public void initialize(Minecraft minecraftInstance) {

    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new OpenCUConfigGui(parentScreen);
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    private static class OpenCUConfigGui extends GuiConfig {

        public OpenCUConfigGui(GuiScreen parentScreen) {
            super(parentScreen, createElements(), OpenCUMod.MODID, false, false, "Open Combat Utility");
        }

        private static List<IConfigElement> createElements() {
            List<IConfigElement> list = new ArrayList<>();

            list.add(new DummyConfigElement.DummyCategoryElement());

            return list;
        }

        private static class Repulsor extends GuiConfigEntries.CategoryEntry {

            public Repulsor(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
                super(owningScreen, owningEntryList, configElement);
            }



        }

    }

}*/
