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
import heaven.main.module.modules.misc.Teams;
import heaven.main.module.modules.player.DodgeUtils.Utils;
import heaven.main.ui.gui.hud.notification.ClientNotification;
import heaven.main.utils.timer.TimerUtils;
import heaven.main.value.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.Vec3;

public class ArrowDodge
extends Module {
    EntityArrow arrow;
    final TimerUtils timer;
    private final Option<Boolean> msg = new Option<Boolean>("WarningMessage", false);
    private final Option<Boolean> friend = new Option<Boolean>("FriendCheck", false);

    public ArrowDodge() {
        super("ArrowDodge", new String[0], ModuleType.Player);
        this.timer = new TimerUtils();
        this.addValues(this.msg, this.friend);
    }

    @Override
    public void onDisable() {
        this.arrow = null;
        super.onDisable();
    }

    @EventHandler
    public void onUpdate(EventPreUpdate event) {
        if (!TimerUtils.hasTimeElapsed(1000L, false)) {
            return;
        }
        for (Entity e : Minecraft.theWorld.loadedEntityList) {
            if (!(e instanceof EntityArrow)) continue;
            this.arrow = (EntityArrow)e;
            if (this.arrow.shootingEntity != null) {
                if (this.arrow.shootingEntity.isEntityEqual(Minecraft.thePlayer)) continue;
            }
            if (this.arrow.shootingEntity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer)this.arrow.shootingEntity;
                if (Client.instance.getModuleManager().getModuleByClass(Teams.class).isEnabled() && Teams.isOnSameTeam(player) || ((Boolean)this.friend.getValue()).booleanValue() && FriendManager.isFriend(player.getName())) continue;
            }
            if (this.arrow.inGround) continue;
            if (Minecraft.thePlayer.getDistanceSqToEntity(this.arrow) >= 20.0) continue;
            this.doBarrier();
        }
    }

    @Override
    public void onEnable() {
        this.arrow = null;
        super.onEnable();
    }

    private void doBarrier() {
        ArrowDodge arrowDodge = this;
        arrowDodge.timer.reset();
        if (((Boolean)this.msg.getValue()).booleanValue()) {
            ClientNotification.sendClientMessage("ArrowDodge", "Arrow incoming!!", 2500L, ClientNotification.Type.WARNING);
        }
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = Minecraft.thePlayer.inventory.getStackInSlot(i);
            if (stack == null || stack.getItem() == null || !(stack.getItem() instanceof ItemBlock)) continue;
            slot = i;
            break;
        }
        if (slot == -1) {
            return;
        }
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
        Minecraft.thePlayer.motionX = 0.0;
        Minecraft.thePlayer.motionZ = 0.0;
        Minecraft.thePlayer.setPosition(vec.xCoord, vec.yCoord, vec.zCoord);
        mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(vec.xCoord, vec.yCoord, vec.zCoord, Minecraft.thePlayer.onGround));
    }
}

