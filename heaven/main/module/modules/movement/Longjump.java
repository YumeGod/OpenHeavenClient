/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.movement;

import heaven.main.event.EventHandler;
import heaven.main.event.events.drakApi.MotionUpdateEvent;
import heaven.main.event.events.world.EventMove;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.event.events.world.EventTick;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.movement.Fly;
import heaven.main.module.modules.movement.Speed;
import heaven.main.module.modules.render.HUD;
import heaven.main.module.modules.world.Scaffold;
import heaven.main.ui.RenderRotate;
import heaven.main.ui.gui.hud.notification.ClientNotification;
import heaven.main.utils.MoveUtils;
import heaven.main.utils.timer.TimerUtil;
import heaven.main.value.Mode;
import heaven.main.value.Option;
import java.io.Serializable;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovementInput;

public class Longjump
extends Module {
    private final String[] modes = new String[]{"Watchdog", "WatchdogBow", "NCP"};
    private final Mode<String> mode = new Mode("Mode", this.modes, this.modes[0]);
    private final Option<Boolean> autoToggle = new Option<Boolean>("Toggle", false);
    private final Option<Boolean> glide = new Option<Boolean>("Glide", false, () -> this.mode.is("NCP"));
    private int stage;
    private double moveSpeed;
    private double lastDist;
    private boolean shouldDisable;
    public final TimerUtil timer = new TimerUtil();
    private int tick;
    private boolean shouldBoost;
    private boolean wait;
    private double baseSpeed;
    int ticks;

    public Longjump() {
        super("LongJump", new String[]{"lj"}, ModuleType.Movement);
        this.addValues(this.mode, this.glide, this.autoToggle);
    }

    public double getMinFallDist() {
        double baseFallDist = 3.0;
        if (Minecraft.thePlayer.isPotionActive(Potion.jump)) {
            baseFallDist += (double)((float)Minecraft.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1.0f);
        }
        return baseFallDist;
    }

    @Override
    public void onDisable() {
        if (this.mode.is("NCP")) {
            if (Minecraft.thePlayer != null) {
                this.moveSpeed = this.getBaseMoveSpeed();
            }
            this.lastDist = 0.0;
            this.stage = 0;
            Longjump.mc.timer.timerSpeed = 1.0f;
        }
        if (this.mode.is("WatchdogBow")) {
            this.tick = 0;
            Longjump.mc.timer.timerSpeed = 1.0f;
        }
        if (this.mode.is("Watchdog")) {
            this.ticks = 0;
            MoveUtils.setMotion(Minecraft.thePlayer.getActivePotionEffects().toString().contains("moveSpeed") ? (double)0.43f : (double)0.37f);
        }
    }

    @Override
    public void onEnable() {
        block5: {
            block7: {
                block6: {
                    if (this.mode.is("NCP")) {
                        if (Minecraft.thePlayer == null) {
                            return;
                        }
                        this.checkModule(Speed.class, Scaffold.class, Fly.class);
                        EventMove.setX(0.0);
                        EventMove.setZ(0.0);
                        this.shouldDisable = (Boolean)this.autoToggle.getValue();
                        this.timer.reset();
                        this.moveSpeed = 0.0;
                        this.lastDist = 0.0;
                        MoveUtils.stop();
                    }
                    if (!this.mode.is("WatchdogBow")) break block5;
                    if (this.bowSlot() == -1) break block6;
                    if (Minecraft.thePlayer.inventory.hasItem(Items.arrow)) break block7;
                }
                ClientNotification.sendClientMessage("LongJump", "You need bow in your hotbar and arrows", 4000L, ClientNotification.Type.WARNING);
                this.setEnabled(false);
                return;
            }
            this.wait = true;
        }
    }

    @EventHandler
    public void onTick(EventTick event) {
        block2: {
            block4: {
                block3: {
                    if (!this.mode.is("WatchdogBow")) break block2;
                    if (this.bowSlot() == -1) break block3;
                    if (Minecraft.thePlayer.inventory.hasItem(Items.arrow)) break block4;
                }
                return;
            }
            ++this.tick;
        }
    }

    @EventHandler
    private void onUpdate(EventPreUpdate e) {
        block5: {
            block7: {
                block6: {
                    this.setSuffix((Serializable)this.mode.getValue());
                    if (!this.mode.is("WatchdogBow")) break block5;
                    if (this.bowSlot() == -1) break block6;
                    if (Minecraft.thePlayer.inventory.hasItem(Items.arrow)) break block7;
                }
                return;
            }
            this.lastDist = Minecraft.thePlayer.getLastTickDistance();
            this.baseSpeed = Minecraft.thePlayer.getBaseMoveSpeed(0.16);
            if (Minecraft.thePlayer.hurtResistantTime == 19) {
                this.wait = false;
                this.stage = 1;
            }
        }
        if (this.mode.isCurrentMode("NCP") && e.getType() == 0) {
            double xDist = Minecraft.thePlayer.posX - Minecraft.thePlayer.prevPosX;
            double zDist = Minecraft.thePlayer.posZ - Minecraft.thePlayer.prevPosZ;
            this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
        }
    }

    @EventHandler
    public void onMotion(MotionUpdateEvent event) {
        block13: {
            block15: {
                block14: {
                    if (!this.mode.is("WatchdogBow")) break block13;
                    if (this.bowSlot() == -1) break block14;
                    if (Minecraft.thePlayer.inventory.hasItem(Items.arrow)) break block15;
                }
                return;
            }
            if (event.getState().equals((Object)MotionUpdateEvent.State.PRE)) {
                new RenderRotate(Minecraft.thePlayer.rotationYawHead / 2.0f, -90.0f, true);
                if (this.tick == 1) {
                    this.sendPacket(new C03PacketPlayer());
                    this.sendPacket(new C09PacketHeldItemChange(this.bowSlot()));
                    this.sendPacket(new C08PacketPlayerBlockPlacement(Minecraft.thePlayer.inventory.getStackInSlot(this.bowSlot())));
                } else if (this.tick == 2) {
                    this.sendPacket(new C03PacketPlayer());
                } else if (this.tick == 3) {
                    this.sendPacket(new C03PacketPlayer());
                } else if (this.tick == 4) {
                    this.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(event.getX(), event.getY(), event.getZ(), event.getYaw(), -90.0f, event.isOnGround()));
                    this.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    this.sendPacket(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem));
                }
            }
            if (this.tick > 35) {
                if (Minecraft.thePlayer.onGround) {
                    Longjump.mc.timer.timerSpeed = 1.0f;
                }
            }
        }
    }

    /*
     * Unable to fully structure code
     */
    @EventHandler
    private void onMove(EventMove e) {
        var2_2 = (String)this.mode.get();
        var3_3 = -1;
        switch (var2_2.hashCode()) {
            case 609795629: {
                if (!var2_2.equals("Watchdog")) break;
                var3_3 = 0;
                break;
            }
            case -1290011555: {
                if (!var2_2.equals("WatchdogBow")) break;
                var3_3 = 1;
                break;
            }
            case 77115: {
                if (!var2_2.equals("NCP")) break;
                var3_3 = 2;
            }
        }
        switch (var3_3) {
            case 0: {
                ++this.ticks;
                if (this.ticks > 52) {
                    i = 0;
                    while ((double)i <= 64.0) {
                        Longjump.mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.0625, Minecraft.thePlayer.posZ, false));
                        Longjump.mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.0625, Minecraft.thePlayer.posZ, false));
                        Longjump.mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, (double)i == 64.0));
                        if (Minecraft.thePlayer.fallDistance < 0.0f) {
                            if (Minecraft.thePlayer.isCollided) {
                                if (Minecraft.thePlayer.onGround) {
                                    this.setEnabled(false);
                                }
                            }
                        }
                        MoveUtils.setMotion(0.4);
                        i = (short)(i + 1);
                    }
                    break;
                }
                MoveUtils.setSpeed(0.0);
                break;
            }
            case 1: {
                if (this.bowSlot() == -1) break;
                if (!Minecraft.thePlayer.inventory.hasItem(Items.arrow)) break;
                if (this.wait) {
                    EventMove.x = 0.0;
                    EventMove.z = 0.0;
                    break;
                }
                if (!Minecraft.thePlayer.moving()) break;
                if (Minecraft.thePlayer.onGround) {
                    if (this.shouldBoost) {
                        Minecraft.thePlayer.motionY = Minecraft.thePlayer.getBaseMotionY() + 0.1 * (double)(4 - (Minecraft.thePlayer.isPotionActive(Potion.jump) ? Minecraft.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1 : 0));
                        e.setY(Minecraft.thePlayer.motionY);
                        this.moveSpeed *= 0.9;
                    } else {
                        this.moveSpeed = Minecraft.thePlayer.getBySprinting() * 2.0;
                    }
                } else {
                    if (Minecraft.thePlayer.motionY == 0.03196443960654492) {
                        Minecraft.thePlayer.motionY = -0.13;
                    }
                    this.moveSpeed = this.lastDist - this.lastDist / 55.0;
                }
                MoveUtils.setSpeed(Math.max(this.moveSpeed, this.baseSpeed));
                this.shouldBoost = Minecraft.thePlayer.onGround;
                if (this.shouldBoost) break;
                this.setEnabled(false);
                break;
            }
            case 2: {
                if (Minecraft.thePlayer.moveStrafing <= 0.0f) {
                    if (Minecraft.thePlayer.moveForward <= 0.0f) {
                        this.stage = 1;
                    }
                }
                if (this.stage != 1) ** GOTO lbl93
                if (Minecraft.thePlayer.moveForward != 0.0f) ** GOTO lbl90
                if (Minecraft.thePlayer.moveStrafing == 0.0f) ** GOTO lbl93
lbl90:
                // 2 sources

                this.stage = 2;
                this.moveSpeed = 2.0 * this.getBaseMoveSpeed() - 0.01;
                ** GOTO lbl132
lbl93:
                // 2 sources

                if (this.stage != 2) ** GOTO lbl102
                this.stage = 3;
                Minecraft.thePlayer.motionY = 0.424;
                e.y = 0.424;
                this.moveSpeed *= 1.7;
                if (((Boolean)this.glide.get()).booleanValue()) {
                    Longjump.mc.timer.timerSpeed = 0.4f;
                }
                ** GOTO lbl132
lbl102:
                // 1 sources

                if (this.stage != 3) ** GOTO lbl110
                this.stage = 4;
                difference = 0.0 * (this.lastDist - this.getBaseMoveSpeed() - (double)Minecraft.thePlayer.fallDistance);
                this.moveSpeed = this.lastDist - difference;
                if (((Boolean)this.glide.get()).booleanValue()) {
                    Longjump.mc.timer.timerSpeed = 0.6f;
                }
                ** GOTO lbl132
lbl110:
                // 1 sources

                if (this.stage != 4) ** GOTO lbl121
                if (!Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(0.0, Minecraft.thePlayer.motionY, 0.0)).isEmpty()) ** GOTO lbl118
                if (!Minecraft.thePlayer.isCollidedVertically) ** GOTO lbl119
