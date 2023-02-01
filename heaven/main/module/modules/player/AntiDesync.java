/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.player;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPacketSend;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.timer.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C16PacketClientStatus;

public class AntiDesync
extends Module {
    private int lastSlot = -1;
    private final TimerUtil senddelay = new TimerUtil();

    public AntiDesync() {
        super("AntiDesync", new String[]{"AntiDesync"}, ModuleType.Player);
    }

    @Override
    public void onDisable() {
        this.lastSlot = -1;
        super.onDisable();
    }

    @EventHandler
    public void onPreUpdate(EventPreUpdate e) {
        if (this.lastSlot != -1) {
            if (this.lastSlot != Minecraft.thePlayer.inventory.currentItem) {
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem));
            }
        }
        if (this.senddelay.delay(500.0f)) {
            mc.getNetHandler().sendQueueWithoutEvent(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
        }
    }

    @EventHandler
    public void onPacketSend(EventPacketSend e) {
        if (e.getPacket() instanceof C09PacketHeldItemChange) {
            C09PacketHeldItemChange packet = (C09PacketHeldItemChange)e.getPacket();
            this.lastSlot = packet.getSlotId();
        }
    }
}

