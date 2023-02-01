/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.misc;

import heaven.main.event.EventHandler;
import heaven.main.event.events.More.PacketEvent;
import heaven.main.event.events.drakApi.LoadWorldEvent;
import heaven.main.event.events.world.EventPacketSend;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.event.events.world.EventTick;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.misc.disabler.MSTimer;
import heaven.main.module.modules.misc.disabler.utilities.PacketUtils;
import heaven.main.module.modules.movement.Fly;
import heaven.main.module.modules.movement.Speed;
import heaven.main.ui.RenderRotate;
import heaven.main.utils.MoveUtils;
import heaven.main.utils.chat.Helper;
import heaven.main.utils.math.RotationUtil;
import heaven.main.utils.timer.Timer;
import heaven.main.utils.timer.TimerUtil;
import heaven.main.value.Mode;
import heaven.main.value.Option;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S18PacketEntityTeleport;

public class Disabler
extends Module {
    private final Mode<String> mode = new Mode("Mode", new String[]{"Watchdog", "NCP", "HmXixPacket", "hypixel"}, "Watchdog");
    public Option<Boolean> noC03 = new Option<Boolean>("NoC03", true);
    public Option<Boolean> strafeDisabler = new Option<Boolean>("Strafe", true);
    public Option<Boolean> timerA = new Option<Boolean>("Timer", true);
    public Option<Boolean> timerB = new Option<Boolean>("Reset", false);
    public Option<Boolean> testBlink = new Option<Boolean>("TestBlink", true);
    private LinkedBlockingQueue<Packet<INetHandlerPlayServer>> packets = new LinkedBlockingQueue();
    private MSTimer timerCancelDelay = new MSTimer();
    private MSTimer timerCancelTimer = new MSTimer();
    private final LinkedBlockingQueue<Packet> packetss = new LinkedBlockingQueue();
    private boolean timerShouldCancel = true;
    private boolean canBlink = true;
    private int key;
    private int transactions;
    private short id;
    private final List<Packet> listTransactions = new CopyOnWriteArrayList<Packet>();
    private final Timer timer = new Timer();
    private boolean afterFly;
    private float lastYaw;
    private float lastPitch;
    private float shouldYaw;
    private float shouldPitch;
    private int packetcount;
    private final TimerUtil time = new TimerUtil();

    public Disabler() {
        super("Disabler", ModuleType.Misc);
        this.addValues(this.mode, this.noC03, this.strafeDisabler, this.timerA, this.timerB, this.testBlink);
    }

    @Override
    public void onEnable() {
        this.lastYaw = Minecraft.thePlayer.rotationYaw;
        this.lastPitch = Minecraft.thePlayer.rotationPitch;
        Disabler.mc.timer.timerSpeed = 1.0f;
    }

    @EventHandler
    private void onPreMotion(EventPreUpdate event) {
        if (Minecraft.thePlayer.ticksExisted < 1) {
            this.timerCancelTimer.reset();
            this.timerCancelDelay.reset();
            this.packets.clear();
        }
        if (((Boolean)this.timerB.getValue()).booleanValue() && this.timerCancelDelay.hasTimePassed(10000L)) {
            this.timerShouldCancel = true;
            this.timerCancelTimer.reset();
            this.timerCancelDelay.reset();
        }
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        if (this.mode.is("HmXixPacket") && this.time.hasReached(1000.0)) {
            if (this.packetcount > 22) {
                Helper.sendMessage("Disabler", "C03PacketPlayer sending too much, less sending now , packetcount -> " + this.packetcount + " / 22");
            }
            this.packetcount = 0;
            this.time.reset();
        }
        if (this.mode.is("NCP") || this.mode.is("HmXixPacket")) {
            if (Disabler.mc.gameSettings.keyBindAttack.isKeyDown() || Disabler.mc.gameSettings.keyBindDrop.isKeyDown() || Disabler.mc.gameSettings.keyBindUseItem.isKeyDown()) {
                return;
            }
            this.shouldYaw = this.getModule(Speed.class).isEnabled() || this.getModule(Fly.class).isEnabled() ? (float)Math.toDegrees(MoveUtils.getDirection()) : MoveUtils.getyaw();
            this.shouldPitch = this.getModule(Speed.class).isEnabled() || this.getModule(Fly.class).isEnabled() ? 30.0f : 80.0f;
            this.lastYaw = RotationUtil.getRotateForScaffold(this.shouldYaw, this.shouldPitch, this.lastYaw, this.lastPitch, 80.0f, 120.0f)[0];
            this.lastPitch = RotationUtil.getRotateForScaffold(this.shouldYaw, this.shouldPitch, this.lastYaw, this.lastPitch, 80.0f, 120.0f)[1];
            new RenderRotate(this.lastYaw, this.lastPitch, true);
            EventPreUpdate.setYaw(this.lastYaw);
            EventPreUpdate.setPitch(this.lastPitch);
        }
        if (this.mode.is("hypixel")) {
            this.isOnHypixel();
        }
    }

    @EventHandler
    public void onPacket(PacketEvent event) {
        if (this.mode.is("HmXixPacket") && event.getState().equals((Object)PacketEvent.State.OUTGOING)) {
            Packet<INetHandlerPlayServer> packet;
            if (event.getPacket() instanceof C0FPacketConfirmTransaction && ((C0FPacketConfirmTransaction)(packet = (C0FPacketConfirmTransaction)event.getPacket())).getID() < 0) {
                ++this.transactions;
                if (this.transactions > 5) {
                    this.id = ((C0FPacketConfirmTransaction)packet).getID();
                    this.listTransactions.add(packet);
                    event.setCancelled(true);
                }
            }
            if (event.getPacket() instanceof C00PacketKeepAlive) {
                packet = (C00PacketKeepAlive)event.getPacket();
                this.key = ((C00PacketKeepAlive)packet).getKey();
                event.setCancelled(true);
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean isPlayerInGame() {
        Minecraft.getMinecraft();
        if (Minecraft.thePlayer == null) return false;
        Minecraft.getMinecraft();
        if (Minecraft.theWorld == null) return false;
        return true;
    }

    public boolean isOnHypixel() {
        if (!this.isPlayerInGame()) {
            return false;
        }
        try {
            return this.mode.is("hypixel");
        }
        catch (Exception welpBruh) {
            welpBruh.printStackTrace();
            return false;
        }
    }

    @EventHandler
    public void onLoadWorld(LoadWorldEvent event) {
        if (this.mode.is("HmXixPacket")) {
            this.transactions = 0;
            this.id = 0;
            this.key = 0;
            this.listTransactions.clear();
        }
    }

    @EventHandler
    public void onTick(EventTick event) {
        if (this.mode.is("HmXixPacket")) {
            if (Minecraft.thePlayer.ticksExisted % 15 == 0 && !this.listTransactions.isEmpty()) {
                this.listTransactions.forEach(this::sendPacketNoEvent);
                this.sendPacketNoEvent(new C00PacketKeepAlive(this.key));
                this.listTransactions.clear();
            }
        }
    }

    @EventHandler
    public void onPacketSend(EventPacketSend e) {
        block35: {
            Packet<INetHandlerPlayClient> packet;
            block36: {
                block37: {
                    block32: {
                        block34: {
                            block33: {
                                this.setSuffix((Serializable)((Object)((String)this.mode.get() + (this.mode.is("HmXixPacket") ? " PPS + " + this.packetcount : ""))));
                                if (!this.mode.is("HmXixPacket")) break block32;
                                if (e.getPacket() instanceof S18PacketEntityTeleport && (Math.abs(((S18PacketEntityTeleport)(packet = (S18PacketEntityTeleport)e.packet)).getX()) > 29999984 || Math.abs(((S18PacketEntityTeleport)packet).getZ()) > 29999984 || Math.abs(((S18PacketEntityTeleport)packet).getY()) > 29999984)) {
                                    this.processExploit("Teleport", "A");
                                    e.setCancelled(true);
                                }
                                if (e.getPacket() instanceof C03PacketPlayer) {
                                    ++this.packetcount;
                                }
                                if (e.getPacket() instanceof S08PacketPlayerPosLook) {
                                    Disabler.mc.timer.timerSpeed = 1.0f;
                                    this.blink();
                                    MoveUtils.stop();
                                }
                                if (this.packetcount >= 150) break block33;
                                if (Minecraft.thePlayer.onGround) break block34;
                            }
                            packet = e.getPacket();
                            if (Minecraft.thePlayer.ticksExisted % 6 == 0) {
                                if (packet instanceof C03PacketPlayer) {
                                    boolean bl = ((C03PacketPlayer)packet).onGround = !Minecraft.thePlayer.onGround;
                                }
                                if (packet instanceof C03PacketPlayer) {
                                    e.setCancelled(true);
                                }
                                if (packet instanceof C03PacketPlayer.C04PacketPlayerPosition || packet instanceof C03PacketPlayer.C06PacketPlayerPosLook || packet instanceof C08PacketPlayerBlockPlacement || packet instanceof C0APacketAnimation || packet instanceof C0BPacketEntityAction || packet instanceof C02PacketUseEntity) {
                                    e.setCancelled(true);
                                    this.packetss.add(packet);
                                }
                            }
                        }
                        if (e.getPacket() instanceof C03PacketPlayer || e.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition || e.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
                            if (Minecraft.thePlayer.ticksExisted % 3 == 0) {
                                Disabler.mc.timer.timerSpeed = 1.0f;
                                e.cancel();
                                this.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, !Minecraft.thePlayer.onGround));
                            }
                        }
                        if (this.packetcount >= 50 && (e.getPacket() instanceof C03PacketPlayer || e.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition || e.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook)) {
                            if (Minecraft.thePlayer.ticksExisted < 50) {
                                Helper.sendMessage("Disabler", "less sending C03Packet Type -> Move Strafe");
                                e.cancel();
                            }
                        }
                    }
                    if (this.mode.is("NCP") && (e.getPacket() instanceof C0FPacketConfirmTransaction || e.getPacket() instanceof C00PacketKeepAlive)) {
                        e.cancel();
                    }
                    if (!this.mode.is("Watchdog")) break block35;
                    packet = e.getPacket();
                    this.canBlink = true;
                    if (!((Boolean)this.timerA.getValue()).booleanValue()) break block36;
                    if (packet instanceof C02PacketUseEntity || packet instanceof C03PacketPlayer || packet instanceof C07PacketPlayerDigging || packet instanceof C08PacketPlayerBlockPlacement || packet instanceof C0APacketAnimation) break block37;
                    if (!(packet instanceof C0BPacketEntityAction)) break block36;
                    if (Minecraft.thePlayer.ticksExisted <= 70) break block36;
                }
                if (this.timerShouldCancel) {
                    if (!this.timerCancelTimer.hasTimePassed(450L)) {
                        this.packets.add(packet);
                        e.cancel();
                        this.canBlink = false;
                    } else {
                        this.timerShouldCancel = false;
                        while (!this.packets.isEmpty()) {
                            try {
                                PacketUtils.sendPacketNoEvent(this.packets.take());
                            }
                            catch (InterruptedException event) {
                                event.printStackTrace();
                            }
                        }
                    }
                }
            }
            if (packet instanceof C03PacketPlayer && !(packet instanceof C03PacketPlayer.C05PacketPlayerLook) && !(packet instanceof C03PacketPlayer.C06PacketPlayerPosLook) && !(packet instanceof C03PacketPlayer.C04PacketPlayerPosition) && ((Boolean)this.noC03.getValue()).booleanValue()) {
                e.cancel();
                this.canBlink = false;
            }
            if (this.isOnHypixel() && ((Boolean)this.testBlink.getValue()).booleanValue()) {
                if (packet instanceof C02PacketUseEntity || packet instanceof C03PacketPlayer || packet instanceof C07PacketPlayerDigging || packet instanceof C08PacketPlayerBlockPlacement || packet instanceof C0APacketAnimation || packet instanceof C0BPacketEntityAction) {
                    while (!this.packets.isEmpty()) {
                        try {
                            PacketUtils.sendPacketNoEvent(this.packets.take());
                        }
                        catch (InterruptedException event) {
                            event.printStackTrace();
                        }
                    }
                } else if (this.canBlink) {
                    this.packets.add(packet);
                    e.cancel();
                }
            }
        }
    }

    private void blink() {
        try {
            while (!this.packets.isEmpty()) {
                mc.getNetHandler().getNetworkManager().sendPacket(this.packets.take());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processExploit(String module, String type) {
        Helper.sendMessage("Disabler", "The server tried to send exploit packet: " + module + " (type:" + type + ")");
    }
}

