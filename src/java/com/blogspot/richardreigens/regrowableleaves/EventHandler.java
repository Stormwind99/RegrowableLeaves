package com.blogspot.richardreigens.regrowableleaves;

import CoroUtil.block.TileEntityRepairingBlock;
import CoroUtil.util.UtilMining;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
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
                LogHelper.info("State: " + state);
                LogHelper.info("State from Meta: " + state.getBlock().getMetaFromState(state));
            }

            if (player.getHeldItemMainhand().isEmpty() || player.getHeldItemMainhand().getItem() != Items.SHEARS) {
                //Minecraft Leaves
            	// TODO: consider converting "== Blocks.LEAVES" to "instanceof BlockLeavesBase"
            	// TODO: consider Block#isLeaves method like https://github.com/lumien231/QuickLeafDecay/blob/master/src/main/java/lumien/quickleafdecay/QuickLeafDecay.java
                if (block.isLeaves(state, world, pos)) {
                    if (ConfigurationHandler.generalSettings.debugMode) LogHelper.info("Breaking leaves");
                    
                    // BEGIN
                    if (UtilMining.canConvertToRepairingBlock(world, state)) {
						TileEntityRepairingBlock.replaceBlockAndBackup(world, pos, 20*10);
						//world.setBlockState(snapshot.getPos(), Blocks.LEAVES.getDefaultState(), 3);
					} else {
						LogHelper.info("cant use repairing block on: " + state);
						//world.setBlockState(snapshot.getPos(), state, 3);
					}
                    // END
                    
                    //block.harvestBlock(world, player, pos, state, null, ItemStack.EMPTY);
                    //world.setBlockState(pos, ModBlocks.blockLeafAir.getStateFromMeta(state.getBlock().getMetaFromState(state) % 4));
                    e.setCanceled(true);
                }
            }
        }
    }
}
