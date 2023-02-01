/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.command;

import heaven.main.utils.chat.Helper;
import net.minecraft.client.Minecraft;

public abstract class Command {
    public static final Minecraft mc = Minecraft.getMinecraft();
    private final String name;
    private final String[] alias;

    public Command(String name, String[] alias) {
        this.name = name.toLowerCase();
        this.alias = alias;
    }

    public Command(String name) {
        this.name = name.toLowerCase();
        this.alias = new String[]{name.toLowerCase()};
    }

    public abstract void execute(String[] var1);

    public String getName() {
        return this.name;
    }

    public String[] getAlias() {
        return this.alias;
    }

    public static void syntaxError() {
        Helper.sendMessage("\u00a777Invalid command usage");
    }
}

