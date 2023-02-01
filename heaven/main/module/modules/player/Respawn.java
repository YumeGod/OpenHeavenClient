/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.player;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import net.minecraft.client.Minecraft;

public class Respawn
extends Module {
    public Respawn() {
        super("Respawn", ModuleType.Player);
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        if (!Minecraft.thePlayer.isEntityAlive()) {
            Minecraft.thePlayer.respawnPlayer();
        }
    }
}

