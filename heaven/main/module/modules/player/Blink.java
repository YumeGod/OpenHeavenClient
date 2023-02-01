/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.module.modules.player;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender3D;
import heaven.main.event.events.world.EventPacketSend;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.Breadcrumbs;
import heaven.main.utils.render.gl.GLUtils;
import heaven.main.utils.timer.TimerUtil;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.awt.Color;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import org.lwjgl.opengl.GL11;

public class Blink
extends Module {
    private final LinkedBlockingQueue<Packet> packets = new LinkedBlockingQueue();
    private EntityOtherPlayerMP fakePlayer;
    private boolean disableLogger;
    private final LinkedList<double[]> positions = new LinkedList();
    private final Option<Boolean> pulseValue = new Option<Boolean>("Pulse", false);
    private final Numbers<Double> maxpulseDelayValue = new Numbers<Double>("MaxPulseDelay", 1000.0, 50.0, 5000.0, 100.0, this.pulseValue::getValue);
    private final Numbers<Double> minpulseDelayValue = new Numbers<Double>("MinPulseDelay", 1000.0, 50.0, 5000.0, 100.0, this.pulseValue::getValue);
    private final TimerUtil pulseTimer = new TimerUtil();

    public Blink() {
        super("Blink", new String[]{"blink"}, ModuleType.Player);
        this.addValues(this.pulseValue, this.maxpulseDelayValue, this.minpulseDelayValue);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void onEnable() {
        if (Minecraft.thePlayer == null) {
            return;
        }
        if (!((Boolean)this.pulseValue.getValue()).booleanValue()) {
            this.fakePlayer = new EntityOtherPlayerMP(Minecraft.theWorld, Minecraft.thePlayer.getGameProfile());
            this.fakePlayer.clonePlayer(Minecraft.thePlayer, true);
            this.fakePlayer.copyLocationAndAnglesFrom(Minecraft.thePlayer);
            this.fakePlayer.rotationYawHead = Minecraft.thePlayer.rotationYawHead;
            Minecraft.theWorld.addEntityToWorld(-1337, this.fakePlayer);
        }
        LinkedList<double[]> linkedList = this.positions;
        synchronized (linkedList) {
            double[] dArray = new double[3];
            dArray[0] = Minecraft.thePlayer.posX;
            dArray[1] = Minecraft.thePlayer.getEntityBoundingBox().minY + (double)(Minecraft.thePlayer.getEyeHeight() / 2.0f);
            dArray[2] = Minecraft.thePlayer.posZ;
            this.positions.add(dArray);
            double[] dArray2 = new double[3];
            dArray2[0] = Minecraft.thePlayer.posX;
            dArray2[1] = Minecraft.thePlayer.getEntityBoundingBox().minY;
            dArray2[2] = Minecraft.thePlayer.posZ;
            this.positions.add(dArray2);
        }
        this.pulseTimer.reset();
    }

    @Override
    public void onDisable() {
        if (Minecraft.thePlayer == null) {
            return;
        }
        this.blink();
        if (this.fakePlayer != null) {
            Minecraft.theWorld.removeEntityFromWorld(this.fakePlayer.getEntityId());
            this.fakePlayer = null;
        }
    }

    @EventHandler
    public void onPacket(EventPacketSend event) {
        Packet packet = event.getPacket();
        if (Minecraft.thePlayer == null || this.disableLogger) {
            return;
        }
        if (packet instanceof C03PacketPlayer) {
            event.setCancelled(true);
        }
        if (packet instanceof C03PacketPlayer.C04PacketPlayerPosition || packet instanceof C03PacketPlayer.C06PacketPlayerPosLook || packet instanceof C08PacketPlayerBlockPlacement || packet instanceof C0APacketAnimation || packet instanceof C0BPacketEntityAction || packet instanceof C02PacketUseEntity) {
            event.setCancelled(true);
            this.packets.add(packet);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventHandler
    public void onUpdate(EventPreUpdate event) {
        LinkedList<double[]> linkedList = this.positions;
        synchronized (linkedList) {
            double[] dArray = new double[3];
            dArray[0] = Minecraft.thePlayer.posX;
            dArray[1] = Minecraft.thePlayer.getEntityBoundingBox().minY;
            dArray[2] = Minecraft.thePlayer.posZ;
            this.positions.add(dArray);
        }
        if (((Boolean)this.pulseValue.getValue()).booleanValue() && TimerUtil.hasTimePassed(ThreadLocalRandom.current().nextLong(((Double)this.maxpulseDelayValue.getValue()).longValue(), ((Double)this.minpulseDelayValue.getValue()).longValue()))) {
            this.blink();
            this.pulseTimer.reset();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventHandler
    public void onRender3D(EventRender3D event) {
        Breadcrumbs breadcrumbs = (Breadcrumbs)Client.instance.getModuleManager().getModuleByClass(Breadcrumbs.class);
        Color color = new Color(((Double)breadcrumbs.colorRedValue.getValue()).intValue(), ((Double)breadcrumbs.colorGreenValue.getValue()).intValue(), ((Double)breadcrumbs.colorBlueValue.getValue()).intValue());
        LinkedList<double[]> linkedList = this.positions;
        synchronized (linkedList) {
            GL11.glPushMatrix();
            GL11.glDisable((int)3553);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glEnable((int)2848);
            GL11.glEnable((int)3042);
            GL11.glDisable((int)2929);
            Blink.mc.entityRenderer.disableLightmap();
            GL11.glBegin((int)3);
            GLUtils.glColor(color.getRGB());
            double renderPosX = Blink.mc.getRenderManager().viewerPosX;
            double renderPosY = Blink.mc.getRenderManager().viewerPosY;
            double renderPosZ = Blink.mc.getRenderManager().viewerPosZ;
            for (double[] pos : this.positions) {
                GL11.glVertex3d((double)(pos[0] - renderPosX), (double)(pos[1] - renderPosY), (double)(pos[2] - renderPosZ));
            }
            GL11.glColor4d((double)1.0, (double)1.0, (double)1.0, (double)1.0);
            GL11.glEnd();
            GL11.glEnable((int)2929);
            GL11.glDisable((int)2848);
            GL11.glDisable((int)3042);
            GL11.glEnable((int)3553);
            GL11.glPopMatrix();
        }
    }

    @EventHandler
    private void onRender2d() {
        this.setSuffix((Serializable)((Object)("Packet:" + this.packets.size())));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void blink() {
        try {
            this.disableLogger = true;
            while (!this.packets.isEmpty()) {
                mc.getNetHandler().getNetworkManager().sendPacket(this.packets.take());
            }
            this.disableLogger = false;
        }
        catch (Exception e) {
            e.printStackTrace();
            this.disableLogger = false;
        }
        LinkedList<double[]> linkedList = this.positions;
        synchronized (linkedList) {
            this.positions.clear();
        }
    }
}

