/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventTick;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.value.Mode;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class FullBright
extends Module {
    private float old;
    private final Mode<String> mode = new Mode("Mode", new String[]{"Gamma", "NightPotion"}, "Gamma");

    public FullBright() {
        super("FullBright", ModuleType.Render);
        this.addValues(this.mode);
    }

    @Override
    public void onEnable() {
        this.old = FullBright.mc.gameSettings.gammaSetting;
        if (Minecraft.thePlayer != null) {
            Minecraft.thePlayer.removePotionEffectClient(Potion.nightVision.id);
        }
    }

    @EventHandler
    private void onTick(EventTick e) {
        if (this.mode.isCurrentMode("Gamma")) {
            FullBright.mc.gameSettings.gammaSetting = 1.5999999E7f;
        } else {
            Minecraft.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.id, 1337, 1));
        }
    }

    @Override
    public void onDisable() {
        FullBright.mc.gameSettings.gammaSetting = this.old;
    }
}

