/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event.events.misc;

import heaven.main.event.Event;
import java.util.List;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.util.IChatComponent;

public class EventChat
extends Event {
    private String message;
    private final List<ChatLine> chatLines;
    private IChatComponent component;

    public EventChat(String message, IChatComponent minecomponent, List<ChatLine> minechatline) {
        this.message = message;
        this.chatLines = minechatline;
        this.component = minecomponent;
        this.setType((byte)0);
    }

    public IChatComponent getComponent() {
        return this.component;
    }

    public void setComponent(IChatComponent p_setComponent_1_) {
        this.component = p_setComponent_1_;
    }

    public List<ChatLine> getChatLines() {
        return this.chatLines;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public IChatComponent getChatComponent() {
        return this.component;
    }

    public void setChatComponent(IChatComponent ChatComponent) {
        this.component = ChatComponent;
    }
}

