package com.blogspot.richardreigens.regrowableleaves;

import com.wumple.blockrepair.BlockRepairingBlock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;

public class BlockLeavesRepairingBlock extends BlockRepairingBlock {
    @Override
    public TileEntity createNewTileEntity(World var1, int meta) 
    {
    	return new TileEntityLeavesRepairingBlock();
    }
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
    	// show debug model if debugging, invisible if not debugging
    	if (ConfigurationHandler.generalSettings.debugMode == true) {
    		return EnumBlockRenderType.MODEL;
    	} 
    	else {
    		return EnumBlockRenderType.INVISIBLE;
    	}
    }
}
