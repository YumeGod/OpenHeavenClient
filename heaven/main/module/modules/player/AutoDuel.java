/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 */
package heaven.main.module.modules.player;

import heaven.main.event.EventHandler;
import heaven.main.event.events.misc.EventChat;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.ui.gui.hud.notification.ClientNotification;
import heaven.main.value.Option;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.StringUtils;

public class AutoDuel
extends Module {
    public static String autoDuelName;
    private final Option<Boolean> sendDuel = new Option<Boolean>("SendDuel", false);
    private final Option<Boolean> accept = new Option<Boolean>("accept", false);

    public AutoDuel() {
        super("AutoDuel", ModuleType.Player);
        this.addValues(this.sendDuel, this.accept);
    }

    @EventHandler
    public void onChat(EventChat e) {
        String message;
        if (((Boolean)this.accept.getValue()).booleanValue() && (message = e.getMessage()).contains(" has sent a duel request with the kit ")) {
            String name = StringUtils.substringBefore((String)message, (String)" has sent a duel request with the");
            String acceptCommand = StringUtils.substringAfterLast((String)message, (String)". Type ");
            Minecraft.thePlayer.sendChatMessage(acceptCommand);
            ClientNotification.sendClientMessage("AutoDuelName-accept", "Accept " + name + "Successful!", 500L, ClientNotification.Type.WARNING);
        }
        if (((Boolean)this.sendDuel.getValue()).booleanValue() && (message = e.getMessage()).contains("\u00a77\u00a7m----------------------------------------------------")) {
            if (autoDuelName != null) {
                String duelCommand = "/duel" + autoDuelName;
                Minecraft.thePlayer.sendChatMessage(duelCommand);
                ClientNotification.sendClientMessage("AutoDuelName-SendDuel", "Send Duel Successful!", 500L, ClientNotification.Type.WARNING);
            } else {
                ClientNotification.sendClientMessage("AutoDuelName-SendDuel", "You have not set a name!!!", 5000L, ClientNotification.Type.WARNING);
            }
        }
    }
}

