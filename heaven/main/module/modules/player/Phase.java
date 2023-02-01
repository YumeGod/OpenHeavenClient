/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.player;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventBoundingBox;
import heaven.main.event.events.world.EventMove;
import heaven.main.event.events.world.EventPostUpdate;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.movement.Step;
import heaven.main.utils.BlockUtils;
import heaven.main.utils.MoveUtils;
import heaven.main.utils.timer.TimerUtil;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import java.io.Serializable;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;

public final class Phase
extends Module {
    final String[] modes = new String[]{"HmXix", "Collision", "Downclip"};
    private final Mode<String> mode = new Mode("Mode", this.modes, this.modes[0]);
    public final Numbers<Double> downclip = new Numbers<Double>("DownClips", 3.0, 2.0, 15.0, 1.0, () -> ((String)this.mode.get()).equals("Downclip"));
    private final TimerUtil tickTimer = new TimerUtil();

    public Phase() {
        super("Phase", ModuleType.Player);
        this.addValues(this.mode, this.downclip);
    }

    @Override
    public void onEnable() {
        if (((String)this.mode.get()).equals("Downclip")) {
            Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - (Double)this.downclip.get(), Minecraft.thePlayer.posZ);
            this.setEnabled(false);
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if (!Client.instance.getModuleManager().getModuleByClass(Step.class).isEnabled()) {
            Minecraft.thePlayer.stepHeight = 0.625f;
        }
        if (this.mode.is("HmXix")) {
            this.tickTimer.reset();
        }
    }

    @EventHandler
    public void onCollide(EventBoundingBox collide) {
        if (this.mode.is("Collision") && this.isEnabled() && BlockUtils.isInsideBlock()) {
            collide.setBoundingBox(null);
        }
        if (this.mode.is("HmXix") && this.isEnabled() && BlockUtils.isInsideBlock()) {
            collide.setBoundingBox(null);
        }
    }

    @EventHandler
    public void onMove(EventMove event) {
        if (this.mode.is("Collision") && this.isEnabled() && BlockUtils.isInsideBlock()) {
            if (Phase.mc.gameSettings.keyBindJump.isKeyDown()) {
                event.setY(Minecraft.thePlayer.motionY += (double)0.09f);
            } else if (Phase.mc.gameSettings.keyBindSneak.isKeyDown()) {
                event.setY(Minecraft.thePlayer.motionY -= (double)0.09f);
            } else {
                Minecraft.thePlayer.motionY = 0.0;
                event.setY(0.0);
            }
            MoveUtils.setSpeed(this.getBaseMoveSpeed());
        }
    }

    @EventHandler
    public void onUpdate(EventPreUpdate event) {
        block4: {
            block5: {
                this.setSuffix((Serializable)this.mode.get());
                if (!this.mode.is("HmXix") || !this.isEnabled()) break block4;
                if (BlockUtils.isInsideBlock()) {
                    Minecraft.thePlayer.noClip = true;
                    Minecraft.thePlayer.motionY = 0.0;
                    Minecraft.thePlayer.onGround = true;
                }
                if (!Minecraft.thePlayer.onGround) break block4;
                if (!TimerUtil.hasTimePassed(2L)) break block4;
                if (!Minecraft.thePlayer.isCollidedHorizontally) break block4;
                if (!BlockUtils.isInsideBlock()) break block5;
                if (!Minecraft.thePlayer.isSneaking()) break block4;
            }
            this.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, true));
            this.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(0.5, 0.0, 0.5, true));
            this.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, true));
            this.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.2, Minecraft.thePlayer.posZ, true));
            this.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(0.5, 0.0, 0.5, true));
            this.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX + 0.5, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ + 0.5, true));
            double yaw = Math.toRadians(Minecraft.thePlayer.rotationYaw);
            double x = -MathHelper.sin(yaw) * 0.04;
            double z = MathHelper.cos(yaw) * 0.04;
            Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX + x, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ + z);
            this.tickTimer.reset();
        }
    }

    @EventHandler
    public void onPost(EventPostUpdate event) {
        if (this.mode.is("Collision") && this.isEnabled()) {
            if (Minecraft.thePlayer.stepHeight > 0.0f) {
                Minecraft.thePlayer.stepHeight = 0.0f;
            }
            float moveStrafe = Minecraft.thePlayer.movementInput.getMoveStrafe();
            float moveForward = Minecraft.thePlayer.movementInput.getMoveForward();
            float rotationYaw = Minecraft.thePlayer.rotationYaw;
            double multiplier = 0.3;
            double mx = -MathHelper.sin(Math.toRadians(rotationYaw));
            double mz = MathHelper.cos(Math.toRadians(rotationYaw));
            double x = (double)moveForward * multiplier * mx + (double)moveStrafe * multiplier * mz;
            double z = (double)moveForward * multiplier * mz - (double)moveStrafe * multiplier * mx;
            if (Minecraft.thePlayer.isCollidedHorizontally) {
                if (!Minecraft.thePlayer.isOnLadder()) {
                    if (Minecraft.thePlayer.onGround) {
                        double posX = Minecraft.thePlayer.posX;
                        double posY = Minecraft.thePlayer.posY;
                        double posZ = Minecraft.thePlayer.posZ;
                        Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX + x, posY, posZ + z, true));
                        Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + 3.0, posZ, true));
                        Minecraft.thePlayer.setPosition(posX + x, posY, posZ + z);
                    }
                }
            }
        }
    }

    private double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }
}

