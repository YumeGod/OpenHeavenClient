/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.player;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.timer.TimerUtil;
import net.minecraft.client.Minecraft;

public class AntiAFK
extends Module {
    private final TimerUtil timer = new TimerUtil();

    public AntiAFK() {
        super("AntiAFK", new String[]{"AntiAFK"}, ModuleType.Player);
    }

    @EventHandler
    private void EventPreUpdate(EventPreUpdate event) {
        AntiAFK.mc.gameSettings.keyBindForward.pressed = true;
        if (this.timer.hasReached(200.0)) {
            Minecraft.thePlayer.rotationYaw += 50.0f;
            if (this.timer.hasReached(200.0)) {
                AntiAFK.mc.gameSettings.keyBindForward.pressed = false;
                Minecraft.thePlayer.rotationYaw += 40.0f;
                if (this.timer.hasReached(200.0)) {
                    Minecraft.thePlayer.rotationYaw += 50.0f;
                    AntiAFK.mc.gameSettings.keyBindForward.pressed = false;
                    if (this.timer.hasReached(200.0)) {
                        AntiAFK.mc.gameSettings.keyBindForward.pressed = true;
                        Minecraft.thePlayer.rotationYaw += 40.0f;
                        this.timer.reset();
                    }
                }
            }
        }
    }
}

