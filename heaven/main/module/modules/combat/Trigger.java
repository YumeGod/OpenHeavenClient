/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.combat;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.math.MathUtil;
import heaven.main.utils.timer.TimerUtil;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public class Trigger
extends Module {
    public final TimerUtil time = new TimerUtil();
    private final Option<Boolean> bRaycast = new Option<Boolean>("ThroughWalls", false);
    private final Numbers<Double> maxCPS = new Numbers<Double>("MaxCPS", 10.0, 1.0, 20.0, 1.0);
    private final Numbers<Double> minCPS = new Numbers<Double>("MinCPS", 10.0, 1.0, 20.0, 1.0);
    private final Option<Boolean> players = new Option<Boolean>("Players", true);
    private final Option<Boolean> mobs = new Option<Boolean>("Mobs", true);
    private final Option<Boolean> animals = new Option<Boolean>("Animals", false);

    public Trigger() {
        super("Trigger", ModuleType.Combat);
        this.addValues(this.bRaycast, this.players, this.mobs, this.animals, this.maxCPS, this.minCPS);
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        Entity onPoint;
        if (Trigger.mc.objectMouseOver != null && (onPoint = Trigger.mc.objectMouseOver.entityHit) != null) {
            boolean ray = false;
            if (((Boolean)this.bRaycast.getValue()).booleanValue() && !Trigger.findRaycast(onPoint).isEmpty()) {
                onPoint = (Entity)Trigger.findRaycast(onPoint).get(0);
                ray = true;
            }
            if (this.entitycheck(onPoint) && !ray && this.time.hasReached(this.CPStoDelay())) {
                this.hitEntity();
            }
        }
    }

    private boolean entitycheck(Entity e) {
        if (!e.isEntityAlive()) {
            return false;
        }
        if (((Boolean)this.players.getValue()).booleanValue() && e instanceof EntityPlayer) {
            return true;
        }
        if (((Boolean)this.mobs.getValue()).booleanValue() && e instanceof EntityMob) {
            return true;
        }
        return (Boolean)this.animals.getValue() != false && e instanceof EntityAnimal;
    }

    private void hitEntity() {
        mc.clickMouse();
        this.time.reset();
    }

    private static List findRaycast(Entity e) {
        ArrayList<Entity> arrayList = new ArrayList<Entity>();
        for (Entity rs : Minecraft.theWorld.loadedEntityList) {
            if ((double)rs.getDistanceToEntity(e) > 0.5 || !rs.isInvisible()) continue;
            arrayList.add(rs);
        }
        return arrayList;
    }

    private long CPStoDelay() {
        return (long)(MathUtil.randomNumber((Double)this.maxCPS.getValue(), (Double)this.minCPS.getValue()) / MathUtil.randomNumber((Double)this.maxCPS.getValue(), (Double)this.minCPS.getValue()) * 40.0);
    }
}

