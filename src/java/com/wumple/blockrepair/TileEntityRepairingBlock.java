package com.wumple.blockrepair;

import java.util.List;
import java.util.Random;

/*
 * Based on CoroUtil's TileEntityRepairingBlock
 * from https://github.com/Corosauce/CoroUtil
 */

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityRepairingBlock extends TileEntity implements ITickable
{
	// block state to restore later
	protected IBlockState orig_blockState;
    protected float orig_hardness = 1;
    protected float orig_explosionResistance = 1;
    
    // when to restore state
    protected long timeToRepairAt = 0;
        
    protected static boolean useLoggingDebug() {
    	return false;
    }
    
    public boolean canRepairBlock() {
        AxisAlignedBB aabb = this.getBlockType().getDefaultState().getBoundingBox(this.getWorld(), this.getPos());
        aabb = aabb.offset(this.getPos());
        List<EntityLivingBase> listTest = this.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, aabb);
        return (listTest.size() == 0);
    }

    public void onCantRepairBlock() {
    	
    }
    
    @Override
    public void update()
    {
    	if (!getWorld().isRemote) {
    		
    	 	if (getWorld().getTotalWorldTime() >= timeToRepairAt) {
    	 		
    	 	
            //if for some reason data is invalid, remove block
    	 		if (orig_blockState == null || orig_blockState == this.getBlockType().getDefaultState()) {
    	 			BlockRepair.logger.debug("invalid state for repairing block, removing, orig_blockState: " + orig_blockState + " vs " + this.getBlockType().getDefaultState());
    	 			getWorld().setBlockState(this.getPos(), Blocks.AIR.getDefaultState());
    	 		}
    	 		else {
                    if (canRepairBlock()) {
                        restoreBlock();
                    }
                    else {
                    	onCantRepairBlock();
                    }
    	 		}
    	 	}
    	}
    }
    
    @Override
    public void onLoad() {
        super.onLoad();

        //i dont currently see any clean ways to init the tile entity with the orig_blockState before onLoad is called, so we cant do this here
        /*if (orig_blockState == null || orig_blockState == this.getBlockType().getDefaultState()) {
            getWorld().setBlockState(this.getPos(), Blocks.AIR.getDefaultState());
        }*/
    }
    
    protected void postRestoreBlock() {
        //try to untrigger leaf decay for those large trees too far from wood source//also undo it for neighbors around it
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos posFix = pos.add(x, y, z);
                    IBlockState state = world.getBlockState(posFix);
                    if (state.getBlock() instanceof BlockLeaves) {
                        try {
                        	BlockRepair.logger.debug("restoring leaf to non decay state at pos: " + posFix);
                            world.setBlockState(posFix, state.withProperty(BlockLeaves.CHECK_DECAY, false), 4);
                        } catch (Exception ex) {
                            //must be a modded block that doesnt use decay
                            if (useLoggingDebug()) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
	
    public void restoreBlock() {
    	BlockRepair.logger.debug("restoring block to state: " + orig_blockState + " at " + this.getPos());
        getWorld().setBlockState(this.getPos(), orig_blockState);
        postRestoreBlock();
    }

    public void setBlockData(IBlockState state) {
        this.orig_blockState = state;
    }

    public IBlockState getOrig_blockState() {
        return orig_blockState;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound var1)
    {
        if (orig_blockState != null) {
            String str = Block.REGISTRY.getNameForObject(this.orig_blockState.getBlock()).toString();
            var1.setString("orig_blockName", str);
            var1.setInteger("orig_blockMeta", this.orig_blockState.getBlock().getMetaFromState(this.orig_blockState));
        }
        var1.setLong("timeToRepairAt", timeToRepairAt);

        var1.setFloat("orig_hardness", orig_hardness);
        var1.setFloat("orig_explosionResistance", orig_explosionResistance);

        return super.writeToNBT(var1);
    }

    @Override
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        timeToRepairAt = var1.getLong("timeToRepairAt");
        try {
            Block block = Block.getBlockFromName(var1.getString("orig_blockName"));
            if (block != null) {
                int meta = var1.getInteger("orig_blockMeta");
                this.orig_blockState = block.getStateFromMeta(meta);
            }
        } catch (Exception ex) {
            ex.printStackTrace();

            this.orig_blockState = Blocks.AIR.getDefaultState();
        }

        orig_hardness = var1.getFloat("orig_hardness");
        orig_explosionResistance = var1.getFloat("orig_explosionResistance");
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    public float getOrig_hardness() {
        return orig_hardness;
    }

    public void setOrig_hardness(float orig_hardness) {
        this.orig_hardness = orig_hardness;
    }

    public float getOrig_explosionResistance() {
        return orig_explosionResistance;
    }

    public void setOrig_explosionResistance(float orig_explosionResistance) {
        this.orig_explosionResistance = orig_explosionResistance;
    }
    
    public void setTicksToRepair(World world, int ticksToRepair) {
    	this.timeToRepairAt = world.getTotalWorldTime() + ticksToRepair;
    }
}