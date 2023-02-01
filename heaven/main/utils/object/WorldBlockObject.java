/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.utils.object;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class WorldBlockObject {
    private final Block block;
    private final BlockPos blockPos;
    private final int blockID;

    public WorldBlockObject(Block block, BlockPos blockPos) {
        this.block = block;
        this.blockPos = blockPos;
        this.blockID = Block.getIdFromBlock(block);
    }

    public Block getBlock() {
        return this.block;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public int getBlockID() {
        return this.blockID;
    }
}

