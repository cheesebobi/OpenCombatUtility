package com.LubieKakao1212.opencu.block;

import codechicken.lib.block.property.PropertyInteger;
import com.LubieKakao1212.opencu.block.tileentity.TileEntityRepulsor;
import com.LubieKakao1212.opencu.block.tileentity.renderer.RendererRepulsor;
import com.LubieKakao1212.opencu.event.RegisterHandler;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class BlockRepulsor extends BlockBase implements ITileEntityProvider {

    //public static PropertyInteger PART_NUMBER = new PropertyInteger("part", 2);

    public BlockRepulsor(Material material, String name) {
        super(material, name);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityRepulsor();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerItemModel() {
        super.registerItemModel();
        //RegisterHandler.registerItemRenderer(createItemBlock(), 0, name);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRepulsor.class, new RendererRepulsor());
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote) {
            TileEntityRepulsor te = (TileEntityRepulsor) worldIn.getTileEntity(pos);
            playerIn.sendMessage(new TextComponentString("" + te.energyBuffer.getEnergyStored()));
        }
        return true;
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
        return true;
    }



    /*@Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { PART_NUMBER });
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(PART_NUMBER);
    }
*/
    /*@Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(PART_NUMBER, meta);
    }*/

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
    /*@Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }*/
}