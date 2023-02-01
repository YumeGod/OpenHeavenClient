/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.combat;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventAttack;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.BlockUtils;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.util.Random;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;

public class Reach
extends Module {
    public static final Numbers<Double> maxrange = new Numbers<Double>("MaxRange", 4.0, 3.0, 8.0, 0.1);
    public static final Numbers<Double> minrange = new Numbers<Double>("MinRange", 3.0, 3.0, 8.0, 0.1);
    public static final Option<Boolean> OnlySprint = new Option<Boolean>("OnlySprint", false);
    public static final Option<Boolean> Onlymoving = new Option<Boolean>("OnlyMoving", false);
    public static final Option<Boolean> Stopinwater = new Option<Boolean>("DisableInWater", false);
    private static final Option<Boolean> combo = new Option<Boolean>("Combo", false);
    protected final Random rand = new Random();
    public static double currentRange = 3.0;
    public static long lastAttack;
    protected long lastMS = -1L;

    public Reach() {
        super("Reach", ModuleType.Combat);
        this.addValues(maxrange, minrange, combo, OnlySprint, Onlymoving, Stopinwater);
    }

    public boolean hasTimePassedMS(long MS) {
        return this.getCurrentMS() >= this.lastMS + MS;
    }

    public void updatebefore() {
        this.lastMS = this.getCurrentMS();
    }

    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    @EventHandler
    public void onAttack(EventAttack event) {
        if (event.entity != null) {
            lastAttack = System.currentTimeMillis();
        }
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        if (this.hasTimePassedMS(2000L)) {
            double rangeMin = (Double)minrange.getValue();
            double rangeMax = (Double)maxrange.getValue();
            double rangeDiff = rangeMax - rangeMin;
            if (rangeDiff < 0.0) {
                return;
            }
            currentRange = rangeMin + this.rand.nextDouble() * rangeDiff;
            this.updatebefore();
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean check() {
        if (((Boolean)Stopinwater.get()).booleanValue()) {
            if (Minecraft.thePlayer.isInWater()) return false;
            if (Minecraft.thePlayer.isInLava()) {
                return false;
            }
            if (BlockUtils.isOnLiquid()) return false;
            if (BlockUtils.isInLiquid()) {
                return false;
            }
            if (BlockUtils.collideBlock2(Minecraft.thePlayer.getEntityBoundingBox(), block -> block instanceof BlockLiquid)) return false;
            if (BlockUtils.collideBlock2(new AxisAlignedBB(Minecraft.thePlayer.getEntityBoundingBox().maxX, Minecraft.thePlayer.getEntityBoundingBox().maxY, Minecraft.thePlayer.getEntityBoundingBox().maxZ, Minecraft.thePlayer.getEntityBoundingBox().minX, Minecraft.thePlayer.getEntityBoundingBox().minY - 0.01, Minecraft.thePlayer.getEntityBoundingBox().minZ), block -> block instanceof BlockLiquid)) {
                return false;
            }
        }
        if (((Boolean)combo.getValue()).booleanValue() && System.currentTimeMillis() - lastAttack > 300L) {
            return false;
        }
        if (((Boolean)Onlymoving.get()).booleanValue()) {
            if (!Minecraft.thePlayer.moving()) {
                return false;
            }
        }
        if ((Boolean)OnlySprint.get() == false) return true;
        if (!Minecraft.thePlayer.isSprinting()) return false;
        return true;
    }

    public static double getMax() {
        if (Reach.check()) {
            return currentRange;
        }
        return Minecraft.playerController.getBlockReachDistance();
    }
}

