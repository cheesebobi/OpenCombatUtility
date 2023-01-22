/*
package com.LubieKakao1212.opencu.gui;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class OCUGuiHandler implements IGuiHandler {

    static final List<GuiFactory> guis = new ArrayList<>();

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return guis.get(ID).ConstructServer(player, world, x, y, z);
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return guis.get(ID).ConstructClient(player, world, x, y, z);
    }

    public static class TEGuiFactory implements GuiFactory {
        public Factory client;
        public Factory server;
        private Class<? extends TileEntity> teClass;

        public TEGuiFactory(Class<? extends TileEntity> tileEntityClass, Factory client, Factory server) {
            this.client = client;
            this.server = server;
            this.teClass = tileEntityClass;
        }

        @Override
        public Object ConstructClient(EntityPlayer player, World world, int x, int y, int z) {
            return construct(client, player,world, x, y, z);
        }

        @Override
        public Object ConstructServer(EntityPlayer player, World world, int x, int y, int z) {
            return construct(server, player,world, x, y, z);
        }

        //
        private Object construct(Factory factory, EntityPlayer player, World world, int x, int y, int z) {
            TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
            if(te != null) {
                if (teClass.isAssignableFrom(te.getClass())) {
                    return factory.Get(player, te);
                }
            }
            return null;
        }

        @FunctionalInterface
        public interface Factory<TE> {
            public Object Get(EntityPlayer player, TE tileEntity);
        }

    }

    public interface GuiFactory {

        Object ConstructClient(EntityPlayer player, World world, int x, int y, int z);

        Object ConstructServer(EntityPlayer player, World world, int x, int y, int z);
    }
}*/
