/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.utils;

import java.util.WeakHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;

public class AnimationHandler {
    WeakHashMap<RenderChunk, AnimationData> timeStamps = new WeakHashMap();

    public void preRender(RenderChunk renderChunk) {
        if (this.timeStamps.containsKey(renderChunk)) {
            int animationDuration;
            long timeDif;
            EnumFacing chunkFacing;
            AnimationData animationData = this.timeStamps.get(renderChunk);
            long time = animationData.timeStamp;
            if (time == -1L) {
                animationData.timeStamp = time = System.currentTimeMillis();
                Minecraft.getMinecraft();
                BlockPos zeroedPlayerPosition = Minecraft.thePlayer.getPosition();
                zeroedPlayerPosition = zeroedPlayerPosition.add(0, -zeroedPlayerPosition.getY(), 0);
                BlockPos zeroedCenteredChunkPos = renderChunk.getPosition().add(8, -renderChunk.getPosition().getY(), 8);
                BlockPos dif = zeroedPlayerPosition.subtract(zeroedCenteredChunkPos);
                int difX = Math.abs(dif.getX());
                int difZ = Math.abs(dif.getZ());
                chunkFacing = difX > difZ ? (dif.getX() > 0 ? EnumFacing.EAST : EnumFacing.WEST) : (dif.getZ() > 0 ? EnumFacing.SOUTH : EnumFacing.NORTH);
                animationData.chunkFacing = chunkFacing;
            }
            if ((timeDif = System.currentTimeMillis() - time) < (long)(animationDuration = 1000)) {
                double chunkY = renderChunk.getPosition().getY();
                chunkFacing = animationData.chunkFacing;
                if (chunkFacing != null) {
                    Vec3i vec = chunkFacing.getDirectionVec();
                    double mod = -(200.0 - 200.0 / (double)animationDuration * (double)timeDif);
                    GlStateManager.translate((double)vec.getX() * mod, 0.0, (double)vec.getZ() * mod);
                }
            } else {
                this.timeStamps.remove(renderChunk);
            }
        }
    }

    public void setPosition(RenderChunk renderChunk, BlockPos position) {
        Minecraft.getMinecraft();
        if (Minecraft.thePlayer != null) {
            Minecraft.getMinecraft();
            BlockPos zeroedPlayerPosition = Minecraft.thePlayer.getPosition();
            zeroedPlayerPosition = zeroedPlayerPosition.add(0, -zeroedPlayerPosition.getY(), 0);
            BlockPos zeroedCenteredChunkPos = position.add(8, -position.getY(), 8);
            EnumFacing chunkFacing = null;
            AnimationData animationData = new AnimationData(-1L, chunkFacing);
            this.timeStamps.put(renderChunk, animationData);
        }
    }

    private class AnimationData {
        public long timeStamp;
        public EnumFacing chunkFacing;

        public AnimationData(long timeStamp, EnumFacing chunkFacing) {
            this.timeStamp = timeStamp;
            this.chunkFacing = chunkFacing;
        }
    }
}

