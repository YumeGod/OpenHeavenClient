/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.misc;

import heaven.main.Client;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class Teams
extends Module {
    public Teams() {
        super("Teams", ModuleType.Misc);
    }

    public static boolean isOnSameTeam(Entity entity) {
        if (!Client.instance.getModuleManager().getModuleByClass(Teams.class).isEnabled()) {
            return false;
        }
        if (!Minecraft.thePlayer.getDisplayName().getUnformattedText().isEmpty()) {
            if (Minecraft.thePlayer.getDisplayName().getUnformattedText().charAt(0) == '\u00a7') {
                if (Minecraft.thePlayer.getDisplayName().getUnformattedText().length() <= 2 || entity.getDisplayName().getUnformattedText().length() <= 2) {
                    return false;
                }
                return Minecraft.thePlayer.getDisplayName().getUnformattedText().substring(0, 2).equals(entity.getDisplayName().getUnformattedText().substring(0, 2));
            }
        }
        return false;
    }
}

