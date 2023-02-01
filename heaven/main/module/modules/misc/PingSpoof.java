/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.misc;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPacketSend;
import heaven.main.event.events.world.EventUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.math.MathUtil;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C16PacketClientStatus;

public class PingSpoof
extends Module {
    private final Mode<String> mode = new Mode("Mode", new String[]{"KeepAlive", "Ping"}, "Ping");
    private final Numbers<Double> maxdelay = new Numbers<Double>("MaxDelay", 1200.0, 100.0, 3000.0, 100.0, () -> this.mode.isCurrentMode("Ping"));
    private final Numbers<Double> mindelay = new Numbers<Double>("MinDelay", 1200.0, 100.0, 3000.0, 100.0, () -> this.mode.isCurrentMode("Ping"));
    private final HashMap<Packet, Long> packets = new HashMap();

    public PingSpoof() {
        super("PingSpoofer", ModuleType.Misc);
        this.addValues(this.mode, this.maxdelay, this.mindelay);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.packets.clear();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventHandler
    private void onPacketSend(EventPacketSend e) {
        block11: {
            block12: {
                block10: {
                    if (!this.mode.isCurrentMode("KeepAlive")) break block10;
                    if (e.getPacket() instanceof C00PacketKeepAlive) {
                        if (Minecraft.thePlayer.isEntityAlive()) {
                            e.setCancelled(true);
                        }
                    }
                    break block11;
                }
                if (!(e.getPacket() instanceof C00PacketKeepAlive) && !(e.getPacket() instanceof C16PacketClientStatus)) break block11;
                if (!Minecraft.thePlayer.isDead) break block12;
                if (Minecraft.thePlayer.getHealth() <= 0.0f) break block11;
            }
            if (!this.packets.containsKey(e.getPacket())) {
                e.setCancelled(true);
                HashMap<Packet, Long> hashMap = this.packets;
                synchronized (hashMap) {
                    this.packets.put(e.getPacket(), System.currentTimeMillis());
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventHandler
    public void onUpdate(EventUpdate e) {
        HashMap<Packet, Long> hashMap = this.packets;
        synchronized (hashMap) {
            this.packets.forEach((packet, time) -> {
                if ((double)(System.currentTimeMillis() - time) >= MathUtil.randomNumber((Double)this.maxdelay.getValue(), (Double)this.mindelay.getValue())) {
                    Minecraft.thePlayer.sendQueue.addToSendQueue((Packet)packet);
                    this.packets.remove(packet);
                }
            });
        }
    }
}

