/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.misc;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.ui.gui.guimainmenu.mainmenu.ClientMainMenu;
import heaven.main.ui.gui.hud.notification.ClientNotification;
import net.minecraft.client.gui.GuiMultiplayer;

public class ServerSwitcher
extends Module {
    public ServerSwitcher() {
        super("ServerSwitcher", new String[]{"ServerSwitch"}, ModuleType.Misc);
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        Client.instance.getModuleManager().getModuleByClass(ServerSwitcher.class).setEnabled(false);
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new GuiMultiplayer(new ClientMainMenu()));
        ClientNotification.sendClientMessage("ServerSwitch", "Enabled MultiPlayer", 4000L, ClientNotification.Type.INFO);
        super.onEnable();
    }
}

