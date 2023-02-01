/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.command.commands;

import heaven.main.command.Command;
import heaven.main.ui.gui.hud.notification.ClientNotification;

public class Clean
extends Command {
    public Clean() {
        super("clean", new String[]{"clean", "cleans", "clear"});
    }

    @Override
    public void execute(String[] args) {
        Clean.mc.renderGlobal.loadRenderers();
        ClientNotification.sendClientMessage("WorldClean", "Clean world litter memory.", 5000L, ClientNotification.Type.WARNING);
    }
}

