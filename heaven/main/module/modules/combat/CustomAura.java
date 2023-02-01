/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.module.modules.combat;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.More.PacketEvent;
import heaven.main.event.events.rendering.EventRender3D;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.management.FriendManager;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.AntiBot;
import heaven.main.module.modules.combat.utils.AuraCore;
import heaven.main.module.modules.misc.Teams;
import heaven.main.module.modules.render.Rotate;
import heaven.main.ui.RenderRotate;
import heaven.main.ui.gui.hud.notification.ClientNotification;
import heaven.main.ui.simplecore.SimpleRender;
import heaven.main.utils.math.MathUtil;
import heaven.main.utils.math.RotationUtil;
import heaven.main.utils.render.RenderUtil;
import heaven.main.utils.render.RenderUtils;
import heaven.main.utils.render.color.Colors;
import heaven.main.utils.timer.TimerUtil;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

public class CustomAura
extends Module {
    private final String[] AuraPriority = new String[]{"Angle", "Range", "Fov", "Health", "LivingTime", "Armor", "Slowly"};
    private final String[] AuraMode = new String[]{"Switch", "Single", "Multi", "MultiNCP"};
    private final String[] BlockMode = new String[]{"Vanilla", "NCP", "AAC", "Verus", "Watchdog"};
    private final String[] AttackModes = new String[]{"Packet", "Controller", "Mouse"};
    private static final String[] marks = new String[]{"Normal", "Exhibition"};
    public final Mode<String> priority = new Mode("Priority", this.AuraPriority, this.AuraPriority[0]);
    public final Mode<String> mode = new Mode("Mode", this.AuraMode, this.AuraMode[0]);
    private final Option<Boolean> autoBlock = new Option<Boolean>("AutoBlock", true);
    public final Mode<String> blockmode = new Mode("BlockMode", this.BlockMode, this.BlockMode[0], this.autoBlock::getValue);
    public final Mode<String> attackmode = new Mode("AttackMode", this.AttackModes, this.AttackModes[0]);
    private final Option<Boolean> players = new Option<Boolean>("Players", true);
    private final Option<Boolean> mobs = new Option<Boolean>("Mobs", false);
    private final Option<Boolean> animals = new Option<Boolean>("Animals", false);
    private final Option<Boolean> invis = new Option<Boolean>("Invisible", false);
    private final Option<Boolean> dead = new Option<Boolean>("Dead", false);
    public static final Option<Boolean> sprint = new Option<Boolean>("KeepSprint", true);
    private final Numbers<Double> range = new Numbers<Double>("AttackRange", 4.2, 0.1, 10.0, 0.1);
    private final Numbers<Double> blockrange = new Numbers<Double>("BlockRange", 8.0, 3.5, 12.0, 0.1);
    private final Numbers<Double> maxcps = new Numbers<Double>("MaxCPS", 9.0, 1.0, 50.0, 1.0);
    private final Numbers<Double> mincps = new Numbers<Double>("MinCPS", 9.0, 1.0, 50.0, 1.0);
    private final Numbers<Double> hitsBeforeSwitch = new Numbers<Double>("SwitchHits", 3.0, 0.0, 30.0, 1.0);
    private final Numbers<Double> fov = new Numbers<Double>("Fov", 180.0, 1.0, 360.0, 1.0);
    private final Numbers<Double> hitchance = new Numbers<Double>("HitChance", 100.0, 0.0, 100.0, 5.0);
    private final Option<Boolean> walls = new Option<Boolean>("ThroughWalls", true);
    private final Option<Boolean> dc = new Option<Boolean>("ImitateDC", false);
    private final Option<Boolean> particle = new Option<Boolean>("Particle", false);
    private final Option<Boolean> aac = new Option<Boolean>("AACHit", false);
    private static final Option<Boolean> rot = new Option<Boolean>("Rotations", true);
    private final Option<Boolean> raytrace = new Option<Boolean>("RayTrace", false);
    public static final Numbers<Double> rotrange = new Numbers<Double>("RotationsRange", 8.0, 3.5, 12.0, 0.1, rot::getValue);
    private final Numbers<Double> maxturnspeed = new Numbers<Double>("MaxTurnSpeed", 120.0, 0.0, 180.0, 1.0, rot::getValue);
    private final Numbers<Double> minturnspeed = new Numbers<Double>("MinTurnSpeed", 80.0, 0.0, 180.0, 1.0, rot::getValue);
    private final Option<Boolean> smooth = new Option<Boolean>("SmoothBack", true, rot::getValue);
    private final Numbers<Double> maxbackspeed = new Numbers<Double>("MaxBackSpeed", 120.0, 0.0, 180.0, 1.0, this.smooth::getValue);
    private final Numbers<Double> minbackspeed = new Numbers<Double>("MinBackSpeed", 120.0, 0.0, 180.0, 1.0, this.smooth::getValue);
    private final Option<Boolean> slientrot = new Option<Boolean>("SilentRotations", true, rot::getValue);
    private final Option<Boolean> downrot = new Option<Boolean>("RotationsDown", false, rot::getValue);
    public static final Option<Boolean> rotresist = new Option<Boolean>("RotationsResist", false, rot::getValue);
    private final Option<Boolean> difference = new Option<Boolean>("RandomCenter", false, rot::getValue);
    private final Option<Boolean> attackRot = new Option<Boolean>("RotationsDelay", false, rot::getValue);
    public static final Option<Boolean> autopitch = new Option<Boolean>("AutomaticPitch", false, rot::getValue);
    private final Option<Boolean> mousecheck = new Option<Boolean>("RequiteMouseDown", false);
    private final Option<Boolean> noinvattack = new Option<Boolean>("NoInvAttack", false);
    private final Option<Boolean> fakeswing = new Option<Boolean>("FakeSwing", false);
    private final Option<Boolean> autodisable = new Option<Boolean>("DisableOnDeath", true);
    public static final Option<Boolean> rotfall = new Option<Boolean>("RotationsLower", true, rot::getValue);
    public static final Option<Boolean> rotperty = new Option<Boolean>("RotationsProperty", false, rot::getValue);
    private final Option<Boolean> packetfix = new Option<Boolean>("PacketItemFix", false);
    private final Option<Boolean> suff = new Option<Boolean>("TargetSuffix", false);
    private static final Option<Boolean> mark = new Option<Boolean>("Mark", true);
    public final Mode<String> markmode = new Mode("MarkMode", marks, marks[0], mark::getValue);
    private final Option<Boolean> rangeCircle = new Option<Boolean>("RangeCircle", false);
    final TimerUtil timer = new TimerUtil();
    final TimerUtil swingtimer = new TimerUtil();
    private long turnTime;
    int hit;
    private List<EntityLivingBase> loaded = new CopyOnWriteArrayList<EntityLivingBase>();
    private final List<EntityLivingBase> attacktargets = new CopyOnWriteArrayList<EntityLivingBase>();
    public static EntityLivingBase target;
    public static EntityLivingBase rottarget;
    public static EntityLivingBase attacktarget;
    public static boolean canRender;
    private float yaw;
    private float pitch;
    public static boolean isBlocking;

    public CustomAura() {
        super("CustomAura", ModuleType.Combat);
        this.addValues(this.priority, this.mode, this.blockmode, this.attackmode, this.markmode, this.maxcps, this.mincps, this.range, this.blockrange, rotrange, this.hitsBeforeSwitch, this.players, this.mobs, this.animals, this.invis, this.dead, sprint, this.autoBlock, this.walls, this.particle, this.aac, this.raytrace, this.dc, this.difference, this.slientrot, this.mousecheck, this.noinvattack, this.fakeswing, this.packetfix, this.suff, mark, this.rangeCircle, this.autodisable, rot, this.maxturnspeed, this.minturnspeed, this.smooth, this.maxbackspeed, this.minbackspeed, this.downrot, rotresist, rotfall, rotperty, autopitch, this.attackRot, this.fov, this.hitchance);
    }

    @Override
    public void onEnable() {
        this.turnTime = System.currentTimeMillis() - 300L;
        target = null;
        attacktarget = null;
        rottarget = null;
        isBlocking = false;
    }

    public boolean raytrace() {
        if (((Boolean)this.raytrace.getValue()).booleanValue()) {
            if (target == null) {
                return false;
            }
            float oldYaw = Minecraft.thePlayer.rotationYaw;
            float oldPitch = Minecraft.thePlayer.rotationPitch;
            Minecraft.thePlayer.rotationYaw = this.yaw;
            Minecraft.thePlayer.rotationPitch = this.pitch;
            Entity lookingAt = RotationUtil.getMouseOver(1.0f, Minecraft.thePlayer);
            if (lookingAt != target) {
                Minecraft.thePlayer.rotationYaw = oldYaw;
                Minecraft.thePlayer.rotationPitch = oldPitch;
                return false;
            }
            Minecraft.thePlayer.rotationYaw = oldYaw;
            Minecraft.thePlayer.rotationPitch = oldPitch;
        }
        return true;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (((Boolean)this.autoBlock.getValue()).booleanValue() && CustomAura.hasSword()) {
            if (Minecraft.thePlayer.isBlocking()) {
                this.UnBlock();
            }
        }
        target = null;
        rottarget = null;
        canRender = false;
        isBlocking = false;
        this.swingtimer.reset();
    }

    private static boolean hasSword() {
        if (Minecraft.thePlayer.inventory.getCurrentItem() != null) {
            return Minecraft.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword;
        }
        return false;
    }

    private boolean canBlockInRange() {
        for (Entity enti : Minecraft.theWorld.loadedEntityList) {
            if (!((double)Minecraft.thePlayer.getDistanceToEntity(enti) >= (Double)this.range.getValue())) continue;
            if (!((double)Minecraft.thePlayer.getDistanceToEntity(enti) < (Double)this.blockrange.getValue()) || !this.isValidEntity((EntityLivingBase)enti, (Double)this.range.getValue(), ((Double)this.fov.getValue()).floatValue())) continue;
            return true;
        }
        return false;
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        if (((Boolean)this.autodisable.getValue()).booleanValue()) {
            if (!Minecraft.thePlayer.isEntityAlive()) {
                isBlocking = false;
                target = null;
                attacktarget = null;
                rottarget = null;
                ClientNotification.sendClientMessage("CustomAura", "Auto disable because player not alive or death", 4000L, ClientNotification.Type.WARNING);
                this.setEnabled(false);
                return;
            }
            if (Minecraft.thePlayer.ticksExisted <= 1) {
                isBlocking = false;
                target = null;
                attacktarget = null;
                rottarget = null;
                ClientNotification.sendClientMessage("CustomAura", "Auto disable because World change or world ticks too lower", 4000L, ClientNotification.Type.WARNING);
                this.setEnabled(false);
            }
        }
    }

    @EventHandler
    private void onPreMotion(EventPreUpdate event) {
        float[] facesTarget;
        if (!((Boolean)this.suff.getValue()).booleanValue()) {
            this.setSuffix((Serializable)this.mode.getValue());
        } else {
            int aps = CustomAura.randomNumber(((Double)this.maxcps.getValue()).intValue(), ((Double)this.mincps.getValue()).intValue());
            if (target != null) {
                this.setSuffix((Serializable)((Object)(aps + " (100%)")));
            } else {
                this.setSuffix((Serializable)((Object)"0 (0%)"));
            }
        }
        if (((Boolean)this.mousecheck.getValue()).booleanValue() && !CustomAura.mc.gameSettings.keyBindAttack.isKeyDown()) {
            target = null;
            attacktarget = null;
            rottarget = null;
            return;
        }
        if (((Boolean)this.autoBlock.getValue()).booleanValue()) {
            if (this.blockmode.isCurrentMode("Watchdog") || this.blockmode.isCurrentMode("NCP")) {
                if (target == null && !this.canBlockInRange() && CustomAura.hasSword()) {
                    this.UnBlock();
                }
                if (CustomAura.hasSword() && target != null && !isBlocking) {
                    this.Block();
                    if (this.blockmode.isCurrentMode("NCP")) {
                        mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    }
                }
            } else {
                isBlocking = false;
            }
        }
        List<EntityLivingBase> sortList = this.sortList(this.getTargets(((Double)this.range.getValue()).intValue(), ((Double)this.fov.getValue()).floatValue()));
        List<EntityLivingBase> rotsortList = this.sortList(this.getTargets(((Double)rotrange.getValue()).intValue(), ((Double)this.fov.getValue()).floatValue()));
        if (sortList.isEmpty() && !this.attacktargets.isEmpty()) {
            this.attacktargets.clear();
        }
        if (((Boolean)this.noinvattack.getValue()).booleanValue() && CustomAura.mc.currentScreen instanceof GuiContainer) {
            target = null;
            rottarget = null;
            attacktarget = null;
            return;
        }
        this.loaded = this.sortList(this.getTargets((Double)this.range.getValue(), ((Double)this.fov.getValue()).floatValue()));
        List<EntityLivingBase> rotloaded = this.sortList(this.getTargets((Double)rotrange.getValue(), ((Double)this.fov.getValue()).floatValue()));
        if (rotloaded.isEmpty()) {
            rottarget = null;
        } else {
            EntityLivingBase rottarget = !rotsortList.isEmpty() && rotloaded.size() > 1 ? rotloaded.get(1) : rotloaded.get(0);
            EntityLivingBase entityLivingBase = CustomAura.rottarget = target == null ? rottarget : target;
        }
        if (this.loaded.isEmpty()) {
            target = null;
        } else {
            EntityLivingBase target = !sortList.isEmpty() && this.attacktargets == sortList.get(0) && this.loaded.size() > 1 ? this.loaded.get(1) : this.loaded.get(0);
            CustomAura.target = target;
        }
        if (rottarget != null) {
            if (!rotsortList.isEmpty()) {
                if (((Boolean)sprint.getValue()).booleanValue()) {
                    if (Minecraft.thePlayer.moving()) {
                        Minecraft.thePlayer.setSprinting(true);
                    }
                } else {
                    Minecraft.thePlayer.setSprinting(false);
                }
                if (((Boolean)rot.getValue()).booleanValue()) {
                    canRender = true;
                    float[] facesDown = RotationUtil.getRotateForAura(rottarget, this.yaw, this.pitch, 0.0f, 0.0f);
                    facesTarget = RotationUtil.getRotateForAura(rottarget, this.yaw, this.pitch, ((Double)this.minturnspeed.getValue()).floatValue(), ((Double)this.maxturnspeed.getValue()).floatValue());
                    if (facesTarget != null) {
                        if (((Boolean)this.downrot.getValue()).booleanValue()) {
                            if (facesDown != null) {
                                if (((Boolean)this.attackRot.getValue()).booleanValue()) {
                                    this.yaw = Minecraft.thePlayer.getDistanceSqToEntity(rottarget) >= 1.5 ? (this.rotticker() ? facesTarget[0] : facesDown[0]) : facesDown[0];
                                    this.pitch = Minecraft.thePlayer.getDistanceSqToEntity(rottarget) >= 1.5 ? (this.rotticker() ? facesTarget[1] : facesDown[1]) : facesDown[1];
                                } else {
                                    this.yaw = Minecraft.thePlayer.getDistanceSqToEntity(rottarget) >= 1.5 ? facesTarget[0] : facesDown[0];
                                    this.pitch = Minecraft.thePlayer.getDistanceSqToEntity(rottarget) >= 1.5 ? facesTarget[1] : facesDown[1];
                                }
                            }
                        } else if (((Boolean)this.attackRot.getValue()).booleanValue()) {
                            if (facesDown != null) {
                                this.yaw = this.rotticker() ? facesTarget[0] : facesDown[0];
                                this.pitch = this.rotticker() ? facesTarget[1] : facesDown[1];
                            }
                        } else {
                            this.yaw = facesTarget[0];
                            this.pitch = facesTarget[1];
                        }
                    }
                    if (((Boolean)this.difference.getValue()).booleanValue()) {
                        this.yaw += (float)(Math.random() * 8.0 - 4.0);
                        this.pitch += (float)(Math.random() * 12.0 - 6.0);
                    }
                    EventPreUpdate.setYaw(this.yaw);
                    EventPreUpdate.setPitch(this.pitch);
                    if (Client.instance.getModuleManager().getModuleByClass(Rotate.class).isEnabled() && Rotate.cuka.isCurrentMode("Body")) {
                        new RenderRotate(this.yaw, this.pitch);
                    }
                    if (!((Boolean)this.slientrot.getValue()).booleanValue()) {
                        Minecraft.thePlayer.rotationYawHead = this.yaw;
                        Minecraft.thePlayer.rotationYaw = this.yaw;
                        Minecraft.thePlayer.renderYawOffset = this.yaw;
                        Minecraft.thePlayer.renderArmPitch = this.pitch;
                    }
                }
            }
            this.turnTime = System.currentTimeMillis();
        } else {
            if (((Boolean)this.smooth.getValue()).booleanValue()) {
                if (System.currentTimeMillis() - this.turnTime < 700L) {
                    facesTarget = RotationUtil.getRotateForReturn(this.yaw, this.pitch, (float)MathUtil.randomNumber(((Double)this.maxbackspeed.getValue()).floatValue(), ((Double)this.minbackspeed.getValue()).floatValue()));
                    this.yaw = facesTarget[0];
                    this.pitch = facesTarget[1];
                    EventPreUpdate.setYaw(this.yaw);
                    EventPreUpdate.setPitch(this.pitch);
                    canRender = true;
                    if (Client.instance.getModuleManager().getModuleByClass(Rotate.class).isEnabled() && Rotate.cuka.isCurrentMode("Body")) {
                        new RenderRotate(this.yaw, this.pitch);
                    }
                } else {
                    canRender = false;
                    this.yaw = Minecraft.thePlayer.rotationYaw;
                    this.pitch = Minecraft.thePlayer.rotationPitch;
                }
            }
            this.attacktargets.clear();
        }
    }

    private static int randomNumber(double min, double max) {
        Random random = new Random();
        return (int)(min + random.nextDouble() * (max - min));
    }

    @EventHandler
    public void onPostMotion(EventPreUpdate e) {
        if (((Boolean)this.mousecheck.getValue()).booleanValue() && !CustomAura.mc.gameSettings.keyBindAttack.isKeyDown()) {
            target = null;
            attacktarget = null;
            rottarget = null;
            return;
        }
        if (target != null && this.shouldAttack()) {
            if (this.raytrace()) {
                this.attack();
            }
            this.timer.reset();
        }
        if (this.mode.isCurrentMode("MultiNCP") || this.mode.isCurrentMode("Multi")) {
            ++this.hit;
            if (this.hit >= 1) {
                this.attacktargets.add(rottarget);
                attacktarget = rottarget;
                this.attacktargets.add(target);
                attacktarget = target;
                this.hit = 0;
            }
        }
        if (this.blockmode.isCurrentMode("Watchdog") && this.shouldAttack() && target != null && CustomAura.hasSword()) {
            if (Minecraft.thePlayer.isBlocking() && this.isValidEntity(target, (Double)this.range.getValue(), ((Double)this.fov.getValue()).floatValue())) {
                this.UnBlock();
            }
        }
        if (this.blockmode.isCurrentMode("NCP") && this.shouldAttack() && target != null && CustomAura.hasSword()) {
            if (Minecraft.thePlayer.isBlocking() && this.isValidEntity(target, (Double)this.range.getValue(), ((Double)this.fov.getValue()).floatValue())) {
                mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                this.UnBlock();
            }
        }
        if (this.blockmode.isCurrentMode("Watchdog") || this.blockmode.isCurrentMode("NCP")) {
            if (!Minecraft.thePlayer.isBlocking() && CustomAura.hasSword() && ((Boolean)this.autoBlock.getValue()).booleanValue() && target != null) {
                this.Block();
            }
        }
        if (!this.getTargets((Double)this.blockrange.getValue(), ((Double)this.fov.getValue()).floatValue()).isEmpty() && CustomAura.canBlock() && ((Boolean)this.autoBlock.getValue()).booleanValue()) {
            if (this.blockmode.isCurrentMode("AAC")) {
                if (!isBlocking) {
                    Minecraft.thePlayer.setItemInUse(Minecraft.thePlayer.inventory.getCurrentItem(), 71999);
                }
                isBlocking = true;
            }
            if (this.blockmode.isCurrentMode("Vanilla") && target != null) {
                Minecraft.playerController.sendUseItem(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer.getHeldItem());
                Minecraft.thePlayer.setItemInUse(Minecraft.thePlayer.getHeldItem(), Minecraft.thePlayer.getHeldItem().getMaxItemUseDuration());
                isBlocking = true;
            }
            if (this.blockmode.isCurrentMode("Verus")) {
                if (!isBlocking) {
                    Minecraft.thePlayer.setItemInUse(Minecraft.thePlayer.inventory.getCurrentItem(), 71999);
                }
                isBlocking = true;
            }
        }
        if (this.blockmode.isCurrentMode("Watchdog") || this.blockmode.isCurrentMode("NCP")) {
            for (Entity enti : Minecraft.theWorld.loadedEntityList) {
                if (!((double)Minecraft.thePlayer.getDistanceToEntity(enti) >= (Double)this.range.getValue())) continue;
                if (!((double)Minecraft.thePlayer.getDistanceToEntity(enti) < (Double)this.blockrange.getValue()) || !this.isValidEntity((EntityLivingBase)enti, (Double)this.blockrange.getValue(), ((Double)this.fov.getValue()).floatValue()) || !CustomAura.hasSword()) continue;
                if (Minecraft.thePlayer.isBlocking() || !((Boolean)this.autoBlock.getValue()).booleanValue()) continue;
                this.Block();
            }
        }
    }

    private boolean rotticker() {
        int aps = CustomAura.randomNumber(((Double)this.maxcps.getValue()).intValue() + 10, ((Double)this.mincps.getValue()).intValue() - 10);
        return this.timer.hasReached(1000 / aps);
    }

    private boolean shouldAttack() {
        if (this.isValidEntity(target, (Double)this.range.getValue(), ((Double)this.fov.getValue()).floatValue())) {
            int aps = CustomAura.randomNumber(((Double)this.maxcps.getValue()).intValue(), ((Double)this.mincps.getValue()).intValue());
            return this.timer.hasReached(1000 / aps);
        }
        return false;
    }

    @EventHandler
    public void onSend(PacketEvent event) {
        if (((Boolean)this.packetfix.get()).booleanValue() && event.getState().equals((Object)PacketEvent.State.OUTGOING)) {
            if (Minecraft.thePlayer.isBlocking()) {
                Packet<INetHandlerPlayServer> packet;
                if (event.getPacket() instanceof C07PacketPlayerDigging && ((C07PacketPlayerDigging)(packet = (C07PacketPlayerDigging)event.getPacket())).getStatus().equals((Object)C07PacketPlayerDigging.Action.RELEASE_USE_ITEM)) {
                    event.setCancelled(true);
                }
                if (event.getPacket() instanceof C08PacketPlayerBlockPlacement && ((C08PacketPlayerBlockPlacement)(packet = (C08PacketPlayerBlockPlacement)event.getPacket())).getPlacedBlockDirection() == 255) {
                    event.setCancelled(true);
                }
            }
        }
    }

    private void attack() {
        float modifierForCreature = EnchantmentHelper.getModifierForCreature(Minecraft.thePlayer.getHeldItem(), EnumCreatureAttribute.UNDEFINED);
        if (Minecraft.thePlayer.fallDistance > 0.0f) {
            if (!Minecraft.thePlayer.onGround) {
                if (!Minecraft.thePlayer.isOnLadder()) {
                    if (!Minecraft.thePlayer.isInWater()) {
                        Minecraft.thePlayer.isPotionActive(Potion.blindness);
                    }
                }
            }
        }
        if (((Boolean)this.aac.getValue()).booleanValue()) {
            for (Entity ent : Minecraft.theWorld.loadedEntityList) {
                if (!((double)Minecraft.thePlayer.getDistanceToEntity(ent) <= (Double)this.range.getValue()) || !(ent instanceof EntityMob) || !ent.isInvisible() || !(target.getDistanceToEntity(ent) < 1.0f)) continue;
                target = (EntityLivingBase)ent;
            }
        }
        if (((Boolean)this.noinvattack.getValue()).booleanValue() && CustomAura.mc.currentScreen instanceof GuiContainer) {
            target = null;
            attacktarget = null;
            return;
        }
        if (modifierForCreature > 0.0f && ((Boolean)this.particle.getValue()).booleanValue()) {
            Minecraft.thePlayer.onEnchantmentCritical(target);
        }
        if (isBlocking && CustomAura.canBlock()) {
            isBlocking = false;
        }
        if (((Boolean)this.fakeswing.getValue()).booleanValue()) {
            Minecraft.thePlayer.swingItem();
            Minecraft.thePlayer.swingItem();
        }
        if (MathUtil.randomFloat(0.0f, 100.0f) <= (double)((Double)this.hitchance.getValue()).floatValue()) {
            Minecraft.thePlayer.swingItem();
            if (((Boolean)this.dc.getValue()).booleanValue() && CustomAura.target.hurtResistantTime > 12 && (double)CustomAura.target.hurtResistantTime < 17.0 + Math.random() * (Math.random() * 2.0 - 1.0)) {
                return;
            }
            if (this.mode.isCurrentMode("Multi")) {
                for (EntityLivingBase target : this.loaded) {
                    if (this.attackmode.isCurrentMode("Packet")) {
                        Minecraft.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity((Entity)target, C02PacketUseEntity.Action.ATTACK));
                    }
                    if (this.attackmode.isCurrentMode("Controller")) {
                        Minecraft.playerController.attackEntity(Minecraft.thePlayer, target);
                    }
                    if (!this.attackmode.isCurrentMode("Mouse")) continue;
                    Minecraft.playerController.attackEntity(Minecraft.thePlayer, target);
                    mc.clickMouse();
                }
            } else {
                if (this.attackmode.isCurrentMode("Packet")) {
                    Minecraft.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity((Entity)target, C02PacketUseEntity.Action.ATTACK));
                }
                if (this.attackmode.isCurrentMode("Controller")) {
                    Minecraft.playerController.attackEntity(Minecraft.thePlayer, target);
                }
                if (this.attackmode.isCurrentMode("Mouse")) {
                    mc.clickMouse();
                }
            }
        }
        if (this.mode.isCurrentMode("Switch")) {
            ++this.hit;
            if ((double)this.hit >= (Double)this.hitsBeforeSwitch.getValue()) {
                this.attacktargets.add(rottarget);
                attacktarget = rottarget;
                this.attacktargets.add(target);
                attacktarget = target;
                this.hit = 0;
            }
        }
        if (CustomAura.canBlock() && ((Boolean)this.autoBlock.getValue()).booleanValue()) {
            if (this.blockmode.isCurrentMode("AAC")) {
                Minecraft.thePlayer.setItemInUse(Minecraft.thePlayer.inventory.getCurrentItem(), 71999);
            }
            if (this.blockmode.isCurrentMode("Vanilla")) {
                Minecraft.playerController.sendUseItem(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer.getHeldItem());
                Minecraft.thePlayer.setItemInUse(Minecraft.thePlayer.getHeldItem(), Minecraft.thePlayer.getHeldItem().getMaxItemUseDuration());
            }
            if (this.blockmode.isCurrentMode("Verus") && CustomAura.target.hurtResistantTime == 0) {
                Minecraft.thePlayer.setItemInUse(Minecraft.thePlayer.inventory.getCurrentItem(), 71999);
            }
            isBlocking = true;
        }
    }

    private List<EntityLivingBase> getTargets(double n, float n2) {
        ArrayList<EntityLivingBase> list = new ArrayList<EntityLivingBase>();
        for (Entity entity : Minecraft.thePlayer.getEntityWorld().loadedEntityList) {
            EntityLivingBase entityLivingBase;
            if (!(entity instanceof EntityLivingBase) || !this.isValidEntity(entityLivingBase = (EntityLivingBase)entity, n, n2)) continue;
            list.add(entityLivingBase);
        }
        return list;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static boolean canBlock() {
        if (Minecraft.thePlayer.getHeldItem() == null) return false;
        if (!(Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemSword)) return false;
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean isValidEntity(EntityLivingBase entity, double n, float n2) {
        if (entity == null) {
            return false;
        }
        if (entity == Minecraft.thePlayer) {
            return false;
        }
        if (!entity.isEntityAlive() && (double)entity.getHealth() == 0.0) {
            return false;
        }
        if (entity instanceof EntityPlayer && !((Boolean)this.players.getValue()).booleanValue()) {
            return false;
        }
        if ((entity instanceof EntityAnimal || entity instanceof EntityVillager || entity instanceof EntitySquid || entity instanceof EntityArmorStand) && !((Boolean)this.animals.getValue()).booleanValue()) {
            return false;
        }
        if ((entity instanceof EntityMob || entity instanceof EntityBat || entity instanceof EntityDragon || entity instanceof EntityVillager || entity instanceof EntityGolem) && !((Boolean)this.mobs.getValue()).booleanValue()) {
            return false;
        }
        if ((double)Minecraft.thePlayer.getDistanceToEntity(entity) > n) {
            return false;
        }
        if (entity.isInvisible() && !((Boolean)this.invis.getValue()).booleanValue()) {
            return false;
        }
        if (entity.isDead && !((Boolean)this.dead.getValue()).booleanValue()) {
            return false;
        }
        if (entity instanceof EntityPlayer && Teams.isOnSameTeam(entity) && Client.instance.getModuleManager().getModuleByClass(Teams.class).isEnabled()) {
            return false;
        }
        if (FriendManager.isFriend(entity.getName()) && entity instanceof EntityPlayer) {
            return false;
        }
        boolean b = !Minecraft.thePlayer.canEntityBeSeen(entity);
        boolean b2 = (Boolean)this.walls.getValue() == false;
        if (b & b2) return false;
        float[] fArray = new float[]{n2, 90.0f};
        if (!(AuraCore.angleDifference(RotationUtil.getEntityRotations(entity, fArray, 180)[0], n2) <= n2)) return false;
        if (!(entity instanceof EntityPlayer)) return true;
        if (AntiBot.isBot(entity)) return false;
        return true;
    }

    private List<EntityLivingBase> sortList(List<EntityLivingBase> list) {
        if (this.priority.isCurrentMode("Range")) {
            list.sort(Comparator.comparingDouble(o -> o.getDistanceToEntity(Minecraft.thePlayer)));
        }
        if (this.priority.isCurrentMode("Fov")) {
            list.sort(Comparator.comparingDouble(o -> Math.abs(AuraCore.getYawChange(360.0f, o.posX, o.posZ))));
        }
        if (this.priority.isCurrentMode("Armor")) {
            list.sort(Comparator.comparingInt(o -> o instanceof EntityPlayer ? ((EntityPlayer)o).inventory.getTotalArmorValue() : (int)o.getHealth()));
        }
        if (this.priority.isCurrentMode("LivingTime")) {
            list.sort(Comparator.comparingInt(o -> o.ticksExisted));
        }
        if (this.priority.isCurrentMode("Angle")) {
            list.sort(Comparator.comparingDouble(o -> AuraCore.getRotationNeededHypixelBetter(o)[0]));
        }
        if (this.priority.isCurrentMode("Slowly")) {
            list.sort((ent1, ent2) -> {
                float f2 = 0.0f;
                float e1 = RotationUtil.getRotations(ent1)[0];
                float e2 = RotationUtil.getRotations(ent2)[0];
                return e1 < f2 ? 1 : (e1 == e2 ? 0 : -2);
            });
        }
        if (this.priority.isCurrentMode("Health")) {
            list.sort(Comparator.comparingDouble(EntityLivingBase::getHealth));
        }
        return list;
    }

    @EventHandler
    private void render(EventRender3D e) {
        if (((Boolean)this.rangeCircle.getValue()).booleanValue()) {
            GL11.glPushMatrix();
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glEnable((int)2848);
            GL11.glLineWidth((float)2.0f);
            GL11.glDisable((int)3553);
            GL11.glEnable((int)2884);
            GL11.glDisable((int)2929);
            AxisAlignedBB Item2 = new AxisAlignedBB(-0.175, 0.0, -0.175, 0.175, 0.35, 0.175);
            GL11.glPushMatrix();
            GL11.glTranslated((double)SimpleRender.getEntityRenderX(Minecraft.thePlayer), (double)SimpleRender.getEntityRenderY(Minecraft.thePlayer), (double)SimpleRender.getEntityRenderZ(Minecraft.thePlayer));
            GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            SimpleRender.drawCircle((int)Item2.minX, (int)Item2.minY, ((Double)this.range.getValue()).intValue(), 1.0f, false, new Color(255, 255, 255).getRGB());
            GL11.glPopMatrix();
            GL11.glColor4f((float)0.0f, (float)0.0f, (float)1.0f, (float)1.0f);
            GL11.glEnable((int)2929);
            GL11.glEnable((int)3553);
            GL11.glDisable((int)3042);
            GL11.glDisable((int)2848);
            GL11.glPopMatrix();
        }
        if (((Boolean)mark.getValue()).booleanValue()) {
            if (this.markmode.isCurrentMode("Normal") && target != null) {
                if ((double)Minecraft.thePlayer.getDistanceToEntity(target) > (Double)this.range.getValue()) {
                    return;
                }
                Color color = new Color(255, 255, 255);
                if (CustomAura.target.hurtResistantTime > 0) {
                    color = new Color(Colors.RED.c);
                }
                double x = CustomAura.target.lastTickPosX + (CustomAura.target.posX - CustomAura.target.lastTickPosX) * (double)CustomAura.mc.timer.renderPartialTicks - RenderManager.renderPosX;
                double y = CustomAura.target.lastTickPosY + (CustomAura.target.posY - CustomAura.target.lastTickPosY) * (double)CustomAura.mc.timer.renderPartialTicks - RenderManager.renderPosY;
                double z = CustomAura.target.lastTickPosZ + (CustomAura.target.posZ - CustomAura.target.lastTickPosZ) * (double)CustomAura.mc.timer.renderPartialTicks - RenderManager.renderPosZ;
                if (target instanceof EntityPlayer) {
                    x -= 0.5;
                    z -= 0.5;
                    double d = (double)target.getEyeHeight() + 0.35;
                    double d2 = target.isSneaking() ? 0.25 : 0.0;
                    double mid = 0.5;
                    GL11.glPushMatrix();
                    GL11.glEnable((int)3042);
                    GL11.glBlendFunc((int)770, (int)771);
                    GL11.glTranslated((double)(x + mid), (double)((y += d - d2) + mid), (double)(z + mid));
                    GL11.glRotated((double)(-CustomAura.target.rotationYaw % 360.0f), (double)0.0, (double)1.0, (double)0.0);
                    GL11.glTranslated((double)(-(x + mid)), (double)(-(y + mid)), (double)(-(z + mid)));
                    GL11.glDisable((int)3553);
                    GL11.glEnable((int)2848);
                    GL11.glDisable((int)2929);
                    GL11.glDepthMask((boolean)false);
                    GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)0.5f);
                    RenderUtil.drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 0.05, z + 1.0));
                    GL11.glDisable((int)2848);
                    GL11.glEnable((int)3553);
                    GL11.glEnable((int)2929);
                    GL11.glDepthMask((boolean)true);
                    GL11.glDisable((int)3042);
                    GL11.glPopMatrix();
                } else {
                    double width = CustomAura.target.getEntityBoundingBox().maxZ - CustomAura.target.getEntityBoundingBox().minZ;
                    double height = 0.1;
                    float red = 0.0f;
                    float green = 0.5f;
                    float blue = 1.0f;
                    float alpha = 0.5f;
                    float lineRed = 0.0f;
                    float lineGreen = 0.5f;
                    float lineBlue = 1.0f;
                    float lineAlpha = 1.0f;
                    float lineWidth = 2.0f;
                    RenderUtil.drawEntityESP(x, y + (double)target.getEyeHeight() + 0.25, z, width, height, red, green, blue, alpha, lineRed, lineGreen, lineBlue, lineAlpha, lineWidth);
                }
            }
            if (this.markmode.isCurrentMode("Exhibiton")) {
                double n = CustomAura.target.lastTickPosX + (CustomAura.target.posX - CustomAura.target.lastTickPosX) * (double)e.getPartialTicks();
                double posX = n - RenderManager.renderPosX;
                double n2 = CustomAura.target.lastTickPosY + (CustomAura.target.posY - CustomAura.target.lastTickPosY) * (double)e.getPartialTicks();
                double posY = n2 - RenderManager.renderPosY;
                double n3 = CustomAura.target.lastTickPosZ + (CustomAura.target.posZ - CustomAura.target.lastTickPosZ) * (double)e.getPartialTicks();
                double posZ = n3 - RenderManager.renderPosZ;
                RenderUtils.drawCylinderESP(target, posX, posY + (double)2.2f, posZ);
            }
        }
    }

    private void Block() {
        if (Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            KeyBinding.setKeyBindState(CustomAura.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
            if (Minecraft.playerController.sendUseItem(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer.inventory.getCurrentItem())) {
                mc.getItemRenderer().resetEquippedProgress2();
            }
            isBlocking = true;
        }
    }

    private void UnBlock() {
        if (Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemSword && isBlocking) {
            KeyBinding.setKeyBindState(CustomAura.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
            Minecraft.playerController.onStoppedUsingItem(Minecraft.thePlayer);
            isBlocking = false;
        }
    }
}

