/*
 * Decompiled with CFR 0.152.
 */
package com.me.guichaguri.betterfps.clones.tileentity;

import com.me.guichaguri.betterfps.transformers.cloner.CopyMode;
import java.util.Arrays;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class BeaconLogic
extends TileEntityBeacon {
    private int tickCount = 0;

    @CopyMode(value=CopyMode.Mode.IGNORE)
    public BeaconLogic() {
    }

    @Override
    public void update() {
        --this.tickCount;
        if (this.tickCount == 100) {
            this.updateEffects(this.pos.getX(), this.pos.getY(), this.pos.getZ());
        } else if (this.tickCount <= 0) {
            this.updateBeacon();
        }
    }

    @Override
    public void updateBeacon() {
        int x = this.pos.getX();
        int y = this.pos.getY();
        int z = this.pos.getZ();
        if (this.worldObj.isRemote) {
            this.updateGlassLayers(x, y, z);
        } else {
            this.updateActivation(x, y, z);
        }
        this.updateLevels(x, y, z);
        this.updateEffects(x, y, z);
        this.tickCount = 200;
    }

    @Override
    public void updateSegmentColors() {
        int x = this.pos.getX();
        int y = this.pos.getY();
        int z = this.pos.getZ();
        if (this.worldObj.isRemote) {
            this.updateGlassLayers(x, y, z);
        } else {
            this.updateActivation(x, y, z);
        }
        this.updateLevels(x, y, z);
    }

    @Override
    public void addEffectsToPlayers() {
        this.updateEffects(this.pos.getX(), this.pos.getY(), this.pos.getZ());
    }

    private void updateEffects(int x, int y, int z) {
        if (this.isComplete && this.levels > 0 && !this.worldObj.isRemote && this.primaryEffect > 0) {
            boolean hasSecondaryEffect;
            int radius = (this.levels + 1) * 10;
            int effectLevel = 0;
            if (this.levels >= 4 && this.primaryEffect == this.secondaryEffect) {
                effectLevel = 1;
            }
            AxisAlignedBB box = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
            box = box.expand(radius, radius, radius).addCoord(0.0, this.worldObj.getHeight(), 0.0);
            Iterator<EntityPlayer> iterator = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, box).iterator();
            boolean bl = hasSecondaryEffect = this.levels >= 4 && this.primaryEffect != this.secondaryEffect && this.secondaryEffect > 0;
            while (iterator.hasNext()) {
                EntityPlayer player = iterator.next();
                player.addPotionEffect(new PotionEffect(this.primaryEffect, 180, effectLevel, true, true));
                if (!hasSecondaryEffect) continue;
                player.addPotionEffect(new PotionEffect(this.secondaryEffect, 180, 0, true, true));
            }
        }
    }

    private void updateGlassLayers(int x, int y, int z) {
        this.isComplete = true;
        this.beamSegments.clear();
        TileEntityBeacon.BeamSegment beam = new TileEntityBeacon.BeamSegment(EntitySheep.getDyeRgb(EnumDyeColor.WHITE));
        float[] oldColor = null;
        this.beamSegments.add(beam);
        int height = this.worldObj.getActualHeight();
        for (int blockY = y + 1; blockY < height; ++blockY) {
            float[] color;
            BlockPos pos = new BlockPos(x, blockY, z);
            IBlockState state = this.worldObj.getBlockState(pos);
            Block b = state.getBlock();
            if (b == Blocks.stained_glass) {
                color = EntitySheep.getDyeRgb(state.getValue(BlockStainedGlass.COLOR));
            } else if (b == Blocks.stained_glass_pane) {
                color = EntitySheep.getDyeRgb(state.getValue(BlockStainedGlassPane.COLOR));
            } else {
                if (b.getLightOpacity() >= 15) {
                    this.isComplete = false;
                    this.beamSegments.clear();
                    break;
                }
                beam.incrementHeight();
                continue;
            }
            if (oldColor != null) {
                color = new float[]{(oldColor[0] + color[0]) / 2.0f, (oldColor[1] + color[1]) / 2.0f, (oldColor[2] + color[2]) / 2.0f};
            }
            if (Arrays.equals(color, oldColor)) {
                beam.incrementHeight();
                continue;
            }
            beam = new TileEntityBeacon.BeamSegment(color);
            this.beamSegments.add(beam);
            oldColor = color;
        }
    }

    private void updateActivation(int x, int y, int z) {
        this.isComplete = true;
        int height = this.worldObj.getActualHeight();
        for (int blockY = y + 1; blockY < height; ++blockY) {
            BlockPos pos = new BlockPos(x, blockY, z);
            IBlockState state = this.worldObj.getBlockState(pos);
            Block b = state.getBlock();
            if (b.getLightOpacity() < 15) continue;
            this.isComplete = false;
            break;
        }
    }

    private void updateLevels(int x, int y, int z) {
        boolean isClient = this.worldObj.isRemote;
        int levelsOld = this.levels;
        block0: for (int lvl = 1; lvl <= 4; ++lvl) {
            this.levels = lvl;
            int blockY = y - lvl;
            if (blockY < 0) break;
            for (int blockX = x - lvl; blockX <= x + lvl; ++blockX) {
                for (int blockZ = z - lvl; blockZ <= z + lvl; ++blockZ) {
                    BlockPos blockPos = new BlockPos(blockX, blockY, blockZ);
                    Block block = this.worldObj.getBlockState(blockPos).getBlock();
                    if (block.isFullBlock()) continue;
                    --this.levels;
                    break block0;
                }
            }
            if (isClient) break;
        }
        if (this.levels == 0) {
            this.isComplete = false;
        }
        if (!isClient && this.levels == 4 && levelsOld < this.levels) {
            AxisAlignedBB box = new AxisAlignedBB(x, y, z, x, y - 4, z).expand(10.0, 5.0, 10.0);
            for (EntityPlayer entityplayer : this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, box)) {
                entityplayer.triggerAchievement(AchievementList.fullBeacon);
            }
        }
    }
}

