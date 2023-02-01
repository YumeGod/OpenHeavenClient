/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.command.commands;

import heaven.main.Client;
import heaven.main.command.Command;
import heaven.main.module.modules.player.AutoDuel;
import heaven.main.ui.gui.hud.notification.ClientNotification;

public class SetAutoDuelName
extends Command {
    public SetAutoDuelName() {
        super("SetAutoDuelName", new String[]{"s"});
    }

    @Override
    public void execute(String[] args) {
        if (args.length >= 1) {
            StringBuilder string = new StringBuilder();
            for (String str : args) {
                string.append(str).append(" ");
            }
            AutoDuel.autoDuelName = string.toString();
            ClientNotification.sendClientMessage("SetAutoDuelName", "Set successfully!", 5000L, ClientNotification.Type.WARNING);
        } else {
            ClientNotification.sendClientMessage("SetAutoDuelName", "Correct usage: " + Client.instance.prefix + "setAutoDuelName <Name>", 5000L, ClientNotification.Type.WARNING);
        }
    }
}

