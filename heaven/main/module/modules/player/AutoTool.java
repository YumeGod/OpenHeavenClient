/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.player;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.value.Option;
import net.minecraft.client.Minecraft;

public class AutoTool
extends Module {
    private final Option<Boolean> nohit = new Option<Boolean>("DisableWhenAttacking", false);
    private final Option<Boolean> switchback = new Option<Boolean>("Switchback", false);
    private int oldSlot;
    private int tick;

    public AutoTool() {
        super("AutoTool", ModuleType.Player);
        this.addValues(this.nohit, this.switchback);
    }

    @EventHandler
    public void onPre(EventPreUpdate event) {
        if (((Boolean)this.nohit.getValue()).booleanValue() && AutoTool.mc.objectMouseOver.entityHit != null) {
            return;
        }
        if (Minecraft.playerController.isBreakingBlock()) {
            ++this.tick;
            if (this.tick == 1) {
                this.oldSlot = Minecraft.thePlayer.inventory.currentItem;
            }
            Minecraft.thePlayer.updateTool(AutoTool.mc.objectMouseOver.getBlockPos());
        } else if (this.tick > 0) {
            if (((Boolean)this.switchback.get()).booleanValue()) {
                Minecraft.thePlayer.inventory.currentItem = this.oldSlot;
            }
            this.tick = 0;
        }
    }
}

