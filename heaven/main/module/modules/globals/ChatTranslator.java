/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.globals;

import heaven.main.event.EventHandler;
import heaven.main.event.events.misc.EventChat;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.string.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class ChatTranslator
extends Module {
    public ChatTranslator() {
        super("ChatTranslator", ModuleType.Globals);
    }

    @EventHandler
    public void onChat(EventChat event) {
        event.getChatComponent().appendSibling(new ChatComponentText((Object)((Object)EnumChatFormatting.GRAY) + " [T]").setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, new StringBuilder().insert(0, "/translate ").append(EnumChatFormatting.getTextWithoutFormattingCodes(event.getMessage())).toString())).setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("Click on this to translate this message.")))));
    }

    @EventHandler
    public void onSendMessage(EventChat event) {
        if (!event.getMessage().isEmpty() && event.getMessage().charAt(0) == '/') {
            return;
        }
        if (!event.getMessage().isEmpty() && event.getMessage().charAt(0) == 'T') {
            event.setCancelled(true);
            Thread translate = new Thread(() -> {
                C01PacketChatMessage chat = new C01PacketChatMessage(StringUtils.translate(event.getMessage().substring(1)));
                if (Minecraft.thePlayer != null) {
                    Minecraft.thePlayer.sendQueue.getNetworkManager().sendPacket(chat);
                }
            });
            translate.setDaemon(true);
            translate.start();
        }
    }
}

