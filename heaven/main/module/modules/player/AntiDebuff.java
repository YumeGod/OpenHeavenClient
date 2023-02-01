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
import net.minecraft.potion.Potion;

public class AntiDebuff
extends Module {
    private final Option<Boolean> blind = new Option<Boolean>("BlindPotion", true);
    private final Option<Boolean> slowdown = new Option<Boolean>("SlowdownPotions", false);

    public AntiDebuff() {
        super("AntiDebuff", ModuleType.Player);
        this.addValues(this.blind, this.slowdown);
    }

    @EventHandler
    private void onUpdate(EventPreUpdate event) {
        if (((Boolean)this.blind.getValue()).booleanValue()) {
            Minecraft.thePlayer.removePotionEffectClient(Potion.blindness.getId());
        }
        if (((Boolean)this.slowdown.getValue()).booleanValue()) {
            Minecraft.thePlayer.removePotionEffectClient(Potion.moveSlowdown.getId());
        }
        Minecraft.thePlayer.removePotionEffectClient(9);
    }
}

