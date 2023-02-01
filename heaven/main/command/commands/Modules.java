/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.command.commands;

import heaven.main.Client;
import heaven.main.command.Command;
import heaven.main.module.Module;
import heaven.main.utils.chat.Helper;
import net.minecraft.util.EnumChatFormatting;

public class Modules
extends Command {
    public Modules() {
        super("Modules", new String[]{"mods", "cheats"});
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            StringBuilder list = new StringBuilder(Client.instance.getModuleManager().getModules().size() + " Modules - ");
            for (Module cheat : Client.instance.getModuleManager().getModules()) {
                list.append((Object)(cheat.isEnabled() ? EnumChatFormatting.GREEN : EnumChatFormatting.RED)).append(cheat.getName()).append(", ");
            }
            Helper.sendMessage("> " + list.substring(0, list.toString().length() - 2));
        } else {
            Helper.sendMessage("\u00a7bCorrect usage: " + Client.instance.prefix + "modules");
        }
    }
}

