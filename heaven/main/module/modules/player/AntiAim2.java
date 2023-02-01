/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.player;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.management.FriendManager;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.AntiBot;
import heaven.main.module.modules.misc.Teams;
import heaven.main.module.modules.player.DodgeUtils.Utils;
import heaven.main.ui.RenderRotate;
import heaven.main.utils.MoveUtils;
import heaven.main.utils.math.RotationUtil;
import heaven.main.value.Option;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.util.Vec3;

public class AntiAim2
extends Module {
    private EntityArrow arrow;
    public Option<Boolean> fakeAngle = new Option<Boolean>("FakeAngle", false);
    public Option<Boolean> zitterYaw = new Option<Boolean>("ZitterYaw", true);
    public Option<Boolean> rollAA = new Option<Boolean>("RollAA", false);
    public Option<Boolean> smoothless = new Option<Boolean>("Smoothless", false);
    public Option<Boolean> autoStop = new Option<Boolean>("AutoStop", false);
    public Option<Boolean> globalsCheck = new Option<Boolean>("GlobalsCheck", false);
    private static float shouldYaw;
    private static float shouldPitch;
    private static float lastYaw;
    private static float lastPitch;
    private Entity target;
    public List<EntityLivingBase> targets = new CopyOnWriteArrayList<EntityLivingBase>();
    private final Comparator<Entity> distanceComparator = Comparator.comparingDouble(e3 -> e3.getDistanceToEntity(Minecraft.thePlayer));

    public AntiAim2() {
        super("AntiAim2", ModuleType.Player);
        this.addValues(this.fakeAngle, this.zitterYaw, this.rollAA, this.smoothless, this.globalsCheck, this.autoStop);
    }

    @Override
    public void onEnable() {
        lastYaw = Minecraft.thePlayer.rotationYaw;
        lastPitch = Minecraft.thePlayer.rotationPitch;
        shouldPitch = 85.0f;
        this.arrow = null;
        this.targets.clear();
        this.target = null;
    }

    @Override
    public void onDisable() {
        this.arrow = null;
        super.onDisable();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static boolean canConsume() {
        if (Minecraft.thePlayer.inventory.getCurrentItem() == null) return false;
        if (!(Minecraft.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow)) return false;
        return true;
    }

    @EventHandler
    private void onUpdate(EventPreUpdate event) {
        block30: {
            block28: {
                block29: {
                    if (AntiAim2.mc.gameSettings.keyBindAttack.isKeyDown() || AntiAim2.mc.gameSettings.keyBindDrop.isKeyDown()) break block28;
                    if (!AntiAim2.canConsume() || !AntiAim2.mc.gameSettings.keyBindUseItem.isKeyDown()) break block29;
                    if (Minecraft.thePlayer.getItemInUseDuration() > 20) break block28;
                }
                if (AntiAim2.canConsume() || !AntiAim2.mc.gameSettings.keyBindUseItem.isKeyDown()) break block30;
            }
            return;
        }
        if (this.arrow == null) {
            this.targets = this.getTargets(13.0);
            if (!this.targets.isEmpty()) {
                this.target = this.targets.get(0);
                if (this.target != null) {
                    float[] rots = RotationUtil.getRotationsForDown(this.target);
                    shouldYaw = -rots[0];
                }
            } else {
                shouldYaw = lastYaw;
            }
            shouldPitch = 90.0f;
        }
        for (Entity e : Minecraft.theWorld.getLoadedEntityList()) {
            if (!(e instanceof EntityArrow)) continue;
            this.arrow = (EntityArrow)e;
            if (this.arrow.shootingEntity != null) {
                if (this.arrow.shootingEntity.isEntityEqual(Minecraft.thePlayer)) {
                    this.arrow = null;
                    continue;
                }
            }
            if (this.arrow.shootingEntity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer)this.arrow.shootingEntity;
                if (Client.instance.getModuleManager().getModuleByClass(Teams.class).isEnabled() && Teams.isOnSameTeam(player)) continue;
            }
            if (!((Boolean)this.globalsCheck.get()).booleanValue() && this.arrow.inGround) continue;
            if (Minecraft.thePlayer.getDistanceSqToEntity(this.arrow) >= 20.0) {
                this.arrow = null;
                continue;
            }
            if (this.arrow == null) continue;
            double angleA = Math.toRadians(this.arrow.rotationYaw);
            Vec3 cVec = new Vec3(Minecraft.thePlayer.posX + Math.cos(angleA) * 0.5, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ - Math.sin(angleA) * 0.7);
            Vec3 cVec2 = new Vec3(Minecraft.thePlayer.posX + Math.cos(angleA) * 1.7, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ - Math.sin(angleA) * 1.7);
            Vec3 vec = new Vec3(Minecraft.thePlayer.posX + Math.cos(angleA) * 1.5, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ - Math.sin(angleA) * 1.5);
            if (!Utils.isBlockPosAir(Utils.getBlockPos(cVec)) || !Utils.isBlockPosAir(Utils.getBlockPos(cVec2))) {
                cVec = new Vec3(Minecraft.thePlayer.posX - Math.cos(angleA) * 0.5, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ + Math.sin(angleA) * 0.5);
                cVec2 = new Vec3(Minecraft.thePlayer.posX - Math.cos(angleA) * 1.7, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ + Math.sin(angleA) * 1.7);
                vec = new Vec3(Minecraft.thePlayer.posX - Math.cos(angleA) * 1.5, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ + Math.sin(angleA) * 1.5);
            } else if (Utils.isBlockPosAir(Utils.getBlockPos(vec)) && Utils.isBlockPosAir(Utils.getBlockPos(vec).down(1)) && Utils.isBlockPosAir(Utils.getBlockPos(vec).down(2))) {
                vec = new Vec3(Minecraft.thePlayer.posX - Math.cos(angleA) * 1.5, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ + Math.sin(angleA) * 1.5);
            }
            if (!Utils.isBlockPosAir(Utils.getBlockPos(cVec)) || !Utils.isBlockPosAir(Utils.getBlockPos(cVec2))) {
                return;
            }
            if (Utils.isBlockPosAir(Utils.getBlockPos(vec)) && Utils.isBlockPosAir(Utils.getBlockPos(vec).down(1)) && Utils.isBlockPosAir(Utils.getBlockPos(vec).down(2))) {
                return;
            }
            if (((Boolean)this.autoStop.get()).booleanValue()) {
                MoveUtils.stop();
            }
            shouldYaw = (float)((double)this.arrow.rotationYaw * Math.nextAfter(vec.xCoord, vec.zCoord) * 4.0);
            shouldPitch = 90.0f;
        }
        lastYaw = RotationUtil.getRotateForScaffold(shouldYaw, shouldPitch, lastYaw, lastPitch, (Boolean)this.fakeAngle.get() != false ? 0.0f : (float)((Boolean)this.smoothless.get() != false ? 120 : 180), (Boolean)this.smoothless.get() != false ? 120 : 180)[0];
        lastPitch = RotationUtil.getRotateForScaffold(shouldYaw, shouldPitch, lastYaw, lastPitch, (Boolean)this.fakeAngle.get() != false ? 0.0f : (float)((Boolean)this.smoothless.get() != false ? 120 : 180), (Boolean)this.smoothless.get() != false ? 120 : 180)[1];
        if (((Boolean)this.zitterYaw.get()).booleanValue()) {
            if (!Minecraft.thePlayer.isCollidedHorizontally) {
                if (this.isMoving()) {
                    if (MoveUtils.isOnGround(0.01)) {
                        lastYaw += (float)(Math.random() * 18.0 - 14.0);
                        lastPitch += (float)(Math.random() * 18.0 - 16.0);
                    } else {
                        lastYaw += (float)(Math.random() * 88.0 - 66.0);
                        lastPitch += (float)(Math.random() * 88.0 - 76.0);
                    }
                } else if (Minecraft.thePlayer.ticksExisted % 2 == 0) {
                    lastYaw += (float)(Math.random() * 88.0 - 66.0);
                    lastPitch += (float)(Math.random() * 88.0 - 76.0);
                } else {
                    lastYaw += (float)(Math.random() * 18.0 - 14.0);
                    lastPitch += (float)(Math.random() * 18.0 - 16.0);
                }
            } else {
                lastYaw += (float)(Math.random() * 8.0 - 4.0);
                lastPitch += (float)(Math.random() * 12.0 - 6.0);
            }
        }
        new RenderRotate(lastYaw, (Boolean)this.rollAA.get() != false ? 40.0f : 90.0f, true);
        EventPreUpdate.setYaw(lastYaw);
        EventPreUpdate.setPitch(lastPitch);
    }

    private List<EntityLivingBase> getTargets(double range) {
        ArrayList<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
        for (Entity o : Minecraft.theWorld.getLoadedEntityList()) {
            EntityLivingBase entity;
            if (!(o instanceof EntityLivingBase) || !this.validEntity(entity = (EntityLivingBase)o, range)) continue;
            targets.add(entity);
        }
        if (!targets.isEmpty()) {
            this.targets.sort(this.distanceComparator);
        }
        return targets;
    }

    private boolean validEntity(Entity ent, double reach) {
        boolean b;
        if (ent == null) {
            b = false;
        } else if (ent == Minecraft.thePlayer) {
            b = false;
        } else {
            if (ent instanceof EntityPlayer) {
                // empty if block
            }
            if (ent instanceof EntityAnimal || ent instanceof EntitySquid) {
                // empty if block
            }
            if (ent instanceof EntityMob || ent instanceof EntityVillager || ent instanceof EntityBat) {
                // empty if block
            }
            if ((double)Minecraft.thePlayer.getDistanceToEntity(ent) > reach) {
                b = false;
            } else if (ent instanceof EntityPlayer && FriendManager.isFriend(ent.getName())) {
                b = false;
            } else if (!ent.isDead && ((EntityLivingBase)ent).getHealth() > 0.0f) {
                if (ent.isInvisible()) {
                    // empty if block
                }
                b = AntiBot.isBot((EntityLivingBase)ent) ? false : !Minecraft.thePlayer.isDead && (!(ent instanceof EntityPlayer) || !Teams.isOnSameTeam(ent));
            } else {
                b = false;
            }
        }
        return b;
    }
}

