/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.world;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPacketReceive;
import heaven.main.event.events.world.EventPacketSend;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.io.Serializable;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class FastBreak
extends Module {
    private boolean bzs;
    private float bzx;
    public BlockPos blockPos;
    public EnumFacing facing;
    private final Mode<String> mode = new Mode("Mode", new String[]{"Watchdog", "Minemora", "Custom"}, "Minemora");
    private final Numbers<Double> breakdmg = new Numbers<Double>("CustomBreakDamage", 0.01, 0.01, 1.0, 0.01, () -> this.mode.isCurrentMode("Custom"));
    private final Option<Boolean> haste = new Option<Boolean>("Haste", false);
    private boolean destroy;
    private float progress;

    public FastBreak() {
        super("FastMine", new String[]{"fastbreak"}, ModuleType.World);
        this.addValues(this.mode, this.breakdmg, this.haste);
    }

    @EventHandler
    public void onDamageBlock(EventPacketReceive event) {
        if (this.mode.is("Watchdog")) {
            if (Minecraft.playerController.getCurrentGameType().isCreative()) {
                return;
            }
            if (event.getPacket() instanceof C07PacketPlayerDigging) {
                if (!Minecraft.playerController.extendedReach()) {
                    if (Minecraft.playerController != null) {
                        C07PacketPlayerDigging packet = (C07PacketPlayerDigging)event.getPacket();
                        if (packet.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                            this.destroy = true;
                            this.blockPos = packet.getPosition();
                            this.facing = packet.getFacing();
                            this.progress = 0.0f;
                        } else if (packet.getStatus() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK || packet.getStatus() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                            this.destroy = false;
                            this.progress = 0.0f;
                            this.blockPos = null;
                            this.facing = null;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        if (this.mode.is("Watchdog")) {
            if (Minecraft.playerController.getCurrentGameType().isCreative()) {
                return;
            }
            if (Minecraft.playerController.extendedReach()) {
                Minecraft.playerController.blockHitDelay = 0;
            } else if (this.destroy) {
                if (Minecraft.playerController.isHittingBlock) {
                    if (Minecraft.thePlayer.canHarvestBlock(Minecraft.theWorld.getBlockState(FastBreak.mc.objectMouseOver.getBlockPos()).getBlock())) {
                        Block block = Minecraft.theWorld.getBlockState(this.blockPos).getBlock();
                        this.progress += block.getPlayerRelativeBlockHardness(Minecraft.thePlayer, Minecraft.theWorld, this.blockPos) * 1.4f;
                        if (this.progress >= 1.0f) {
                            Minecraft.theWorld.setBlockState(this.blockPos, Blocks.air.getDefaultState(), 11);
                            Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.blockPos, this.facing));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPre(EventPreUpdate e) {
        this.setSuffix((Serializable)this.mode.getValue());
        if (((Boolean)this.haste.getValue()).booleanValue()) {
            if (Minecraft.thePlayer.onGround) {
                Minecraft.thePlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 0, 1));
            }
        }
        if (this.mode.isCurrentMode("Custom")) {
            Minecraft.playerController.blockHitDelay = 0;
            if ((double)Minecraft.playerController.curBlockDamageMP > (Double)this.breakdmg.getValue()) {
                Minecraft.playerController.curBlockDamageMP = 1.0f;
            }
        }
        if (this.mode.isCurrentMode("Minemora")) {
            if (Minecraft.playerController.extendedReach()) {
                Minecraft.playerController.blockHitDelay = 0;
            } else if (this.bzs) {
                Block block = Minecraft.theWorld.getBlockState(this.blockPos).getBlock();
                this.bzx += (float)((double)block.getPlayerRelativeBlockHardness(Minecraft.thePlayer, Minecraft.theWorld, this.blockPos) * 1.4);
                if (this.bzx >= 1.0f) {
                    Minecraft.theWorld.setBlockState(this.blockPos, Blocks.air.getDefaultState(), 11);
                    Minecraft.thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.blockPos, this.facing));
                    this.bzx = 0.0f;
                    this.bzs = false;
                }
            }
        }
    }

    @EventHandler
    public final void onSendPacket(EventPacketSend event) {
        if (this.mode.isCurrentMode("Minemora") && event.getPacket() instanceof C07PacketPlayerDigging) {
            if (!Minecraft.playerController.extendedReach()) {
                if (Minecraft.playerController != null) {
                    C07PacketPlayerDigging c07PacketPlayerDigging = (C07PacketPlayerDigging)event.getPacket();
                    if (c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                        this.bzs = true;
                        this.blockPos = c07PacketPlayerDigging.getPosition();
                        this.facing = c07PacketPlayerDigging.getFacing();
                        this.bzx = 0.0f;
                    } else if (c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK || c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                        this.bzs = false;
                        this.blockPos = null;
                        this.facing = null;
                    }
                }
            }
        }
    }
}

