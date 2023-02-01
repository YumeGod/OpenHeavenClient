/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.misc;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPacketSend;
import heaven.main.event.events.world.EventPostUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.chat.Helper;
import heaven.main.utils.timer.TimerUtil;
import java.io.Serializable;
import net.minecraft.network.play.client.C03PacketPlayer;

public class PacketMotior
extends Module {
    private int packetcount;
    private final TimerUtil time = new TimerUtil();

    public PacketMotior() {
        super("PacketMotior", new String[]{"rotate"}, ModuleType.Misc);
    }

    @EventHandler
    private void onPacket(EventPacketSend e) {
        if (e.getPacket() instanceof C03PacketPlayer) {
            ++this.packetcount;
        }
    }

    @EventHandler
    public void OnUpdate(EventPostUpdate event) {
        if (this.time.hasReached(1000.0)) {
            this.setSuffix((Serializable)((Object)("PPS:" + this.packetcount)));
            if (this.packetcount > 22) {
                Helper.sendMessage("C03PacketPlayer is not sending packets normally  (" + this.packetcount + "/22)");
            }
            this.packetcount = 0;
            this.time.reset();
        }
    }
}

