package com.blogspot.richardreigens.regrowableleaves.blocks.bopLeavesAirBlocks;


import biomesoplenty.api.block.BOPBlocks;
import com.blogspot.richardreigens.regrowableleaves.ConfigurationHandler;
import com.blogspot.richardreigens.regrowableleaves.LogHelper;
import com.blogspot.richardreigens.regrowableleaves.blocks.bopLeavesPages.BOPPage_4;
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
public class BlockBOPLeafAir_4 extends BlockAir {
    public static final PropertyEnum TYPE = PropertyEnum.create("type", BOPPage_4.EnumType.class);

    public BlockBOPLeafAir_4() {
        super();
        this.setTickRandomly(true);
        this.setUnlocalizedName("BOPLeafAir_4");
        this.setRegistryName("BlockBOPLeafAir_4");
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPE, BOPPage_4.EnumType.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((BOPPage_4.EnumType) state.getValue(TYPE)).getID();
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
                if (ConfigurationHandler.debugMode) LogHelper.info("BlockBOPLeafAir_4 Tick");
                worldIn.setBlockState(pos, BOPBlocks.leaves_4.getStateFromMeta(state.getBlock().getMetaFromState(state) + 8));
            }
        }
    }
}