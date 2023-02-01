/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.command.commands;

import heaven.main.Client;
import heaven.main.command.Command;
import heaven.main.module.Module;
import heaven.main.utils.chat.Helper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.EnumChatFormatting;

public class setName
extends Command {
    public static List<String> list = new ArrayList<String>();

    public setName() {
        super("setName");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            Helper.sendMessage("Correct usage .setName <module> [name]");
        }
        boolean found = false;
        Module m = Client.instance.getModuleManager().getAlias(args[0]);
        if (m != null) {
            found = true;
            if (args.length >= 2) {
                m.setCustomName(args[1]);
                Helper.sendMessage((Object)((Object)EnumChatFormatting.BLUE) + m.getName() + (Object)((Object)EnumChatFormatting.GRAY) + " was" + (Object)((Object)EnumChatFormatting.GREEN) + " set" + (Object)((Object)EnumChatFormatting.GRAY) + " to " + (Object)((Object)EnumChatFormatting.YELLOW) + args[1]);
            } else {
                m.setCustomName(null);
                Helper.sendMessage((Object)((Object)EnumChatFormatting.BLUE) + m.getName() + (Object)((Object)EnumChatFormatting.GRAY) + " was" + (Object)((Object)EnumChatFormatting.RED) + " reset");
            }
        }
        if (!found) {
            Helper.sendMessage("Module name " + (Object)((Object)EnumChatFormatting.RED) + args[0] + (Object)((Object)EnumChatFormatting.GRAY) + " is invalid");
        }
    }
}

