/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event.events.world;

import heaven.main.event.Event;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class EventCollideWithBlock
extends Event {
    private Block block;
    private BlockPos blockPos;
    public AxisAlignedBB boundingBox;
    public final List<AxisAlignedBB> boxes;
    public final IBlockState state;

    public EventCollideWithBlock(Block block, BlockPos pos, AxisAlignedBB boundingBox, IBlockState state, List<AxisAlignedBB> boxes) {
        this.block = block;
        this.blockPos = pos;
        this.boundingBox = boundingBox;
        this.state = state;
        this.boxes = boxes;
    }

    public Block getBlock() {
        return this.block;
    }

    public BlockPos getPos() {
        return this.blockPos;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public void setBoundingBox(AxisAlignedBB boundingBox) {
        this.boundingBox = boundingBox;
    }
}

