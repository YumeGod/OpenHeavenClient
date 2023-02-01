/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.command.commands;

import heaven.main.Client;
import heaven.main.command.Command;
import heaven.main.management.FileManager;
import heaven.main.ui.gui.hud.notification.ClientNotification;

public class Reload
extends Command {
    public Reload() {
        super("Reload");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            FileManager.init();
            ClientNotification.sendClientMessage("Config Reload", "Client was reloaded!", 5000L, ClientNotification.Type.WARNING);
        } else {
            ClientNotification.sendClientMessage("Config Reload", "invalid syntax Valid " + Client.instance.prefix + "reload", 5000L, ClientNotification.Type.WARNING);
        }
    }
}

