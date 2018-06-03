package com.wumple.blockrepair;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockRepairManager
{	
    public static final String block_repairing_name = "repairing_block";

    @GameRegistry.ObjectHolder(Reference.MOD_ID + ":" + block_repairing_name)
    public static final Block blockRepairingBlock = null;

    @GameRegistry.ObjectHolder(Reference.MOD_ID + ":blank")
    public static final Block blockBlank = null;

    public BlockRepairManager()
    {
    	MinecraftForge.EVENT_BUS.register(this);
    }
    
    public BlockBlank BlockBlankFactory() {
    	return new BlockBlank(Material.AIR);
    }
    
    public BlockRepairingBlock BlockRepairingBlockFactory() {
    	return new BlockRepairingBlock();
    }
    
    public Class TileEntityRepairingBlockClass() {
    	return TileEntityRepairingBlock.class;
	}
    
    /*
     * Register blocks, items, etc
     */

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        //used for replacing foliage with blank for shaders
        addBlock(event, BlockBlankFactory(), "blank");
        addBlock(event, BlockRepairingBlockFactory(), TileEntityRepairingBlockClass(), block_repairing_name);
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        addItemBlock(event, new ItemBlock(blockRepairingBlock).setRegistryName(blockRepairingBlock.getRegistryName()));
    }

    public void addBlock(RegistryEvent.Register<Block> event, Block block, Class tEnt, String unlocalizedName) {
        addBlock(event, block, tEnt, unlocalizedName, true);
    }

    public void addBlock(RegistryEvent.Register<Block> event, Block block, Class tEnt, String unlocalizedName, boolean creativeTab) {
        addBlock(event, block, unlocalizedName, creativeTab);
        GameRegistry.registerTileEntity(tEnt, unlocalizedName);
    }

    public void addBlock(RegistryEvent.Register<Block> event, Block parBlock, String unlocalizedName) {
        addBlock(event, parBlock, unlocalizedName, true);
    }

    public void addBlock(RegistryEvent.Register<Block> event, Block parBlock, String unlocalizedName, boolean creativeTab) {
        parBlock.setUnlocalizedName(getNameUnlocalized(unlocalizedName));
        parBlock.setRegistryName(getNameDomained(unlocalizedName));

        parBlock.setCreativeTab(CreativeTabs.MISC);

        if (event != null) {
            event.getRegistry().register(parBlock);
        }
    }

    public void addItemBlock(RegistryEvent.Register<Item> event, Item item) {
        event.getRegistry().register(item);
    }

    public void addItem(RegistryEvent.Register<Item> event, Item item, String name) {
        item.setUnlocalizedName(getNameUnlocalized(name));
        item.setRegistryName(getNameDomained(name));

        item.setCreativeTab(CreativeTabs.MISC);

        if (event != null) {
            event.getRegistry().register(item);
        } 
    }

    public static String getNameUnlocalized(String name) {
        return Reference.MOD_ID + "." + name;
    }

    public static String getNameDomained(String name) {
        return Reference.MOD_ID + ":" + name;
    }
    
    /*
     * Place repair block methods
     */
    
    public TileEntityRepairingBlock replaceBlockAndBackup(World world, BlockPos pos) {
        return replaceBlockAndBackup(world, pos, 20*60*5);
    }

    /**
     *
     * Some mod blocks might require getting data only while their block is still around, so we get it here and save it rather than on the fly later
     *
     * @param world
     * @param pos
     */
    public TileEntityRepairingBlock replaceBlockAndBackup(World world, BlockPos pos, int ticksToRepair) {
        IBlockState oldState = world.getBlockState(pos);
        float oldHardness = oldState.getBlockHardness(world, pos);
        float oldExplosionResistance = 1;
        try {
            oldExplosionResistance = oldState.getBlock().getExplosionResistance(world, pos, null, null);
        } catch (Exception ex) {

        }

        world.setBlockState(pos, blockRepairingBlock.getDefaultState());
        TileEntity tEnt = world.getTileEntity(pos);
        if (tEnt instanceof TileEntityRepairingBlock) {
            //IBlockState state = world.getBlockState(pos);
            BlockRepair.logger.debug("set repairing block for pos: " + pos + ", " + oldState.getBlock());
            TileEntityRepairingBlock repairing = ((TileEntityRepairingBlock) tEnt);
            repairing.setBlockData(oldState);
            repairing.setOrig_hardness(oldHardness);
            repairing.setOrig_explosionResistance(oldExplosionResistance);
            repairing.setTicksToRepair(world, ticksToRepair);
            return repairing;
        } else {
        	BlockRepair.logger.debug("failed to set repairing block for pos: " + pos);
            return null;
        }
    }

    public boolean replaceBlock(World world, IBlockState state, BlockPos pos, int ticks) {
    	if (!BlockRepairingBlock.canConvertToRepairingBlock(world, state)) {
    		BlockRepair.logger.info("cant use repairing block on: " + state);
    		return false;
    	}
    	
    	replaceBlockAndBackup(world, pos, ticks);
    	return true;
    }
}