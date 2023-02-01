/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.command.commands;

import heaven.main.Client;
import heaven.main.command.Command;
import heaven.main.module.Module;
import heaven.main.ui.gui.hud.notification.ClientNotification;

public class Hide
extends Command {
    public Hide() {
        super("Hide", new String[]{"hide", "hidden"});
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 1) {
            Module m = Client.instance.getModuleManager().getAlias(args[0]);
            if (m != null) {
                m.setRemoved(!m.wasRemoved());
                ClientNotification.sendClientMessage("Hide", (m.wasRemoved() ? "Hide Module " : " Shown Module ") + m.getName(), 5000L, ClientNotification.Type.WARNING);
            } else {
                ClientNotification.sendClientMessage("Hide", " Invalid module name, double check spelling.", 5000L, ClientNotification.Type.WARNING);
            }
        } else {
            ClientNotification.sendClientMessage("Hide", "Correct usage: " + Client.instance.prefix + "hide <module>", 5000L, ClientNotification.Type.WARNING);
        }
    }
}

