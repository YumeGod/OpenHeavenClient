/*
 * Decompiled with CFR 0.152.
 */
package com.me.guichaguri.betterfps.clones.world;

import com.me.guichaguri.betterfps.transformers.cloner.CopyMode;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class ChunkLogic
extends Chunk {
    public boolean shouldTick = true;

    @CopyMode(value=CopyMode.Mode.IGNORE)
    private ChunkLogic(World worldIn, int x, int z) {
        super(worldIn, x, z);
    }
}

