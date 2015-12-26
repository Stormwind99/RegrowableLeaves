package com.blogspot.richardreigens.regrowableleaves.blocks;

import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by LiLRichy on 12/26/2015.
 */
public class OakLeafAirBlock extends BlockAir {

    public OakLeafAirBlock() {
        super();
        this.setTickRandomly(true);
        this.setBlockName("OakLeafAir");
    }

    public void updateTick(World world, int x, int y, int z, Random random) {
        if (!world.isRemote) {

            if (random.nextInt(5) > 0) {
                world.setBlock(x, y, z, Blocks.leaves, 0, 3);
            }
        }
    }
}