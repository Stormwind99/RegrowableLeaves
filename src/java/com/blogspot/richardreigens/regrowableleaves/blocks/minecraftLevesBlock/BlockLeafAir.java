package com.blogspot.richardreigens.regrowableleaves.blocks.minecraftLevesBlock;


import java.util.Random;

import com.blogspot.richardreigens.regrowableleaves.ConfigurationHandler;
import com.blogspot.richardreigens.regrowableleaves.LogHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by LiLRichy on 12/26/2015.
 */
public class BlockLeafAir extends BlockAir {
    public static final PropertyEnum<BlockProperties.EnumType> TYPE = PropertyEnum.create("type", BlockProperties.EnumType.class);
    final int META_OFFSET = 5;
    int[] surroundings;

    public BlockLeafAir() {
        super();
        this.setTickRandomly(true);
        this.setUnlocalizedName("LeafAir");
        this.setRegistryName("BlockLeafAir");
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        if (ConfigurationHandler.generalSettings.debugMode)
            return EnumBlockRenderType.MODEL;
        else
            return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPE, BlockProperties.EnumType.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((BlockProperties.EnumType) state.getValue(TYPE)).getID();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE);
    }
  
    // TODO: why doesn't code from BlockLeaves#updateTick work?
    // This is broken - always returns false
    protected boolean canLeavesBeSustainedAtPos(World worldIn, BlockPos pos, IBlockState state)
    {
        boolean leavesCanGrow = !ConfigurationHandler.generalSettings.growOutward;
        
        if (leavesCanGrow) {
        	return leavesCanGrow;
        }
    
        // copied from BlockLeaves#updateTick, unfortunately
	    int k = pos.getX();
	    int l = pos.getY();
	    int i1 = pos.getZ();
	
	    if (this.surroundings == null)
	    {
	        this.surroundings = new int[32768];
	    }
	
	    if (!worldIn.isAreaLoaded(pos, 1)) return false; // Forge: prevent decaying leaves from updating neighbors and loading unloaded chunks
	    if (worldIn.isAreaLoaded(pos, 6)) // Forge: extend range from 5 to 6 to account for neighbor checks in world.markAndNotifyBlock -> world.updateObservingBlocksAt
	    {
	        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
	
	        for (int i2 = -4; i2 <= 4; ++i2)
	        {
	            for (int j2 = -4; j2 <= 4; ++j2)
	            {
	                for (int k2 = -4; k2 <= 4; ++k2)
	                {
	                    IBlockState iblockstate = worldIn.getBlockState(blockpos$mutableblockpos.setPos(k + i2, l + j2, i1 + k2));
	                    Block block = iblockstate.getBlock();
	
	                    if (!block.canSustainLeaves(iblockstate, worldIn, blockpos$mutableblockpos.setPos(k + i2, l + j2, i1 + k2)))
	                    {
	                    	blockpos$mutableblockpos.setPos(k + i2, l + j2, i1 + k2);
	                        if (block.isLeaves(iblockstate, worldIn, blockpos$mutableblockpos) || (pos == blockpos$mutableblockpos))
	                        {
	                            this.surroundings[(i2 + 16) * 1024 + (j2 + 16) * 32 + k2 + 16] = -2;
	                        }
	                        else
	                        {
	                            this.surroundings[(i2 + 16) * 1024 + (j2 + 16) * 32 + k2 + 16] = -1;
	                        }
	                    }
	                    else
	                    {
	                        this.surroundings[(i2 + 16) * 1024 + (j2 + 16) * 32 + k2 + 16] = 0;
	                    }
	                }
	            }
	        }
	
	        for (int i3 = 1; i3 <= 4; ++i3)
	        {
	            for (int j3 = -4; j3 <= 4; ++j3)
	            {
	                for (int k3 = -4; k3 <= 4; ++k3)
	                {
	                    for (int l3 = -4; l3 <= 4; ++l3)
	                    {
	                        if (this.surroundings[(j3 + 16) * 1024 + (k3 + 16) * 32 + l3 + 16] == i3 - 1)
	                        {
	                            if (this.surroundings[(j3 + 16 - 1) * 1024 + (k3 + 16) * 32 + l3 + 16] == -2)
	                            {
	                                this.surroundings[(j3 + 16 - 1) * 1024 + (k3 + 16) * 32 + l3 + 16] = i3;
	                            }
	
	                            if (this.surroundings[(j3 + 16 + 1) * 1024 + (k3 + 16) * 32 + l3 + 16] == -2)
	                            {
	                                this.surroundings[(j3 + 16 + 1) * 1024 + (k3 + 16) * 32 + l3 + 16] = i3;
	                            }
	
	                            if (this.surroundings[(j3 + 16) * 1024 + (k3 + 16 - 1) * 32 + l3 + 16] == -2)
	                            {
	                                this.surroundings[(j3 + 16) * 1024 + (k3 + 16 - 1) * 32 + l3 + 16] = i3;
	                            }
	
	                            if (this.surroundings[(j3 + 16) * 1024 + (k3 + 16 + 1) * 32 + l3 + 16] == -2)
	                            {
	                                this.surroundings[(j3 + 16) * 1024 + (k3 + 16 + 1) * 32 + l3 + 16] = i3;
	                            }
	
	                            if (this.surroundings[(j3 + 16) * 1024 + (k3 + 16) * 32 + (l3 + 16 - 1)] == -2)
	                            {
	                                this.surroundings[(j3 + 16) * 1024 + (k3 + 16) * 32 + (l3 + 16 - 1)] = i3;
	                            }
	
	                            if (this.surroundings[(j3 + 16) * 1024 + (k3 + 16) * 32 + l3 + 16 + 1] == -2)
	                            {
	                                this.surroundings[(j3 + 16) * 1024 + (k3 + 16) * 32 + l3 + 16 + 1] = i3;
	                            }
	                        }
	                    }
	                }
	            }
	        }
	    }
	
	    int l2 = this.surroundings[16912];
	
	    return (l2 >= 0);
     }
    
    protected boolean canLeavesGrowAtLocation(World worldIn, BlockPos pos, IBlockState state) {
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

    @SuppressWarnings("deprecation")
    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            int metaToSet = state.getBlock().getMetaFromState(state);
            if (rand.nextInt(10) > ConfigurationHandler.generalSettings.leafRegrowthRate && worldIn.getLight(pos) >= ConfigurationHandler.generalSettings.lightRequiredToGrow) {
            	// TODO: consider checking for valid tree like https://github.com/Shovinus/ChopDownUpdated/blob/master/versions/1.12.2/src/main/java/com/shovinus/chopdownupdated/tree/Tree.java
               
            	// only grow leaves next to leaves or leaf sustaining blocks - makes it look like leaves will grow back outward from remaining tree parts
            	boolean leavesCanGrow = canLeavesGrowAtLocation(worldIn, pos, state);
               
                if (leavesCanGrow) {
                	if (ConfigurationHandler.generalSettings.debugMode) LogHelper.info("BlockLeafAir Tick sustained");
                	
	                if (metaToSet >= META_OFFSET)
	                    worldIn.setBlockState(pos, Blocks.LEAVES2.getStateFromMeta(state.getBlock().getMetaFromState(state) - META_OFFSET + 8));
	                else
	                    worldIn.setBlockState(pos, Blocks.LEAVES.getStateFromMeta(state.getBlock().getMetaFromState(state) + 8));
                } else {
                	if (ConfigurationHandler.generalSettings.debugMode) LogHelper.info("BlockLeafAir Tick unsubstained");
                }
            }
        }
    }
}

