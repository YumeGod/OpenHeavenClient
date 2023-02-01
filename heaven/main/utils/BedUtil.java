/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.utils;

import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventBlockRenderSide;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.utils.chat.Helper;
import java.util.ArrayList;
import net.minecraft.block.BlockBed;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

public class BedUtil {
    public final ArrayList<BlockPos> list = new ArrayList();

    public void update() {
        this.list.clear();
        Minecraft.getMinecraft().renderGlobal.loadRenderers();
    }

    boolean isOneBed(BlockPos pos) {
        for (BlockPos bpos : this.list) {
            if (bpos.getX() < 0 && pos.getX() < 0 && bpos.getZ() == pos.getZ() && Math.max(Math.abs(bpos.getX()), Math.abs(pos.getX())) - Math.min(Math.abs(bpos.getX()), Math.abs(pos.getX())) == 1) {
                return true;
            }
            if (bpos.getX() > 0 && pos.getX() > 0 && bpos.getZ() == pos.getZ() && Math.max(bpos.getX(), pos.getX()) - Math.min(bpos.getX(), pos.getX()) == 1) {
                return true;
            }
            if (bpos.getX() > 0 && pos.getX() < 0 && bpos.getZ() == pos.getZ() && Math.max(bpos.getX(), Math.abs(pos.getX())) - Math.min(bpos.getX(), Math.abs(pos.getX())) == 1) {
                return true;
            }
            if (bpos.getX() < 0 && pos.getX() > 0 && bpos.getZ() == pos.getZ() && Math.max(Math.abs(bpos.getX()), pos.getX()) - Math.min(Math.abs(bpos.getX()), pos.getX()) == 1) {
                return true;
            }
            if (bpos.getZ() < 0 && pos.getZ() < 0 && bpos.getX() == pos.getX() && Math.max(Math.abs(bpos.getZ()), Math.abs(pos.getZ())) - Math.min(Math.abs(bpos.getZ()), Math.abs(pos.getZ())) == 1) {
                return true;
            }
            if (bpos.getZ() > 0 && pos.getZ() > 0 && bpos.getX() == pos.getX() && Math.max(bpos.getZ(), pos.getZ()) - Math.min(bpos.getZ(), pos.getZ()) == 1) {
                return true;
            }
            if (bpos.getZ() > 0 && pos.getZ() < 0 && bpos.getX() == pos.getX() && Math.max(bpos.getZ(), Math.abs(pos.getZ())) - Math.min(bpos.getZ(), Math.abs(pos.getZ())) == 1) {
                return true;
            }
            if (bpos.getZ() >= 0 || pos.getZ() <= 0 || bpos.getX() != pos.getX() || Math.max(Math.abs(bpos.getZ()), pos.getZ()) - Math.min(Math.abs(bpos.getZ()), pos.getZ()) != 1) continue;
            return true;
        }
        return false;
    }

    @EventHandler
    public void onRenderBlock(EventBlockRenderSide eventRenderBlock) {
        BlockPos blockPos = new BlockPos(eventRenderBlock.pos.offset(eventRenderBlock.getSide(), -1).getX(), eventRenderBlock.pos.offset(eventRenderBlock.getSide(), -1).getY() + 1, eventRenderBlock.pos.offset(eventRenderBlock.getSide(), -1).getZ());
        if (!this.list.contains(blockPos)) {
            Minecraft.getMinecraft();
            if (Minecraft.theWorld.getBlockState(blockPos).getBlock() instanceof BlockBed && !this.isOneBed(blockPos)) {
                this.list.add(blockPos);
                StringBuilder stringBuilder = new StringBuilder().append(blockPos).append(" ");
                Minecraft.getMinecraft();
                Helper.sendMessageWithoutPrefix(stringBuilder.append(Minecraft.theWorld.getBlockState(blockPos).getBlock().toString()).toString());
            }
        }
    }

    @EventHandler
    public void onUpdate(EventPreUpdate eventUpdate) {
        for (int i = 0; i < this.list.size(); ++i) {
            BlockPos blockPos = this.list.get(i);
            Minecraft.getMinecraft();
            if (Minecraft.theWorld.getBlockState(blockPos).getBlock() instanceof BlockBed) continue;
            this.list.remove(blockPos);
        }
    }
}

