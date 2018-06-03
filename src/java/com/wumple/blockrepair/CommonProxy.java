package com.wumple.blockrepair;

import com.wumple.blockrepair.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class CommonProxy
{	
    public static final String block_repairing_name = "repairing_block";

    @GameRegistry.ObjectHolder(Reference.MOD_ID + ":" + block_repairing_name)
    public static Block blockRepairingBlock;

    @GameRegistry.ObjectHolder(Reference.MOD_ID + ":blank")
    public static Block blockBlank;

    public CommonProxy()
    {
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        //used for replacing foliage with blank for shaders
        addBlock(event, new BlockBlank(Material.AIR), "blank");
        addBlock(event, new BlockRepairingBlock(), TileEntityRepairingBlock.class, block_repairing_name);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        addItemBlock(event, new ItemBlock(blockRepairingBlock).setRegistryName(blockRepairingBlock.getRegistryName()));
    }

    public static void addBlock(RegistryEvent.Register<Block> event, Block block, Class tEnt, String unlocalizedName) {
        addBlock(event, block, tEnt, unlocalizedName, true);
    }

    public static void addBlock(RegistryEvent.Register<Block> event, Block block, Class tEnt, String unlocalizedName, boolean creativeTab) {
        addBlock(event, block, unlocalizedName, creativeTab);
        GameRegistry.registerTileEntity(tEnt, unlocalizedName);
    }

    public static void addBlock(RegistryEvent.Register<Block> event, Block parBlock, String unlocalizedName) {
        addBlock(event, parBlock, unlocalizedName, true);
    }

    public static void addBlock(RegistryEvent.Register<Block> event, Block parBlock, String unlocalizedName, boolean creativeTab) {
        //GameRegistry.registerBlock(parBlock, unlocalizedName);

        parBlock.setUnlocalizedName(getNameUnlocalized(unlocalizedName));
        parBlock.setRegistryName(getNameDomained(unlocalizedName));

        parBlock.setCreativeTab(CreativeTabs.MISC);

        if (event != null) {
            event.getRegistry().register(parBlock);
        }
    }

    public static void addItemBlock(RegistryEvent.Register<Item> event, Item item) {
        event.getRegistry().register(item);
    }

    public static void addItem(RegistryEvent.Register<Item> event, Item item, String name) {
        item.setUnlocalizedName(getNameUnlocalized(name));
        //item.setRegistryName(new ResourceLocation(Weather.modID, name));
        item.setRegistryName(getNameDomained(name));

        item.setCreativeTab(CreativeTabs.MISC);

        if (event != null) {
            event.getRegistry().register(item);
        } else {
            //GameRegistry.register(item);
        }

        //registerItemVariantModel(item, name, 0);

        //return item;
    }

    public static String getNameUnlocalized(String name) {
        return Reference.MOD_ID + "." + name;
    }

    public static String getNameDomained(String name) {
        return Reference.MOD_ID + ":" + name;
    }
}