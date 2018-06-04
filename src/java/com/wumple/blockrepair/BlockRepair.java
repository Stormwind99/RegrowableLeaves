package com.wumple.blockrepair;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/*
 * BlockRepair library
 * 
 * Ways to use:
 * 
 * a. Default behavior: 
 *    1. Create an instance of BlockRepairManager
 *    2. Call BlockRepairManager#replaceBlock(world, state, pos, ticks) for block (at pos) to remove and be repaired in ticks
 *    
 * b. Custom behavior (can have multiple instances with same or different behavior)
 *    1. Subclass BlockRepairManager, BlockRepairingBlock, and/or TileEntityRepairingBlock, overriding methods like:
 *       * BlockRepairManager#BlockBlankFactory, BlockRepairingBlockFactory, TileEntityRepairingBlockClass
 *       * BlockRepairingBlock#createNewTileEntity
 *       * TileEntityRepairingBlock#canRepairBlock, onCantRepairBlock   
 *    2. Create an instance of YourBlockRepairManager
 *    3. Call YourBlockRepairManager#replaceBlock(world, state, pos, ticks) for block (at pos) to remove and be repaired in ticks
 */

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, dependencies = Reference.DEPENDENCIES, updateJSON=Reference.UPDATEJSON)
public class BlockRepair {
    @Mod.Instance(Reference.MOD_ID)
    public static BlockRepair instance;
    
	public static Logger logger;
	public static BlockRepairManager proxy = new BlockRepairManager();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }
}