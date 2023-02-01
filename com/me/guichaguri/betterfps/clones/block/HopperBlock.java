/*
 * Decompiled with CFR 0.152.
 */
package com.me.guichaguri.betterfps.clones.block;

import com.me.guichaguri.betterfps.clones.tileentity.HopperLogic;
import com.me.guichaguri.betterfps.transformers.cloner.CopyMode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class HopperBlock
extends BlockHopper {
    @CopyMode(value=CopyMode.Mode.IGNORE)
    public HopperBlock() {
    }

    @Override
    @CopyMode(value=CopyMode.Mode.APPEND)
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te != null) {
            TileEntityHopper hopper = (TileEntityHopper)te;
            ((HopperLogic)hopper).checkBlockOnTop();
        }
    }
}

