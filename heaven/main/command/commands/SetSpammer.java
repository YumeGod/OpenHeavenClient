/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.command.commands;

import heaven.main.Client;
import heaven.main.command.Command;
import heaven.main.module.modules.misc.Spammer;
import heaven.main.ui.gui.hud.notification.ClientNotification;

public class SetSpammer
extends Command {
    public SetSpammer() {
        super("SetSpammer", new String[]{"s"});
    }

    @Override
    public void execute(String[] args) {
        if (args.length >= 1) {
            StringBuilder string = new StringBuilder();
            for (String str : args) {
                string.append(str).append(" ");
            }
            Spammer.bindmessage = string.toString();
            ClientNotification.sendClientMessage("SetSpammer", "Set successfully!", 5000L, ClientNotification.Type.WARNING);
        } else {
            ClientNotification.sendClientMessage("SetSpammer", "Correct usage: " + Client.instance.prefix + "setspammer <Message>", 5000L, ClientNotification.Type.WARNING);
        }
    }
}

