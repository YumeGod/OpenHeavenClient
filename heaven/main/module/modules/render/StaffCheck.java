/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.misc.EventChat;
import heaven.main.event.events.rendering.EventRender2D;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.timer.TimerUtil;
import heaven.main.value.Option;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class StaffCheck
extends Module {
    private static final String[] modlist = new String[]{"LadyBleu", "GitLab", "Externalizable", "Sylent_", "Minikloon", "flameboy101", "skyerzz", "_PolynaLove_", "Gerbor12", "tjbruce17594", "Peso255", "tjommie", "MegaLatios", "HammyPlaysGames", "Wind1000100", "HLCMac", "Akamaru_", "Rewind", "Magicboys", "AmyTheMudkip", "Sir_Cow", "mike601", "Flafkas", "shrekster", "JordWG", "kaskada99", "darthdoesmc", "Teddy"};
    private String modname;
    private final TimerUtil timer = new TimerUtil();
    private final ArrayList<String> offlinemod = new ArrayList();
    private final ArrayList<String> onlinemod = new ArrayList();
    private final Option<Boolean> showOffline = new Option<Boolean>("ShowOffline", true);
    private final Option<Boolean> showOnline = new Option<Boolean>("ShowOnline", true);
    private int counter;
    private boolean isFinished;

    public StaffCheck() {
        super("StaffCheck", ModuleType.Render);
        this.addValues(this.showOffline, this.showOnline);
    }

    @EventHandler
    public void onRender(EventRender2D e) {
        FontRenderer font = Minecraft.fontRendererObj;
        Gui.drawRect(4, 120, 100, 130, new Color(0, 155, 255, 230).getRGB());
        font.drawString("Mods:" + this.onlinemod.size() + "/" + modlist.length, 6, 121, new Color(255, 255, 255, 230).getRGB());
        List<String> listArray = Arrays.asList(modlist);
        listArray.sort((o1, o2) -> font.getStringWidth((String)o2) - font.getStringWidth((String)o1));
        int counter2 = 0;
        for (String mods : listArray) {
            if (this.offlinemod.contains(mods) && ((Boolean)this.showOffline.getValue()).booleanValue()) {
                Gui.drawRect(4, 130 + counter2 * 10, 100, 140 + counter2 * 10, new Color(255, 255, 255, 150).getRGB());
                font.drawStringWithShadow(mods, 5.0f, 130 + counter2 * 10, Color.RED.getRGB());
                ++counter2;
            }
            if (!this.onlinemod.contains(mods) || !((Boolean)this.showOnline.getValue()).booleanValue()) continue;
            Gui.drawRect(4, 130 + counter2 * 10, 100, 140 + counter2 * 10, new Color(255, 255, 255, 150).getRGB());
            font.drawStringWithShadow(mods, 5.0f, 130 + counter2 * 10, Color.GREEN.getRGB());
            ++counter2;
        }
    }

    @EventHandler
    public void onChat(EventChat e) {
        if (e.getMessage().contains("\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u04b2\ufffd\ufffd\ufffd\ufffd\u07e3\ufffd")) {
            e.setCancelled(true);
            if (this.onlinemod.contains(this.modname)) {
                this.onlinemod.remove(this.modname);
                this.offlinemod.add(this.modname);
                return;
            }
            if (!this.offlinemod.contains(this.modname)) {
                this.offlinemod.add(this.modname);
            }
        }
        if (e.getMessage().contains("\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u04b2\ufffd\ufffd\ufffd\ufffd\u07e3\ufffd") || e.getMessage().contains("That player is not online!")) {
            e.setCancelled(true);
            if (this.onlinemod.contains(this.modname)) {
                this.onlinemod.remove(this.modname);
                this.offlinemod.add(this.modname);
                return;
            }
            if (!this.offlinemod.contains(this.modname)) {
                this.offlinemod.add(this.modname);
            }
        }
        if (e.getMessage().contains("You cannot message this player.")) {
            e.setCancelled(true);
            if (this.offlinemod.contains(this.modname)) {
                this.offlinemod.remove(this.modname);
                this.onlinemod.add(this.modname);
                return;
            }
            if (!this.onlinemod.contains(this.modname)) {
                this.onlinemod.add(this.modname);
            }
        }
        if (e.getMessage().contains("\ufffd\u04b2\ufffd\ufffd\ufffd\ufffd\ufffd\u03aa \"" + this.modname + "\" \ufffd\ufffd\ufffd\ufffd\ufffd")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        if (this.timer.hasReached(this.isFinished ? 10000.0 : 6000.0)) {
            if (this.counter >= modlist.length) {
                this.counter = -1;
                if (!this.isFinished) {
                    this.isFinished = true;
                }
            }
            ++this.counter;
            this.modname = modlist[this.counter];
            Minecraft.thePlayer.sendChatMessage("/message " + this.modname + " hi");
            this.timer.reset();
        }
    }
}

