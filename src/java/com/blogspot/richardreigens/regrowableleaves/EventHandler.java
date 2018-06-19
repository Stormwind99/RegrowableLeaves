package com.blogspot.richardreigens.regrowableleaves;

import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
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
           	RegrowableLeaves.logger.debug("Block: " + block);

            // only regrow if leaves broke with empty hand or tool other than shears
            if (player.getHeldItemMainhand().isEmpty() || player.getHeldItemMainhand().getItem() != Items.SHEARS) {
                // only handle breaking leaves
            	if (block.isLeaves(state, world, pos) || (block instanceof BlockLeaves)) {
                    RegrowableLeaves.logger.debug("Breaking leaves");

                    RegrowableLeaves.blockRepairManager.replaceBlock(world, state, pos, LeavesRepairManager.getRandomTicksToRepair());
                    
                    // TODO: harvest
                    // block.harvestBlock(world, player, pos, state, null, ItemStack.EMPTY);
                  
                    // event handled, so don't handle it later
                    e.setCanceled(true);
                }
            }
        }
    }
    
    /*
     * Consider handling explosionEvent and repairing any leaves (assuming tree survives)
     * See CoroUtil.forge.EventHandlerForge#explosionEvent for example
     */
    
}
