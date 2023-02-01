/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event.events.rendering;

import heaven.main.event.Event;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class EventBlockRenderSide
extends Event {
    private IBlockState state;
    private final IBlockAccess world;
    public final BlockPos pos;
    private final EnumFacing side;
    private boolean toRender;
    public final double maxX;
    public final double maxY;
    public final double maxZ;
    public final double minX;
    public final double minY;
    public final double minZ;

    public EventBlockRenderSide(IBlockAccess world, BlockPos pos, EnumFacing side, double maxX, double minX, double maxY, double minY, double maxZ, double minZ) {
        if (Minecraft.theWorld != null) {
            this.state = Minecraft.theWorld.getBlockState(pos);
        }
        this.world = world;
        this.pos = pos;
        this.side = side;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
    }

    public IBlockState getState() {
        return this.state;
    }

    public IBlockAccess getWorld() {
        return this.world;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public EnumFacing getSide() {
        return this.side;
    }

    public boolean isToRender() {
        return this.toRender;
    }

    public void setToRender(boolean toRender) {
        this.toRender = toRender;
    }
}

