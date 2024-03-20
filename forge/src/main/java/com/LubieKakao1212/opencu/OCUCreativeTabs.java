package com.LubieKakao1212.opencu;

import com.LubieKakao1212.opencu.init.CUBlocks;
import com.LubieKakao1212.opencu.init.CUItems;
import dan200.computercraft.api.ComputerCraftAPI;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class OCUCreativeTabs {

    @SubscribeEvent
    public static void registerItemGroup(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new Identifier(OpenCUMod.MODID, "tab"), (builder) ->
            builder.icon(() -> new ItemStack((ItemConvertible) CUBlocks.REPULSOR.get()))
                    .displayName(Text.literal("Open Combat Utility"))
                    .entries((displayContext, entries) -> {
                        entries.add(new ItemStack(CUBlocks.REPULSOR.get()));
                        entries.add(new ItemStack(CUBlocks.OMNI_DISPENSER.get()));

                        entries.add(new ItemStack(Blocks.DISPENSER));
                        entries.add(new ItemStack(Blocks.DROPPER));
                        entries.add(new ItemStack(CUItems.DISPENSER_T2.get()));
                        entries.add(new ItemStack(CUItems.DISPENSER_T3.get()));
                        entries.add(new ItemStack(CUItems.VECTOR_MESH.get()));
                    })
        );
    }

}
