/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.movement;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventCollideWithBlock;
import heaven.main.event.events.world.EventPacketSend;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.MoveUtils;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import java.io.Serializable;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

public class WallClimb
extends Module {
    private final Mode<String> mode = new Mode("Mode", new String[]{"Motion", "FastJump", "Jump", "AAC4.3.6"}, "Jump");
    private final Numbers<Double> Speed = new Numbers<Double>("MotionSpeed", 0.1, 0.0, 0.5, 0.01, () -> this.mode.isCurrentMode("Motion"));
    boolean climbing;

    public WallClimb() {
        super("WallClimb", ModuleType.Movement);
        this.addValues(this.mode, this.Speed);
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        this.setSuffix((Serializable)this.mode.getValue());
        if (this.mode.isCurrentMode("Motion")) {
            if (Minecraft.thePlayer.isCollidedHorizontally) {
                Minecraft.thePlayer.motionY += ((Double)this.Speed.getValue()).doubleValue();
                this.climbing = true;
            } else if (this.climbing) {
                Minecraft.thePlayer.motionY = 0.0;
                this.climbing = false;
            }
        } else if (this.mode.isCurrentMode("FastJump")) {
            if (Minecraft.thePlayer.isCollidedHorizontally) {
                if (!Minecraft.thePlayer.isOnLadder()) {
                    if (!Minecraft.thePlayer.isInWater()) {
                        if (!Minecraft.thePlayer.isInLava()) {
                            if (Minecraft.thePlayer.onGround) {
                                Minecraft.thePlayer.motionY = 0.39;
                            } else if (Minecraft.thePlayer.motionY < 0.0) {
                                Minecraft.thePlayer.motionY = -0.24;
                            }
                        }
                    }
                }
            }
        } else if (this.mode.isCurrentMode("Jump") || this.mode.isCurrentMode("AAC4.3.6")) {
            if (Minecraft.thePlayer.isCollidedHorizontally) {
                if (!Minecraft.thePlayer.isOnLadder()) {
                    if (!Minecraft.thePlayer.isInWater()) {
                        if (!Minecraft.thePlayer.isInLava()) {
                            if (Minecraft.thePlayer.onGround) {
                                Minecraft.thePlayer.jump();
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockCollide(EventCollideWithBlock e) {
        if (this.mode.isCurrentMode("Jump") || this.mode.isCurrentMode("FastJump") || this.mode.isCurrentMode("AAC4.3.6")) {
            if (Minecraft.thePlayer.isCollidedHorizontally) {
                if (!Minecraft.thePlayer.isOnLadder()) {
                    if (!Minecraft.thePlayer.isInWater()) {
                        if (!Minecraft.thePlayer.isInLava()) {
                            e.boxes.add(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).offset(Minecraft.thePlayer.posX, (int)Minecraft.thePlayer.posY - 1, Minecraft.thePlayer.posZ));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPacket(EventPacketSend event) {
        if ((this.mode.isCurrentMode("Jump") || this.mode.isCurrentMode("FastJump") || this.mode.isCurrentMode("AAC4.3.6")) && event.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer p = (C03PacketPlayer)event.getPacket();
            if (Minecraft.thePlayer.isCollidedHorizontally) {
                if (!Minecraft.thePlayer.isOnLadder()) {
                    if (!Minecraft.thePlayer.isInWater()) {
                        if (!Minecraft.thePlayer.isInLava()) {
                            double speed = 1.0E-10;
                            float f = MoveUtils.getDirection2();
                            p.x -= (double)MathHelper.sin(f) * speed;
                            p.z += (double)MathHelper.cos(f) * speed;
                        }
                    }
                }
            }
        }
    }
}

