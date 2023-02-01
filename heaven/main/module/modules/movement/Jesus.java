/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.movement;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventBoundingBox;
import heaven.main.event.events.world.EventPacketSend;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.movement.Speed;
import heaven.main.utils.BlockUtils;
import heaven.main.utils.MoveUtils;
import heaven.main.value.Mode;
import heaven.main.value.Option;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class Jesus
extends Module {
    private boolean wasWater;
    private int ticks;
    private final String[] modes = new String[]{"Solid", "free", "Dolphin", "Horizon", "AAC3.3.11", "Jump"};
    private final Mode<String> mode = new Mode("Mode", this.modes, this.modes[0]);
    public static final Option<Boolean> stopspeed = new Option<Boolean>("StopSpeed", false);

    public Jesus() {
        super("WaterWalk", ModuleType.Movement);
        this.addValues(this.mode, stopspeed);
    }

    @Override
    public void onEnable() {
        this.wasWater = false;
        super.onEnable();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean canJeboos() {
        if (Minecraft.thePlayer.fallDistance >= 3.0f) return false;
        if (Jesus.mc.gameSettings.keyBindJump.isPressed()) return false;
        if (BlockUtils.isInLiquid()) return false;
        if (Minecraft.thePlayer.isSneaking()) return false;
        return true;
    }

    public static boolean shouldJesus() {
        double x = Minecraft.thePlayer.posX;
        double y = Minecraft.thePlayer.posY;
        double z = Minecraft.thePlayer.posZ;
        ArrayList<BlockPos> pos = new ArrayList<BlockPos>(Arrays.asList(new BlockPos(x + 0.3, y, z + 0.3), new BlockPos(x - 0.3, y, z + 0.3), new BlockPos(x + 0.3, y, z - 0.3), new BlockPos(x - 0.3, y, z - 0.3)));
        for (BlockPos po : pos) {
            if (!(Minecraft.theWorld.getBlockState(po).getBlock() instanceof BlockLiquid)) continue;
            if (!(Minecraft.theWorld.getBlockState(po).getProperties().get((Object)BlockLiquid.LEVEL) instanceof Integer)) continue;
            if ((Integer)((Comparable)Minecraft.theWorld.getBlockState(po).getProperties().get((Object)BlockLiquid.LEVEL)) > 4) continue;
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPre(EventPreUpdate e) {
        block33: {
            block30: {
                block32: {
                    block31: {
                        this.setSuffix((Serializable)this.mode.getValue());
                        if (!this.mode.isCurrentMode("Dolphin")) break block30;
                        if (Minecraft.thePlayer.isInWater()) {
                            if (!Minecraft.thePlayer.isSneaking() && Jesus.shouldJesus()) {
                                Minecraft.thePlayer.motionY = 0.09;
                            }
                        }
                        if (e.getType() == 1) {
                            return;
                        }
                        if (Minecraft.thePlayer.onGround) break block31;
                        if (!Minecraft.thePlayer.isOnLadder()) break block32;
                    }
                    this.wasWater = false;
                }
                if (Minecraft.thePlayer.motionY > 0.0 && this.wasWater) {
                    if (Minecraft.thePlayer.motionY <= 0.11) {
                        EntityPlayerSP player = Minecraft.thePlayer;
                        player.motionY *= 1.2671;
                    }
                    EntityPlayerSP player2 = Minecraft.thePlayer;
                    player2.motionY += 0.05172;
                }
                if (BlockUtils.isInLiquid()) {
                    if (!Minecraft.thePlayer.isSneaking()) {
                        if (this.ticks < 3) {
                            Minecraft.thePlayer.motionY = 0.13;
                            ++this.ticks;
                            this.wasWater = false;
                        } else {
                            Minecraft.thePlayer.motionY = 0.5;
                            this.ticks = 0;
                            this.wasWater = true;
                        }
                    }
                }
                break block33;
            }
            if (this.mode.isCurrentMode("Solid")) {
                if (BlockUtils.isInLiquid()) {
                    if (!Minecraft.thePlayer.isSneaking() && !Jesus.mc.gameSettings.keyBindJump.isPressed()) {
                        Minecraft.thePlayer.motionY = 0.05;
                        Minecraft.thePlayer.onGround = true;
                    }
                }
            } else if (this.mode.isCurrentMode("Horizon")) {
                if (Minecraft.thePlayer.isInWater()) {
                    if (!Minecraft.thePlayer.isSneaking() && Jesus.shouldJesus()) {
                        Minecraft.thePlayer.motionY = 0.09;
                    }
                }
            } else if (this.mode.isCurrentMode("AAC3.3.11")) {
                if (Minecraft.thePlayer.isInWater()) {
                    Minecraft.thePlayer.motionX *= 1.17;
                    Minecraft.thePlayer.motionZ *= 1.17;
                    if (Minecraft.thePlayer.isCollidedHorizontally) {
                        Minecraft.thePlayer.motionY = 0.24;
                    } else if (Minecraft.theWorld.getBlockState(new BlockPos(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 1.0, Minecraft.thePlayer.posZ)).getBlock() != Blocks.air) {
                        Minecraft.thePlayer.motionY += 0.04;
                    }
                }
            } else if (this.mode.isCurrentMode("Jump") && BlockUtils.isOnLiquid()) {
                if (!Minecraft.thePlayer.isSneaking() && !Jesus.mc.gameSettings.keyBindJump.isKeyDown()) {
                    Minecraft.thePlayer.jump();
                    MoveUtils.strafe(0.02);
                }
            }
        }
    }

    @EventHandler
    public void onPacket(EventPacketSend e) {
        if (this.mode.isCurrentMode("Solid") && e.getPacket() instanceof C03PacketPlayer && Jesus.canJeboos() && BlockUtils.isOnLiquid()) {
            C03PacketPlayer packet = (C03PacketPlayer)e.getPacket();
            packet.y = Minecraft.thePlayer.ticksExisted % 2 == 0 ? packet.y + 0.01 : packet.y - 0.01;
        }
    }

    @EventHandler
    public void onBB(EventBoundingBox e) {
        if (this.mode.is("free")) {
            if (!Minecraft.thePlayer.movementInput.sneak && BlockUtils.isInLiquid() && this.neededLevel(e.getX(), e.getY(), e.getZ())) {
                Minecraft.thePlayer.motionY = 0.12;
            }
        }
        if (this.mode.isCurrentMode("Solid") && e.getBlock() instanceof BlockLiquid && Jesus.canJeboos()) {
            e.setBoundingBox(new AxisAlignedBB(e.getX(), e.getY(), e.getZ(), (double)e.getX() + 1.0, (double)e.getY() + 1.0, (double)e.getZ() + 1.0));
        }
    }

    private boolean neededLevel(double x, double y, double z) {
        return (Integer)((Comparable)Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)).getProperties().get((Object)BlockLiquid.LEVEL)) < (Client.instance.getModuleManager().getModuleByClass(Speed.class).isEnabled() ? 2 : 4);
    }
}

