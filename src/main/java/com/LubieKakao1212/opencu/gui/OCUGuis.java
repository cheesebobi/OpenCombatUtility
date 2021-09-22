package com.LubieKakao1212.opencu.gui;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.block.tileentity.TileEntityOmniDispenser;
import com.LubieKakao1212.opencu.gui.container.OmnidispenserContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class OCUGuis {

    public static final Map<String, Integer> ids = new HashMap<>();

    static
    {
        registerGui("dispenser",
                new OCUGuiHandler.TEGuiFactory(
                        TileEntityOmniDispenser.class,
                        (OCUGuiHandler.TEGuiFactory.Factory<TileEntityOmniDispenser>)(EntityPlayer player, TileEntityOmniDispenser tileEntity) -> { return new OmnidispenserGUI(tileEntity, player); },
                        (OCUGuiHandler.TEGuiFactory.Factory<TileEntityOmniDispenser>)(EntityPlayer player, TileEntityOmniDispenser tileEntity) -> { return new OmnidispenserContainer(tileEntity, player.inventory); }
                ));
    }

    public static void openGUI(String id, EntityPlayer player, World world, int x, int y, int z) {
        player.openGui(OpenCUMod.instance, ids.get(id), world, x, y, z);
    }

    private static void registerGui(String id, OCUGuiHandler.GuiFactory factory) {
        ids.put(id, OCUGuiHandler.guis.size());
        OCUGuiHandler.guis.add(factory);
    }
}
