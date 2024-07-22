package com.LubieKakao1212.opencu.fabric.item;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.registry.fabric.CUBlocksImpl;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {

	public static ItemGroup OPEN_COMBAT_UTILITY = FabricItemGroup.builder(
					new Identifier(OpenCUModCommon.MODID, "open_combat_utility"))
			.displayName(Text.translatable("itemGroup.opencu"))
			.icon(() -> new ItemStack(CUBlocksImpl.REPULSOR)).build();

	public static void registerItemGroups() {
		// Register your item groups here if necessary
	}
}
