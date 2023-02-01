/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 */
package heaven.main.module.modules.combat;

import com.mojang.authlib.GameProfile;
import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender3D;
import heaven.main.event.events.world.EventPacketSend;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.management.FriendManager;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.AntiBot;
import heaven.main.module.modules.misc.Teams;
import heaven.main.module.modules.render.Rotate;
import heaven.main.ui.RenderRotate;
import heaven.main.utils.TPUtil;
import heaven.main.utils.math.RotationUtil;
import heaven.main.utils.render.RenderUtil;
import heaven.main.utils.timer.TimeHelper;
import heaven.main.utils.timer.TimerUtil;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class TPAura
extends Module {
    private final Numbers<Double> CPS = new Numbers<Double>("APS", 10.0, 1.0, 20.0, 0.5);
    public final Numbers<Double> range = new Numbers<Double>("Range", 20.0, 1.0, 300.0, 1.0);
    public final Numbers<Integer> maxt = new Numbers<Integer>("MaxTargets", 3, 1, 50, 1);
    private final Numbers<Double> packetDistance = new Numbers<Double>("PacketDistance", 5.0, 0.5, 10.0, 0.5);
    private final Option<Boolean> blocking = new Option<Boolean>("AutoBlock", true);
    private final Option<Boolean> showPath = new Option<Boolean>("ShowPath", true);
    public final Option<Boolean> players = new Option<Boolean>("Players", true);
    public final Option<Boolean> mobs = new Option<Boolean>("Mobs", false);
    public final Option<Boolean> animals = new Option<Boolean>("Animals", false);
    public final Option<Boolean> invis = new Option<Boolean>("Invisible", false);
    private final Mode<String> priority = new Mode("Priority", new String[]{"Distance", "Health"}, "Distance");
    private ArrayList<Vec3> path = new ArrayList();
    private ArrayList[] test = new ArrayList[50];
    private final ArrayList<Packet> packetList = new ArrayList();
    public List<EntityLivingBase> targets = new CopyOnWriteArrayList<EntityLivingBase>();
    private final TimeHelper cps = new TimeHelper();
    public static TimerUtil timer = new TimerUtil();
    private boolean isBlocking;
    private final Comparator<Entity> distanceComparator = Comparator.comparingDouble(e3 -> e3.getDistanceToEntity(Minecraft.thePlayer));
    private final Comparator<EntityLivingBase> healthComparator = Comparator.comparingDouble(EntityLivingBase::getHealth);
    private Entity target;

    public TPAura() {
        super("TPAura", ModuleType.Combat);
        this.addValues(this.priority, this.CPS, this.range, this.packetDistance, this.maxt, this.players, this.mobs, this.animals, this.invis, this.blocking, this.showPath);
    }

    @Override
    public void onEnable() {
        timer.reset();
        this.targets.clear();
        this.target = null;
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        int delayValue = 20 / ((Double)this.CPS.getValue()).intValue() * 50;
        this.targets = this.getTargets((Double)this.range.get());
        if (this.targets.isEmpty()) {
            return;
        }
        this.target = this.targets.get(0);
        if (Client.instance.getModuleManager().getModuleByClass(Rotate.class).isEnabled() && Rotate.ka.is("Body")) {
            new RenderRotate(RotationUtil.faceTarget(this.target, 600.0f, 600.0f)[0]);
        }
        if (this.cps.check(delayValue) && !this.targets.isEmpty()) {
            this.test = new ArrayList[50];
            for (int i = 0; i < 1; ++i) {
                Vec3 topFrom = new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ);
                Vec3 to = new Vec3(this.target.posX, this.target.posY, this.target.posZ);
                this.test[i] = this.path = TPUtil.computePath(topFrom, to, (Double)this.packetDistance.getValue(), false);
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C0FPacketConfirmTransaction(0, -1, false));
                PlayerCapabilities playerCapabilities = new PlayerCapabilities();
                playerCapabilities.allowFlying = true;
                playerCapabilities.isFlying = true;
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C13PacketPlayerAbilities(playerCapabilities));
                this.packetList.clear();
                for (Vec3 pathElm : this.path) {
                    int j;
                    float f2;
                    float f1;
                    float f;
                    EntityOtherPlayerMP entityOtherPlayerMP = new EntityOtherPlayerMP(Minecraft.theWorld, new GameProfile(new UUID(69L, 96L), Minecraft.thePlayer.getName()));
                    entityOtherPlayerMP.inventory = Minecraft.thePlayer.inventory;
                    entityOtherPlayerMP.inventoryContainer = Minecraft.thePlayer.inventoryContainer;
                    entityOtherPlayerMP.setPositionAndRotation(pathElm.xCoord, pathElm.yCoord, pathElm.zCoord, Minecraft.thePlayer.rotationYaw, Minecraft.thePlayer.rotationPitch);
                    if (entityOtherPlayerMP.onGround || entityOtherPlayerMP.isCollidedVertically) {
                        playerCapabilities.isFlying = false;
                        playerCapabilities.allowFlying = false;
                        Minecraft.thePlayer.sendQueue.addToSendQueue(new C13PacketPlayerAbilities(playerCapabilities));
                        Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(entityOtherPlayerMP.posX, entityOtherPlayerMP.posY, entityOtherPlayerMP.posZ, true));
                        playerCapabilities.isFlying = true;
                        playerCapabilities.allowFlying = true;
                        Minecraft.thePlayer.sendQueue.addToSendQueue(new C13PacketPlayerAbilities(playerCapabilities));
                        f = (float)(pathElm.xCoord - this.target.posX);
                        f1 = (float)(pathElm.yCoord - this.target.posY);
                        f2 = (float)(pathElm.zCoord - this.target.posZ);
                        if (!(MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2) < 2.5f)) continue;
                        for (j = 0; j <= 2; ++j) {
                            Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(RotationUtil.teleportRot(entityOtherPlayerMP, this.target, 600.0f, 600.0f)[0], RotationUtil.teleportRot(entityOtherPlayerMP, this.target, 1000.0f, 1000.0f)[1], false));
                        }
                    } else {
                        Minecraft.thePlayer.sendQueue.sendQueueWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(pathElm.xCoord, pathElm.yCoord, pathElm.zCoord, false));
                        this.packetList.add(new C03PacketPlayer.C04PacketPlayerPosition(pathElm.xCoord, pathElm.yCoord, pathElm.zCoord, false));
                        entityOtherPlayerMP.rotationYawHead = Minecraft.thePlayer.rotationYawHead;
                        f = (float)(pathElm.xCoord - this.target.posX);
                        f1 = (float)(pathElm.yCoord - this.target.posY);
                        f2 = (float)(pathElm.zCoord - this.target.posZ);
                        if (!(MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2) < 2.5f)) continue;
                        for (j = 0; j <= 2; ++j) {
                            Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(RotationUtil.teleportRot(entityOtherPlayerMP, this.target, 600.0f, 600.0f)[0], RotationUtil.teleportRot(entityOtherPlayerMP, this.target, 1000.0f, 1000.0f)[1], false));
                        }
                    }
                    break;
                }
                ArrayList<Packet> packetArrayList = new ArrayList<Packet>();
                if (this.packetList.isEmpty()) {
                    return;
                }
                for (int j = this.packetList.size() - 1; j > 0; --j) {
                    packetArrayList.add(this.packetList.get(j));
                }
                if (((Boolean)this.blocking.getValue()).booleanValue() && this.isBlocking) {
                    KeyBinding.setKeyBindState(TPAura.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
                    Minecraft.thePlayer.stopUsingItem();
                    Minecraft.playerController.onStoppedUsingItem(Minecraft.thePlayer);
                }
                Minecraft.thePlayer.swingItem();
                this.sendPacket(new C02PacketUseEntity(this.target, C02PacketUseEntity.Action.ATTACK));
                playerCapabilities.allowFlying = true;
                playerCapabilities.isFlying = true;
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C13PacketPlayerAbilities(playerCapabilities));
                for (Packet packet : packetArrayList) {
                    Minecraft.thePlayer.sendQueue.sendQueueWithoutEvent(packet);
                }
            }
            this.cps.reset();
        }
        if (((Boolean)this.blocking.getValue()).booleanValue() && !this.isBlocking) {
            if (Minecraft.thePlayer.getHeldItem() != null) {
                if (Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                    KeyBinding.setKeyBindState(TPAura.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                    Minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Minecraft.thePlayer.getHeldItem()));
                    Minecraft.playerController.sendUseItem(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer.getHeldItem());
                }
            }
        }
    }

    @EventHandler
    public void onRender(EventRender3D e) {
        if (!((Boolean)this.showPath.getValue()).booleanValue()) {
            return;
        }
        if (!this.targets.isEmpty()) {
            int color = this.targets.get((int)0).hurtResistantTime > 15 ? new Color(255, 0, 0, 100).getRGB() : new Color(255, 255, 255, 100).getRGB();
            RenderUtil.drawESP(this.targets.get(0), color);
        }
        if (!this.path.isEmpty()) {
            for (int i = 0; i < this.targets.size(); ++i) {
                if (this.test == null) continue;
                for (Object pos : this.test[i]) {
                    float f2;
                    float f1;
                    float f;
                    Vec3 vec3 = (Vec3)pos;
                    if (pos == null || MathHelper.sqrt_float((f = (float)(vec3.xCoord - this.target.posX)) * f + (f1 = (float)(vec3.yCoord - this.target.posY)) * f1 + (f2 = (float)(vec3.zCoord - this.target.posZ)) * f2) < 2.5f) continue;
                    RenderUtil.drawPath(vec3);
                }
            }
            if (this.cps.check(1000.0f)) {
                this.test = new ArrayList[50];
                this.path.clear();
            }
        }
    }

    @Override
    public void onDisable() {
        KeyBinding.setKeyBindState(TPAura.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
        Minecraft.thePlayer.stopUsingItem();
        Minecraft.playerController.onStoppedUsingItem(Minecraft.thePlayer);
        super.onDisable();
    }

    private boolean validEntity(Entity ent, double reach) {
        boolean b = ent == null ? false : (ent == Minecraft.thePlayer ? false : (ent instanceof EntityPlayer && !((Boolean)this.players.getValue()).booleanValue() ? false : ((ent instanceof EntityAnimal || ent instanceof EntitySquid) && !((Boolean)this.animals.getValue()).booleanValue() ? false : ((ent instanceof EntityMob || ent instanceof EntityVillager || ent instanceof EntityBat) && !((Boolean)this.mobs.getValue()).booleanValue() ? false : ((double)Minecraft.thePlayer.getDistanceToEntity(ent) > reach ? false : (ent instanceof EntityPlayer && FriendManager.isFriend(ent.getName()) ? false : (!ent.isDead && ((EntityLivingBase)ent).getHealth() > 0.0f ? (ent.isInvisible() && !((Boolean)this.invis.getValue()).booleanValue() ? false : (AntiBot.isBot((EntityLivingBase)ent) ? false : !Minecraft.thePlayer.isDead && (!(ent instanceof EntityPlayer) || !Teams.isOnSameTeam(ent)))) : false)))))));
        return b;
    }

    private List<EntityLivingBase> getTargets(double n) {
        ArrayList<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
        for (Entity o : Minecraft.theWorld.getLoadedEntityList()) {
            EntityLivingBase entity;
            if (!(o instanceof EntityLivingBase) || !this.validEntity(entity = (EntityLivingBase)o, n)) continue;
            targets.add(entity);
        }
        if (!targets.isEmpty()) {
            if (this.priority.isCurrentMode("Distance")) {
                this.targets.sort(this.distanceComparator);
            } else {
                this.targets.sort(this.healthComparator);
            }
        }
        return targets;
    }

    @EventHandler
    public void onListener(EventPacketSend packet) {
        C08PacketPlayerBlockPlacement blockPlacement;
        if (packet.getPacket() instanceof C07PacketPlayerDigging && ((C07PacketPlayerDigging)packet.getPacket()).getStatus().equals((Object)C07PacketPlayerDigging.Action.RELEASE_USE_ITEM)) {
            this.isBlocking = false;
        }
        if (packet.getPacket() instanceof C08PacketPlayerBlockPlacement && (blockPlacement = (C08PacketPlayerBlockPlacement)packet.getPacket()).getStack() != null && blockPlacement.getStack().getItem() instanceof ItemSword && blockPlacement.getPosition().equals(new BlockPos(-1, -1, -1))) {
            this.isBlocking = true;
        }
    }
}

