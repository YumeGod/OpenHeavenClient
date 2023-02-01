/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.player;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventTick;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.value.Numbers;
import net.minecraft.client.Minecraft;

public class FastDrop
extends Module {
    private final Numbers<Double> clicks = new Numbers<Double>("Clicks", 64.0, 1.0, 64.0, 1.0);

    public FastDrop() {
        super("FastDrop", ModuleType.Player);
        this.addValues(this.clicks);
    }

    @EventHandler
    public void onTick(EventTick e) {
        if (FastDrop.mc.gameSettings.keyBindDrop.isKeyDown()) {
            int i = 0;
            while ((double)i < (Double)this.clicks.get()) {
                Minecraft.thePlayer.dropOneItem(false);
                ++i;
            }
        }
    }
}