lbl118:
                // 2 sources

                this.stage = 5;
lbl119:
                // 2 sources

                this.moveSpeed = this.lastDist - this.lastDist / 159.0;
                ** GOTO lbl132
lbl121:
                // 1 sources

                if (this.stage == 5) {
                    if (this.shouldDisable) {
                        this.setEnabled(false);
                        if (((Boolean)HUD.notifications.getValue()).booleanValue()) {
                            ClientNotification.sendClientMessage("LongJump", "LongJump Toggle", 1600L, ClientNotification.Type.WARNING);
                        } else {
                            ClientNotification.sendClientMessage("LongJump", "LongJump AutoDisabled", 1600L, ClientNotification.Type.WARNING);
                        }
                    }
                    this.stage = 1;
                } else if (this.stage == 0) {
                    this.stage = 1;
                }
lbl132:
                // 8 sources

                this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
                forward = MovementInput.moveForward;
                strafe = MovementInput.moveStrafe;
                yaw = Minecraft.thePlayer.rotationYaw;
                if (forward == 0.0f && strafe == 0.0f) {
                    EventMove.x = 0.0;
                    EventMove.z = 0.0;
                } else if (forward != 0.0f) {
                    if (strafe >= 1.0f) {
                        yaw += (float)(forward > 0.0f ? -45 : 45);
                        strafe = 0.0f;
                    } else if (strafe <= -1.0f) {
                        yaw += (float)(forward > 0.0f ? 45 : -45);
                        strafe = 0.0f;
                    }
                    if (forward > 0.0f) {
                        forward = 1.0f;
                    } else if (forward < 0.0f) {
                        forward = -1.0f;
                    }
                }
                mx = Math.cos(Math.toRadians(yaw + 90.0f));
                mz = Math.sin(Math.toRadians(yaw + 90.0f));
                EventMove.x = (double)forward * this.moveSpeed * mx + (double)strafe * this.moveSpeed * mz;
                EventMove.z = (double)forward * this.moveSpeed * mz - (double)strafe * this.moveSpeed * mx;
                if (forward != 0.0f || strafe != 0.0f) break;
                EventMove.x = 0.0;
                EventMove.z = 0.0;
                break;
            }
        }
    }

    private int bowSlot() {
        return Minecraft.thePlayer.getSlotByItem(Items.bow);
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

