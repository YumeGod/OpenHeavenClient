/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package heaven.main.module.modules.misc;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.management.FriendManager;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;

public class MCF
extends Module {
    private boolean down;

    public MCF() {
        super("MCF", new String[]{"mcf"}, ModuleType.Misc);
    }

    @EventHandler
    private void onClick(EventPreUpdate e) {
        if (Mouse.isButtonDown((int)2) && !this.down) {
            if (MCF.mc.objectMouseOver.entityHit != null) {
                EntityPlayer player = (EntityPlayer)MCF.mc.objectMouseOver.entityHit;
                String playerName = player.getName();
                if (!FriendManager.isFriend(playerName)) {
                    Minecraft.thePlayer.sendChatMessage("-f add " + playerName);
                } else {
                    Minecraft.thePlayer.sendChatMessage("-f del " + playerName);
                }
            }
            this.down = true;
        }
        if (!Mouse.isButtonDown((int)2)) {
            this.down = false;
        }
    }
}

