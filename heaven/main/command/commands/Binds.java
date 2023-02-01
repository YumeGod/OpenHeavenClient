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
import heaven.main.utils.chat.Helper;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

public class Binds
extends Command {
    public Binds() {
        super("binds");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            StringBuilder list2 = new StringBuilder();
            for (Module cheat : Client.instance.getModuleManager().getModules()) {
                if (Keyboard.getKeyName((int)cheat.getKey()).equals("NONE")) continue;
                list2.append(cheat.getName()).append(" ").append(Keyboard.getKeyName((int)cheat.getKey())).append(", ");
            }
            Helper.sendMessage((Object)((Object)EnumChatFormatting.WHITE) + "Binds:");
            Helper.sendMessage((Object)((Object)EnumChatFormatting.WHITE) + list2.substring(0, list2.toString().length() - 2));
        } else {
            Helper.sendMessage("invalid syntax Valid -binds");
        }
    }
}

