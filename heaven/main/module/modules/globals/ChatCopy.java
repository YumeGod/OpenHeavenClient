/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.globals;

import heaven.main.event.EventHandler;
import heaven.main.event.events.misc.EventChat;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class ChatCopy
extends Module {
    public ChatCopy() {
        super("ChatCopy", ModuleType.Globals);
    }

    @EventHandler
    public void onChatFrom(EventChat e) {
        IChatComponent ChatComponent = e.getChatComponent();
        e.getChatComponent().appendSibling(new ChatComponentText((Object)((Object)EnumChatFormatting.GRAY) + " [C]").setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, new StringBuilder().insert(0, "/messagecopy ").append(EnumChatFormatting.getTextWithoutFormattingCodes(e.getMessage())).toString())).setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("Click on this to copy this message.")))));
        e.setChatComponent(ChatComponent);
    }
}

