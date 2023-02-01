/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.misc;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPacketReceive;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.ui.gui.hud.notification.ClientNotification;
import heaven.main.utils.chat.Helper;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;

public class Lightningtracker
extends Module {
    public Lightningtracker() {
        super("LightningTracker", ModuleType.Misc);
    }

    @EventHandler
    public void onPacketReceive(EventPacketReceive e) {
        S2CPacketSpawnGlobalEntity packetIn;
        if (e.getPacket() instanceof S2CPacketSpawnGlobalEntity && (packetIn = (S2CPacketSpawnGlobalEntity)e.getPacket()).func_149053_g() == 1) {
            int x = packetIn.func_149051_d() / 32;
            int y = packetIn.func_149050_e() / 32;
            int z = packetIn.func_149049_f() / 32;
            Helper.sendMessage("[Lightningtracker] X:" + x + " Y:" + y + " Z:" + z);
            ClientNotification.sendClientMessage("Lightningtracker", "X:" + x + " Y:" + y + " Z:" + z, 3500L, ClientNotification.Type.WARNING);
        }
    }
}

