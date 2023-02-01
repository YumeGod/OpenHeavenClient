/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event.events.rendering;

import heaven.main.event.Event;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class EventRenderBlock
extends Event {
    private final int x;
    private final int y;
    private final int z;
    private final Block block;
    public final BlockPos blockPos;

    public EventRenderBlock(int x, int y, int z, Block block, BlockPos blockPos) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.block = block;
        this.blockPos = blockPos;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public Block getBlock() {
        return this.block;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }
}

