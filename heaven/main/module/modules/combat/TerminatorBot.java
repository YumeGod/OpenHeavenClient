/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.combat;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.ui.gui.hud.notification.ClientNotification;
import heaven.main.utils.math.RotationUtil;
import heaven.main.utils.timer.TimerUtil;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public class TerminatorBot
extends Module {
    private final Numbers<Double> cps = new Numbers<Double>("CPS", 10.0, 1.0, 20.0, 1.0);
    private final Numbers<Double> range = new Numbers<Double>("Range", 10.0, 0.0, 30.0, 0.1);
    private final Option<Boolean> player = new Option<Boolean>("Players", true);
    private final Option<Boolean> monster = new Option<Boolean>("Monster", true);
    private final Option<Boolean> animal = new Option<Boolean>("Animals", false);
    private final Option<Boolean> timerFast = new Option<Boolean>("Fast", false);
    private final Option<Boolean> keepJump = new Option<Boolean>("KeepJump", false);
    private final Option<Boolean> targetCheck = new Option<Boolean>("CheckBadTarget", false);
    private final TimerUtil cpsTimerUtil = new TimerUtil();
    private EntityLivingBase lastEntity;

    public TerminatorBot() {
        super("TerminatorBot", ModuleType.Combat);
        this.addValues(this.cps, this.range, this.player, this.monster, this.animal, this.keepJump, this.timerFast);
    }

    @EventHandler
    public void onPre(EventPreUpdate e) {
        CopyOnWriteArrayList<EntityLivingBase> inRangeEntities = new CopyOnWriteArrayList<EntityLivingBase>();
        for (Entity entity : Minecraft.theWorld.loadedEntityList) {
            if (!((double)Minecraft.thePlayer.getDistanceToEntity(entity) <= (Double)this.range.getValue()) || !this.shouldAdd(entity) || !(entity instanceof EntityLivingBase)) continue;
            inRangeEntities.add((EntityLivingBase)entity);
        }
        inRangeEntities.sort((e1, e2) -> (int)(Minecraft.thePlayer.getDistanceToEntity((Entity)e1) - Minecraft.thePlayer.getDistanceToEntity((Entity)e2)));
        if (!inRangeEntities.isEmpty()) {
            EntityLivingBase currentEntity = (EntityLivingBase)inRangeEntities.get(0);
            if (((Boolean)this.timerFast.get()).booleanValue()) {
                if (currentEntity != null) {
                    if (Minecraft.thePlayer.fallDistance < 2.0f) {
                        TerminatorBot.mc.timer.timerSpeed = 1.2f;
                    }
                } else if (TerminatorBot.mc.timer.timerSpeed != 1.0f) {
                    TerminatorBot.mc.timer.timerSpeed = 1.0f;
                }
            }
            if (this.lastEntity == null || this.lastEntity != currentEntity) {
                this.lastEntity = currentEntity;
                this.renderInfo("Terminator", "New attack target is " + currentEntity.getName(), 4000L, ClientNotification.Type.INFO);
            }
            if (((Boolean)this.targetCheck.get()).booleanValue() && (currentEntity.motionX == 0.0 && currentEntity.motionZ == 0.0 || currentEntity.moveForward == 0.0f || currentEntity.moveStrafing == 0.0f)) {
                currentEntity = null;
                return;
            }
            if (!currentEntity.isEntityAlive()) {
                inRangeEntities.clear();
                return;
            }
            float[] rotationValue = RotationUtil.getPredictedRotations(currentEntity);
            Minecraft.thePlayer.rotationYaw = rotationValue[0];
            Minecraft.thePlayer.rotationPitch = rotationValue[1];
            if (((Boolean)this.keepJump.get()).booleanValue()) {
                KeyBinding.setKeyBindState(TerminatorBot.mc.gameSettings.keyBindJump.getKeyCode(), true);
            }
            KeyBinding.setKeyBindState(TerminatorBot.mc.gameSettings.keyBindForward.getKeyCode(), true);
            if (this.shouldAttack()) {
                mc.clickMouse();
            }
        }
    }

    @Override
    public void onDisable() {
        if (((Boolean)this.timerFast.get()).booleanValue()) {
            TerminatorBot.mc.timer.timerSpeed = 1.0f;
        }
        super.onDisable();
    }

    private boolean shouldAttack() {
        int APS = 20 / ((Double)this.cps.getValue()).intValue();
        if (this.cpsTimerUtil.hasReached(50 * APS)) {
            this.cpsTimerUtil.reset();
            return true;
        }
        return false;
    }

    private boolean shouldAdd(Entity entity) {
        if (entity == Minecraft.thePlayer) {
            return false;
        }
        if (!entity.isEntityAlive()) {
            return false;
        }
        if (entity instanceof EntityPlayer && ((Boolean)this.player.getValue()).booleanValue()) {
            return true;
        }
        if (entity instanceof EntityMob && ((Boolean)this.monster.getValue()).booleanValue()) {
            return true;
        }
        return entity instanceof EntityAnimal && (Boolean)this.animal.getValue() != false;
    }
}

