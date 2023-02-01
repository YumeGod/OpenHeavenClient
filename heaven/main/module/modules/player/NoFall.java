/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.player;

import heaven.main.event.EventCancel;
import heaven.main.event.EventHandler;
import heaven.main.event.events.misc.EventChat;
import heaven.main.event.events.world.EventPacketReceive;
import heaven.main.event.events.world.EventPacketSend;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.movement.Speed;
import heaven.main.utils.MoveUtils;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.io.Serializable;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

public class NoFall
extends Module {
    private final String[] antiMode = new String[]{"Packet", "Watchdog", "Watchdog2", "Spoof", "Mineplex", "Minemora", "AAC3.3.15", "NCPBeta", "ACR", "Vulcan", "Matrix", "FakeGround", "NoGround"};
    public final Mode<String> mode = new Mode("Mode", this.antiMode, this.antiMode[0]);
    public final Option<Boolean> outdis = new Option<Boolean>("OutDistance", false);
    public final Numbers<Double> outdiss = new Numbers<Double>("OutDistances", 20.0, 4.0, 300.0, 1.0);
    protected int tick;
    private float lastTickFallDist;
    private float fallDist;
    private int offGroundTicks;

    public NoFall() {
        super("NoFall", ModuleType.Player);
        this.addValues(this.mode, this.outdis, this.outdiss);
    }

    @Override
    public void onEnable() {
        this.tick = 0;
    }

    @Override
    public void onDisable() {
        this.tick = 0;
    }

    /*
     * Unable to fully structure code
     */
    @EventHandler
    public void onPreMotion(EventPreUpdate event) {
        block19: {
            block18: {
                this.offGroundTicks = Minecraft.thePlayer.onGround ? 0 : ++this.offGroundTicks;
                if (Minecraft.thePlayer.fallDistance == 0.0f) {
                    this.fallDist = 0.0f;
                }
                this.fallDist += Minecraft.thePlayer.fallDistance - this.lastTickFallDist;
                this.lastTickFallDist = Minecraft.thePlayer.fallDistance;
                if (!this.mode.is("Watchdog")) break block18;
                if (Minecraft.thePlayer.onGround) ** GOTO lbl-1000
                if ((double)Minecraft.thePlayer.fallDistance - (double)this.tick * 2.8 >= 0.0) {
                    if (Minecraft.thePlayer.ticksExisted > 150) {
                        EventPreUpdate.setOnGround(true);
                    }
                    ++this.tick;
                } else if (Minecraft.thePlayer.onGround) {
                    this.tick = 1;
                }
            }
            if (this.mode.is("Matrix")) {
                EventPreUpdate.setOnGround(false);
                if (!this.isMoving()) {
                    this.offGroundTicks = 0;
                }
                if (NoFall.mc.gameSettings.keyBindJump.isKeyDown()) {
                    if (Minecraft.thePlayer.onGround) {
                        Minecraft.thePlayer.jump();
                        MoveUtils.strafe(0.22);
                    }
                }
                if (!this.isMoving()) {
                    if (Minecraft.thePlayer.onGround) {
                        Minecraft.thePlayer.motionX *= 0.6;
                        Minecraft.thePlayer.motionZ *= 0.6;
                    }
                }
                if ((double)MoveUtils.getSpeed() < 0.1) {
                    MoveUtils.strafe((double)MoveUtils.getSpeed() * 1.7);
                }
                if (Minecraft.thePlayer.onGround) {
                    Speed.strafe();
                }
                Minecraft.thePlayer.onGround = false;
            }
            if (!this.mode.is("Vulcan")) break block19;
            mathGround = (double)Math.round(event.getY() / 0.015625) * 0.015625;
            if (!((double)this.fallDist > 1.3)) ** GOTO lbl-1000
            if (Minecraft.thePlayer.ticksExisted % 15 == 0) {
                Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, mathGround, Minecraft.thePlayer.posZ);
                EventPreUpdate.setY(mathGround);
                mathGround = (double)Math.round(event.getY() / 0.015625) * 0.015625;
                if (Math.abs(mathGround - event.getY()) < 0.01) {
                    if (Minecraft.thePlayer.motionY < -0.4) {
                        Minecraft.thePlayer.motionY = -0.4;
                    }
                    this.sendPacket(new C03PacketPlayer(true));
                    NoFall.mc.timer.timerSpeed = 0.9f;
                }
            } else if (NoFall.mc.timer.timerSpeed == 0.9f) {
                NoFall.mc.timer.timerSpeed = 1.0f;
            }
        }
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        if (((Boolean)this.outdis.get()).booleanValue()) {
            this.setSuffix((Serializable)((Object)((String)this.mode.get() + " (" + ((Double)this.outdiss.get()).intValue() + " blocks)")));
        } else {
            this.setSuffix((Serializable)this.mode.get());
        }
        if (((Boolean)this.outdis.get()).booleanValue()) {
            if (Minecraft.thePlayer.fallDistance > ((Double)this.outdiss.get()).floatValue()) {
                return;
            }
        }
        switch (((String)this.mode.get()).toLowerCase()) {
            case "packet": 
            case "mineplex": {
                if (!(Minecraft.thePlayer.fallDistance >= 2.0f)) break;
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                break;
            }
            case "watchdog2": {
                if (Minecraft.thePlayer.onGround) {
                    Minecraft.thePlayer.fallDistance = 0.5f;
                }
                if (!(Minecraft.thePlayer.fallDistance > 2.0f)) break;
                Minecraft.thePlayer.onGround = false;
                this.sendPacket(new C03PacketPlayer(true));
                break;
            }
            case "minemora": {
                if (!(Minecraft.thePlayer.fallDistance > 2.0f)) break;
                if (!mc.isIntegratedServerRunning()) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Double.NaN, Minecraft.thePlayer.posZ, false));
                }
                Minecraft.thePlayer.fallDistance = -9999.0f;
                break;
            }
            case "aac3.3.15": {
                if (!(Minecraft.thePlayer.fallDistance > 2.0f)) break;
                if (!mc.isIntegratedServerRunning()) {
                    Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Double.NaN, Minecraft.thePlayer.posZ, false));
                }
                Minecraft.thePlayer.fallDistance = -9999.0f;
            }
        }
    }

    @EventHandler
    public void onPacket(EventPacketReceive event) {
        switch (((String)this.mode.get()).toLowerCase()) {
            case "minemora": {
                if (!(event.getPacket() instanceof C03PacketPlayer)) break;
                if (!(Minecraft.thePlayer.fallDistance >= 2.0f)) break;
                C03PacketPlayer xd = (C03PacketPlayer)event.getPacket();
                xd.moving = true;
            }
        }
    }

    @EventHandler
    public void onPacketSend(EventPacketSend e) {
        if (((Boolean)this.outdis.get()).booleanValue()) {
            if (Minecraft.thePlayer.fallDistance > ((Double)this.outdiss.get()).floatValue()) {
                return;
            }
        }
        switch (((String)this.mode.get()).toLowerCase()) {
            case "spoof": {
                if (!(e.getPacket() instanceof C03PacketPlayer)) break;
                C03PacketPlayer packet = (C03PacketPlayer)e.getPacket();
                if (!(Minecraft.thePlayer.fallDistance > 3.0f) || !this.isBlockUnder()) break;
                packet.onGround = true;
                Minecraft.thePlayer.fallDistance = 0.0f;
                break;
            }
            case "fakeground": {
                if (!(e.getPacket() instanceof C03PacketPlayer)) break;
                C03PacketPlayer packet = (C03PacketPlayer)e.getPacket();
                e.sendPacketInEvent();
                packet.setOnGround(true);
                break;
            }
            case "acr": 
            case "ncpbeta": {
                if (!(e.getPacket() instanceof C03PacketPlayer)) break;
                C03PacketPlayer packet = (C03PacketPlayer)e.getPacket();
                if (!((double)Minecraft.thePlayer.fallDistance > 0.5)) break;
                e.sendPacketInEvent();
                packet.setOnGround(true);
                break;
            }
            case "watchdog2": {
                if (!(e.getPacket() instanceof C03PacketPlayer)) break;
                C03PacketPlayer packet = (C03PacketPlayer)e.getPacket();
                if (Minecraft.thePlayer == null) break;
                if (!((double)Minecraft.thePlayer.fallDistance > 1.5)) break;
                e.sendPacketInEvent();
                packet.onGround = Minecraft.thePlayer.ticksExisted % 2 == 0;
                break;
            }
            case "noground": {
                if (!(e.getPacket() instanceof C03PacketPlayer)) break;
                C03PacketPlayer packet = (C03PacketPlayer)e.getPacket();
                e.sendPacketInEvent();
                packet.setOnGround(false);
            }
        }
    }

    @EventCancel
    public void onChat(EventChat e) {
        if (((Boolean)this.outdis.get()).booleanValue()) {
            if (Minecraft.thePlayer.fallDistance > ((Double)this.outdiss.get()).floatValue()) {
                return;
            }
        }
        if (e.getMessage().contains("The game starts in 1 second!")) {
            new Thread(() -> {
                try {
                    Thread.sleep(5000L);
                }
                catch (InterruptedException interruptedException) {
                    // empty catch block
                }
            }).start();
        } else if (e.getMessage().contains(Minecraft.thePlayer.getName() + " has joined")) {
            // empty if block
        }
    }

    private boolean isBlockUnder() {
        for (int i = (int)(Minecraft.thePlayer.posY - 1.0); i > 0; --i) {
            BlockPos pos = new BlockPos(Minecraft.thePlayer.posX, (double)i, Minecraft.thePlayer.posZ);
            if (Minecraft.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) continue;
            return true;
        }
        return false;
    }
}

