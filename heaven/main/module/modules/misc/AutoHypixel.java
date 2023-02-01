/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.misc;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.misc.EventChat;
import heaven.main.event.events.rendering.EventRender2D;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.ui.gui.hud.notification.ClientNotification;
import heaven.main.utils.timer.TimerUtil;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.awt.Color;
import java.io.Serializable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class AutoHypixel
extends Module {
    private final Option<Boolean> gg = new Option<Boolean>("AutoGG", true);
    private final String[] AMode = new String[]{"BedWars_1v1", "BedWars_2v2", "BedWars_3v3", "BedWars_4v4", "SkyWars_Solo", "SkyWars_Solo_Insane", "SkyWars_Solo_LuckyBlock", "SkyWars_Team", "SkyWars_Team_Insane", "SkyWars_Team_LuckyBlock", "SurivialGames_Solo", "SurivialGames_Team", "MiniWalls"};
    private final Mode<String> mode = new Mode("PlayMode", this.AMode, this.AMode[0]);
    private final Numbers<Double> delay = new Numbers<Double>("PlayDelay", 3.0, 0.0, 5.0, 0.1);
    private final Option<Boolean> rect = new Option<Boolean>("PopRect", false);
    public boolean winning;
    private final TimerUtil timer = new TimerUtil();
    int a;

    public AutoHypixel() {
        super("AutoHypixel", new String[]{"autoplay", "autophyp"}, ModuleType.Misc);
        this.addValues(this.mode, this.delay, this.gg, this.rect);
    }

    @EventHandler
    public void onUpdate(EventPreUpdate event) {
        this.setSuffix((Serializable)this.mode.getValue());
        if (!this.winning) {
            return;
        }
        if (!this.timer.hasReached((Double)this.delay.getValue() * 1000.0)) {
            return;
        }
        if (this.mode.is("BedWars_1v1")) {
            Minecraft.thePlayer.sendChatMessage("/play bedwars_eight_one");
        }
        if (this.mode.is("BedWars_2v2")) {
            Minecraft.thePlayer.sendChatMessage("/play bedwars_eight_two");
        }
        if (this.mode.is("BedWars_3v3")) {
            Minecraft.thePlayer.sendChatMessage("/play bedwars_four_three");
        }
        if (this.mode.is("BedWars_4v4")) {
            Minecraft.thePlayer.sendChatMessage("/play bedwars_four_four");
        }
        if (this.mode.is("SkyWars_Solo")) {
            Minecraft.thePlayer.sendChatMessage("/play solo_normal");
        }
        if (this.mode.is("SkyWars_Solo_Insane")) {
            Minecraft.thePlayer.sendChatMessage("/play solo_insane");
        }
        if (this.mode.is("SkyWars_Solo_LuckyBlock")) {
            Minecraft.thePlayer.sendChatMessage("/play solo_insane_lucky");
        }
        if (this.mode.is("SkyWars_Team")) {
            Minecraft.thePlayer.sendChatMessage("/play teams_normal");
        }
        if (this.mode.is("SkyWars_Team_Insane")) {
            Minecraft.thePlayer.sendChatMessage("/play teams_insane");
        }
        if (this.mode.is("SkyWars_Team_LuckyBlock")) {
            Minecraft.thePlayer.sendChatMessage("/play teams_insane_lucky");
        }
        if (this.mode.is("SurivialGames_Solo")) {
            Minecraft.thePlayer.sendChatMessage("/play blitz_solo_normal");
        }
        if (this.mode.is("SurivialGames_Solo")) {
            Minecraft.thePlayer.sendChatMessage("/play blitz_teams_normal");
        }
        if (this.mode.is("MiniWalls")) {
            Minecraft.thePlayer.sendChatMessage("/play arcade_mini_walls");
        }
        if (!((Boolean)this.rect.get()).booleanValue()) {
            String delaytext = "Join next game in " + this.delay.getValue() + "s";
            if (this.winning) {
                ClientNotification.sendClientMessage("AutoHypixel", delaytext, (long)((Double)this.delay.getValue() * 1000.0), ClientNotification.Type.SUCCESS);
            }
        }
        this.winning = false;
    }

    @Override
    public void onEnable() {
        if (this.mode.is("BedWars_1v1")) {
            Minecraft.thePlayer.sendChatMessage("/play bedwars_eight_one");
        }
        if (this.mode.is("BedWars_2v2")) {
            Minecraft.thePlayer.sendChatMessage("/play bedwars_eight_two");
        }
        if (this.mode.is("BedWars_3v3")) {
            Minecraft.thePlayer.sendChatMessage("/play bedwars_four_three");
        }
        if (this.mode.is("BedWars_4v4")) {
            Minecraft.thePlayer.sendChatMessage("/play bedwars_four_four");
        }
        if (this.mode.is("SkyWars_Solo")) {
            Minecraft.thePlayer.sendChatMessage("/play solo_normal");
        }
        if (this.mode.is("SkyWars_Solo_Insane")) {
            Minecraft.thePlayer.sendChatMessage("/play solo_insane");
        }
        if (this.mode.is("SkyWars_Solo_LuckyBlock")) {
            Minecraft.thePlayer.sendChatMessage("/play solo_insane_lucky");
        }
        if (this.mode.is("SkyWars_Team")) {
            Minecraft.thePlayer.sendChatMessage("/play teams_normal");
        }
        if (this.mode.is("SkyWars_Team_Insane")) {
            Minecraft.thePlayer.sendChatMessage("/play teams_insane");
        }
        if (this.mode.is("SkyWars_Team_LuckyBlock")) {
            Minecraft.thePlayer.sendChatMessage("/play teams_insane_lucky");
        }
        if (this.mode.is("SurivialGames_Solo")) {
            Minecraft.thePlayer.sendChatMessage("/play blitz_solo_normal");
        }
        if (this.mode.is("SurivialGames_Solo")) {
            Minecraft.thePlayer.sendChatMessage("/play blitz_teams_normal");
        }
        if (this.mode.is("MiniWalls")) {
            Minecraft.thePlayer.sendChatMessage("/play arcade_mini_walls");
        }
    }

    @Override
    public void onDisable() {
        this.a = -40;
        this.winning = false;
    }

    @EventHandler
    private void onChat(EventChat e) {
        if (e.getMessage().contains("Victory") || e.getMessage().contains("\u02a4\ufffd\ufffd")) {
            this.winning = true;
            e.setCancelled(true);
        }
        if (((Boolean)this.gg.getValue()).booleanValue() && (e.getMessage().contains("Victory") || e.getMessage().contains("\u02a4\ufffd\ufffd"))) {
            Minecraft.thePlayer.sendChatMessage("GG");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onRender2D(EventRender2D event) {
        if (((Boolean)this.rect.get()).booleanValue()) {
            if (this.winning) {
                if (this.a < 40) {
                    this.a += 8;
                }
            } else {
                this.timer.reset();
                if (this.a > -40) {
                    this.a -= 8;
                }
            }
            String test = "Auto Play";
            String test2 = "Join next game in " + ((Double)this.delay.getValue() - (double)(this.timer.getElapsedTime() / 1000L)) + "s";
            RenderUtil.drawRoundedRect(ScaledResolution.getScaledWidth() / 2 - 80, this.a, ScaledResolution.getScaledWidth() / 2 + 80, this.a + 30, 0.0f, new Color(0, 0, 0, 150).getRGB());
            Client.instance.FontLoaders.Comfortaa22.drawString("Auto Play", ScaledResolution.getScaledWidth() / 2 - Client.instance.FontLoaders.Comfortaa22.getStringWidth("Auto Play") / 2, this.a + 5, new Color(255, 255, 255).getRGB());
            Client.instance.FontLoaders.Comfortaa18.drawString(test2, ScaledResolution.getScaledWidth() / 2 - Client.instance.FontLoaders.Comfortaa18.getStringWidth(test2) / 2, this.a + 18, new Color(180, 180, 180).getRGB());
        }
    }
}

