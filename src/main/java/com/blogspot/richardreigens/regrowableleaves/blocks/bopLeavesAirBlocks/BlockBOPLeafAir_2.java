package com.blogspot.richardreigens.regrowableleaves.blocks.bopLeavesAirBlocks;


import biomesoplenty.api.block.BOPBlocks;
import com.blogspot.richardreigens.regrowableleaves.ConfigurationHandler;
import com.blogspot.richardreigens.regrowableleaves.LogHelper;
import com.blogspot.richardreigens.regrowableleaves.blocks.bopLeavesPages.BOPPage_2;
import net.minecraft.block.BlockAir;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by LiLRichy on 12/26/2015.
 */
public class BlockBOPLeafAir_2 extends BlockAir {
    public static final PropertyEnum TYPE = PropertyEnum.create("type", BOPPage_2.EnumType.class);

    public BlockBOPLeafAir_2() {
        super();
        this.setTickRandomly(true);
        this.setUnlocalizedName("BOPLeafAir_2");
        this.setRegistryName("BlockBOPLeafAir_2");
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPE, BOPPage_2.EnumType.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((BOPPage_2.EnumType) state.getValue(TYPE)).getID();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        if (ConfigurationHandler.debugMode)
            return EnumBlockRenderType.MODEL;
        else
            return EnumBlockRenderType.INVISIBLE;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            if (rand.nextInt(10) > ConfigurationHandler.leafRegrowthRate && worldIn.getLight(pos) >= ConfigurationHandler.lightRequiredToGrow) {
                if (ConfigurationHandler.debugMode) LogHelper.info("BlockBOPLeafAir_2 Tick");
                worldIn.setBlockState(pos, BOPBlocks.leaves_2.getStateFromMeta(state.getBlock().getMetaFromState(state) + 8));
            }
        }
    }
}