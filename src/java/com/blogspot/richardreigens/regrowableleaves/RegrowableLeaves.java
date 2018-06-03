package com.blogspot.richardreigens.regrowableleaves;

import org.apache.logging.log4j.Logger;

import com.blogspot.richardreigens.regrowableleaves.reference.Reference;
import com.wumple.blockrepair.BlockRepairManager;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, dependencies = Reference.DEPENDENCIES, updateJSON=Reference.UPDATEJSON)
public class RegrowableLeaves {
    @Mod.Instance(Reference.MOD_ID)
    public static RegrowableLeaves instance;
	public static Logger logger;
	public static LeavesRepairManager blockRepairManager = new LeavesRepairManager();
	
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new com.blogspot.richardreigens.regrowableleaves.EventHandler());
    }
}