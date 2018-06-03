package com.blogspot.richardreigens.regrowableleaves;

import com.wumple.blockrepair.TileEntityRepairingBlock;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityLeavesRepairingBlock extends TileEntityRepairingBlock {

	protected boolean canLeavesGrowAtLocation(World worldIn, BlockPos pos) {
	    boolean leavesCanGrow = !ConfigurationHandler.generalSettings.growOutward;
	    
	    if (leavesCanGrow) {
	    	return leavesCanGrow;
	    }
	    
	    BlockPos[] positions = new BlockPos[] { pos.up(), pos.down(), pos.east(), pos.west(), pos.north(), pos.south() };
	    
	    for (BlockPos blockpos : positions) {
	    	final IBlockState blockState = worldIn.getBlockState(blockpos);
	    	final Block block = blockState.getBlock();
	    	
	    	if (block.canSustainLeaves(blockState, worldIn, blockpos) || block.isLeaves(blockState, worldIn, blockpos)) {
	    		leavesCanGrow = true;
	    		break;
	    	}
	    }               
		
	    return leavesCanGrow;
	}

	@Override
	public boolean canRepairBlock() {
		return (
				super.canRepairBlock()
				&& getWorld().isAreaLoaded(getPos(), 1)
				&& (getWorld().getLightFromNeighbors(getPos().up()) >= ConfigurationHandler.generalSettings.lightRequiredToGrow)
				&& canLeavesGrowAtLocation(getWorld(), getPos())
				);
	}

	@Override
	public void onCantRepairBlock() {
    	super.onCantRepairBlock();
    	setTicksToRepair(getWorld(), LeavesRepairManager.getRandomTicksToRepair());
    }
}
