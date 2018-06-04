package com.blogspot.richardreigens.regrowableleaves;

import java.util.concurrent.ThreadLocalRandom;

import com.wumple.blockrepair.BlockBlank;
import com.wumple.blockrepair.BlockRepairManager;
import com.wumple.blockrepair.BlockRepairingBlock;

import net.minecraft.block.material.Material;

public class LeavesRepairManager extends BlockRepairManager {

	@Override
    protected BlockBlank BlockBlankFactory() {
    	return new BlockBlank(Material.AIR);
    }
    
	@Override
	protected BlockRepairingBlock BlockRepairingBlockFactory() {
    	return new BlockLeavesRepairingBlock();
    }
    
	@Override
	protected Class TileEntityRepairingBlockClass() {
    	return TileEntityLeavesRepairingBlock.class;
	}
	
	public static int getRandomTicksToRepair() {
		return 20 /*per second*/ * ThreadLocalRandom.current().nextInt(ConfigurationHandler.generalSettings.leafRegrowthRate/2,ConfigurationHandler.generalSettings.leafRegrowthRate*3/2);  
	}
}
