package com.blogspot.richardreigens.regrowableleaves;

import java.util.Random;

import com.wumple.blockrepair.TileEntityRepairingBlock;
import com.wumple.blockrepair.BlockRepairingBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


/**
 * Created by LiLRichy on 12/26/2015.
 */
public class EventHandler {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void breakEvent(BlockEvent.BreakEvent e) {
        BlockPos pos = e.getPos();
        World world = e.getWorld();
        IBlockState state = e.getState();
        EntityPlayer player = e.getPlayer();
        Block block = state.getBlock();

        if (block.isLeaves(state, world, pos)) {
            //Output for debug text.
            if (ConfigurationHandler.generalSettings.debugMode) {
                LogHelper.info("Block: " + block);
                //LogHelper.info("State: " + state);
                //LogHelper.info("State from Meta: " + state.getBlock().getMetaFromState(state));
            }

            // only regrow if leaves broke with empty hand or tool other than shears
            if (player.getHeldItemMainhand().isEmpty() || player.getHeldItemMainhand().getItem() != Items.SHEARS) {
                // only handle breaking leaves
            	if (block.isLeaves(state, world, pos) || (block instanceof BlockLeaves)) {
                    if (ConfigurationHandler.generalSettings.debugMode) LogHelper.info("Breaking leaves");
                    
                    // BEGIN
                    if (BlockRepairingBlock.canConvertToRepairingBlock(world, state)) {
						TileEntityRepairingBlock.replaceBlockAndBackup(world, pos, 20*10);
					} else {
						LogHelper.info("cant use repairing block on: " + state);
					}
                    // END
                    
                    // TODO: harvest
                    // block.harvestBlock(world, player, pos, state, null, ItemStack.EMPTY);
                  
                    // event handled, so don't handle it later
                    e.setCanceled(true);
                }
            }
        }
    }
}

/*
// TODO: Port this code to CoroUtil's repair block API
 
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
*/
