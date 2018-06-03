package com.blogspot.richardreigens.regrowableleaves;

import com.wumple.blockrepair.BlockRepairingBlock;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockLeavesRepairingBlock extends BlockRepairingBlock {
    @Override
    public TileEntity createNewTileEntity(World var1, int meta) 
    {
    	return new TileEntityLeavesRepairingBlock();
    }
}
