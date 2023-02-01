/*
 * Decompiled with CFR 0.152.
 */
package net.optifine.shaders;

import java.util.Iterator;
import java.util.NoSuchElementException;
import net.minecraft.util.BlockPos;
import net.optifine.BlockPosM;

public class IteratorAxis
implements Iterator<BlockPos> {
    private final double yDelta;
    private final double zDelta;
    private final int xStart;
    private final int xEnd;
    private double yStart;
    private double yEnd;
    private double zStart;
    private double zEnd;
    private int xNext;
    private double yNext;
    private double zNext;
    private final BlockPosM pos = new BlockPosM(0, 0, 0);
    private boolean hasNext = false;

    public IteratorAxis(BlockPos posStart, BlockPos posEnd, double yDelta, double zDelta) {
        this.yDelta = yDelta;
        this.zDelta = zDelta;
        this.xStart = posStart.getX();
        this.xEnd = posEnd.getX();
        this.yStart = posStart.getY();
        this.yEnd = (double)posEnd.getY() - 0.5;
        this.zStart = posStart.getZ();
        this.zEnd = (double)posEnd.getZ() - 0.5;
        this.xNext = this.xStart;
        this.yNext = this.yStart;
        this.zNext = this.zStart;
        this.hasNext = this.xNext < this.xEnd && this.yNext < this.yEnd && this.zNext < this.zEnd;
    }

    @Override
    public boolean hasNext() {
        return this.hasNext;
    }

    @Override
    public BlockPos next() {
        if (!this.hasNext) {
            throw new NoSuchElementException();
        }
        this.pos.setXyz((double)this.xNext, this.yNext, this.zNext);
        this.nextPos();
        this.hasNext = this.xNext < this.xEnd && this.yNext < this.yEnd && this.zNext < this.zEnd;
        return this.pos;
    }

    private void nextPos() {
        this.zNext += 1.0;
        if (this.zNext >= this.zEnd) {
            this.zNext = this.zStart;
            this.yNext += 1.0;
            if (this.yNext >= this.yEnd) {
                this.yNext = this.yStart;
                this.yStart += this.yDelta;
                this.yEnd += this.yDelta;
                this.yNext = this.yStart;
                this.zStart += this.zDelta;
                this.zEnd += this.zDelta;
                this.zNext = this.zStart;
                ++this.xNext;
                if (this.xNext >= this.xEnd) {
                    // empty if block
                }
            }
        }
    }

    @Override
    public void remove() {
        throw new RuntimeException("Not implemented");
    }

    public static void main(String[] args) {
        BlockPos blockpos = new BlockPos(-2, 10, 20);
        BlockPos blockpos1 = new BlockPos(2, 12, 22);
        double d0 = -0.5;
        double d1 = 0.5;
        IteratorAxis iteratoraxis = new IteratorAxis(blockpos, blockpos1, d0, d1);
        System.out.println("Start: " + blockpos + ", end: " + blockpos1 + ", yDelta: " + d0 + ", zDelta: " + d1);
        while (iteratoraxis.hasNext()) {
            BlockPos blockpos2 = iteratoraxis.next();
            System.out.println(blockpos2);
        }
    }
}

