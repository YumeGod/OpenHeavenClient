/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.command.commands;

import heaven.main.Client;
import heaven.main.command.Command;
import heaven.main.module.Module;
import heaven.main.module.modules.render.ClickGui;
import heaven.main.ui.gui.clickgui.CSGOClickGui;
import heaven.main.ui.gui.hud.notification.ClientNotification;

public class Toggle
extends Command {
    public Toggle() {
        super("toggle", new String[]{"t", "tt", "disable", "enable"});
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            ClientNotification.sendClientMessage("Command", "Correct usage: " + Client.instance.prefix + "t <Module>", 5000L, ClientNotification.Type.WARNING);
            return;
        }
        boolean found = false;
        Module m = Client.instance.getModuleManager().getAlias(args[0]);
        if (m != null) {
            if (m instanceof ClickGui) {
                mc.displayGuiScreen(new CSGOClickGui());
            } else {
                m.setEnabledWithoutNotification(!m.isEnabled());
            }
            found = true;
            if (m.isEnabled()) {
                ClientNotification.sendClientMessage("Toggle", m.getName() + " was enabled", 5000L, ClientNotification.Type.WARNING);
            } else {
                ClientNotification.sendClientMessage("Toggle", m.getName() + " was disabled", 5000L, ClientNotification.Type.WARNING);
            }
        }
        if (!found) {
            ClientNotification.sendClientMessage("Toggle", "Module name \"" + args[0] + "\" is invalid", 5000L, ClientNotification.Type.WARNING);
        }
    }
}

