/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package heaven.main.module.modules.render;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender2D;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.CustomAura;
import heaven.main.module.modules.combat.KillAura;
import heaven.main.module.modules.movement.Fly;
import heaven.main.module.modules.movement.Longjump;
import heaven.main.module.modules.movement.Speed;
import heaven.main.module.modules.world.Scaffold;
import heaven.main.ui.font.CFontRenderer;
import java.awt.Color;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

public class ModuleIndicator
extends Module {
    final EnumChatFormatting green = EnumChatFormatting.GREEN;
    final EnumChatFormatting red = EnumChatFormatting.RED;
    final String greenText = (Object)((Object)this.green) + " Enable";
    final String redText = (Object)((Object)this.red) + " Disable";
    final Color white = new Color(255, 255, 255);
    final CFontRenderer font;

    public ModuleIndicator() {
        super("ModuleIndicator", new String[]{"mod"}, ModuleType.Render);
        this.font = Client.instance.FontLoaders.Comfortaa18;
    }

    @EventHandler
    public void onRender2D(EventRender2D e) {
        if (ModuleIndicator.mc.gameSettings.showDebugInfo) {
            return;
        }
        KillAura customAura = (KillAura)Client.instance.getModuleManager().getModuleByClass(KillAura.class);
        Speed speed = (Speed)Client.instance.getModuleManager().getModuleByClass(Speed.class);
        Scaffold scaffold = (Scaffold)Client.instance.getModuleManager().getModuleByClass(Scaffold.class);
        Fly fly = (Fly)Client.instance.getModuleManager().getModuleByClass(Fly.class);
        Longjump lj = (Longjump)Client.instance.getModuleManager().getModuleByClass(Longjump.class);
        CustomAura customkillAura = (CustomAura)Client.instance.getModuleManager().getModuleByClass(CustomAura.class);
        int Y = 50;
        this.font.drawStringWithShadow("KillAura" + (customAura.isEnabled() ? this.greenText : this.redText) + " " + (Object)((Object)EnumChatFormatting.WHITE) + Keyboard.getKeyName((int)customAura.getKey()), 5.0, 160 - Y, this.white.getRGB());
        this.font.drawStringWithShadow("Speed" + (speed.isEnabled() ? this.greenText : this.redText) + " " + (Object)((Object)EnumChatFormatting.WHITE) + Keyboard.getKeyName((int)speed.getKey()), 5.0, 172 - Y, this.white.getRGB());
        this.font.drawStringWithShadow("Scaffold" + (scaffold.isEnabled() ? this.greenText : this.redText) + " " + (Object)((Object)EnumChatFormatting.WHITE) + Keyboard.getKeyName((int)scaffold.getKey()), 5.0, 184 - Y, this.white.getRGB());
        this.font.drawStringWithShadow("Fly" + (fly.isEnabled() ? this.greenText : this.redText) + " " + (Object)((Object)EnumChatFormatting.WHITE) + Keyboard.getKeyName((int)fly.getKey()), 5.0, 196 - Y, this.white.getRGB());
        this.font.drawStringWithShadow("LongJump" + (lj.isEnabled() ? this.greenText : this.redText) + " " + (Object)((Object)EnumChatFormatting.WHITE) + Keyboard.getKeyName((int)lj.getKey()), 5.0, 208 - Y, this.white.getRGB());
        this.font.drawStringWithShadow("CustomAura" + (customkillAura.isEnabled() ? this.greenText : this.redText) + " " + (Object)((Object)EnumChatFormatting.WHITE) + Keyboard.getKeyName((int)lj.getKey()), 5.0, 220 - Y, this.white.getRGB());
    }
}

