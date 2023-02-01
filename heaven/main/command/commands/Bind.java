/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package heaven.main.command.commands;

import heaven.main.Client;
import heaven.main.command.Command;
import heaven.main.module.Module;
import heaven.main.ui.gui.hud.notification.ClientNotification;
import heaven.main.utils.chat.Helper;
import org.lwjgl.input.Keyboard;

public class Bind
extends Command {
    public Bind() {
        super("Bind");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 2) {
            String keyName;
            int key = Keyboard.getKeyIndex((String)args[1].toUpperCase());
            String string = keyName = key == 0 ? "none" : args[1].toUpperCase();
            if (args[0].equalsIgnoreCase("all")) {
                for (Module module : Client.instance.getModuleManager().getModules()) {
                    module.setKey(key);
                }
                ClientNotification.sendClientMessage("Binds", "Bind all modules to " + keyName + " .", 5000L, ClientNotification.Type.SUCCESS);
            } else {
                Module m = Client.instance.getModuleManager().getAlias(args[0]);
                if (m != null) {
                    m.setKey(key);
                    ClientNotification.sendClientMessage("Binds", "bind " + m.getName() + " to " + keyName + " .", 5000L, ClientNotification.Type.SUCCESS);
                } else {
                    Helper.sendMessage("\u00a7bInvalid module name, double check spelling.");
                }
            }
        } else {
            ClientNotification.sendClientMessage("Binds", "Correct usage: " + Client.instance.prefix + "bind <Module> <SimpleWhiteKey>", 5000L, ClientNotification.Type.WARNING);
        }
    }
}

