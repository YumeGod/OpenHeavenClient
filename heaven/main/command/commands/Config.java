/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.command.commands;

import heaven.main.Client;
import heaven.main.command.Command;
import heaven.main.utils.chat.Helper;

public class Config
extends Command {
    public Config() {
        super("config", new String[]{"cm"});
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 2 && args[0].equalsIgnoreCase("save")) {
            Client.instance.configManager.SaveConfig(args[1]);
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("load")) {
            Client.instance.configManager.LoadConfig(args[1]);
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            Client.instance.configManager.RemoveConfig(args[1]);
        }
        if (args.length != 2) {
            Helper.sendMessageWithoutPrefix("\u00a77\u00a7m\u00a7l==================================");
            Helper.sendMessageWithoutPrefix("\u00a7b\u00a7lConfigManager");
            Helper.sendMessageWithoutPrefix("\u00a7b" + Client.instance.prefix + "config save <Configuration name> :\u00a77 Save Config");
            Helper.sendMessageWithoutPrefix("\u00a7b" + Client.instance.prefix + "config load <Configuration name> :\u00a77 Load Config");
            Helper.sendMessageWithoutPrefix("\u00a7b" + Client.instance.prefix + "config remove <Configuration name> :\u00a77 Remove Config");
            Helper.sendMessageWithoutPrefix("\u00a77\u00a7m\u00a7l==================================");
        }
    }
}

