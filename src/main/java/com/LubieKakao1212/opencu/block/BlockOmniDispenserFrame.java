package com.LubieKakao1212.opencu.block;

import com.LubieKakao1212.opencu.block.tileentity.TileEntityOmniDispenser;
import com.LubieKakao1212.opencu.block.tileentity.TileEntityRepulsor;
import com.LubieKakao1212.opencu.block.tileentity.renderer.RendererRepulsor;
import com.LubieKakao1212.opencu.gui.OCUGuis;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class BlockOmniDispenserFrame extends CUBlock {

    public BlockOmniDispenserFrame(Material material, String name) {
        super(material, name);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerItemModel() {
        super.registerItemModel();
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
}
