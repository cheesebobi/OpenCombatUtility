package com.LubieKakao1212.opencu.block;

import com.LubieKakao1212.opencu.block.tileentity.TileEntityOmniDispenser;
import com.LubieKakao1212.opencu.block.tileentity.TileEntityRepulsor;
import com.LubieKakao1212.opencu.block.tileentity.renderer.RendererRepulsor;
import com.LubieKakao1212.opencu.gui.OCUGuis;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockOmniDispenserFrame extends CUBlock {

    public BlockOmniDispenserFrame(Material material, String name) {
        super(material, name);
        this.setHardness(1f);
        this.setHarvestLevel("pickaxe", 2);
        this.setSoundType(SoundType.METAL);
        this.setResistance(3.5f);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState blockState) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityOmniDispenser();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote){
            OCUGuis.openGUI("dispenser", playerIn, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity te = worldIn.getTileEntity(pos);

        IItemHandler inventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        for(int i=0; i<inventory.getSlots(); i++) {
            Block.spawnAsEntity(worldIn, pos, inventory.extractItem(i, 999, false));
        }

        super.breakBlock(worldIn, pos, state);
    }
}
