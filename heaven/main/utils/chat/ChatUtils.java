/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.utils.chat;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class ChatUtils {
    private final ChatComponentText message;

    private ChatUtils(ChatComponentText message) {
        this.message = message;
    }

    public void displayClientSided() {
        Minecraft.getMinecraft();
        Minecraft.thePlayer.addChatMessage(this.message);
    }

    private ChatComponentText getChatComponent() {
        return this.message;
    }

    public static class ChatMessageBuilder {
        private static final EnumChatFormatting defaultMessageColor = EnumChatFormatting.WHITE;
        private final ChatComponentText theMessage = new ChatComponentText("");
        private final boolean useDefaultMessageColor;
        private ChatStyle workingStyle = new ChatStyle();
        private ChatComponentText workerMessage = new ChatComponentText("");

        public ChatMessageBuilder(boolean prependDefaultPrefix) {
            if (prependDefaultPrefix) {
                this.theMessage.appendSibling(new ChatMessageBuilder(false).appendText((Object)((Object)EnumChatFormatting.GRAY) + "[" + (Object)((Object)EnumChatFormatting.RED) + "Heaven" + (Object)((Object)EnumChatFormatting.GRAY) + "]" + (Object)((Object)EnumChatFormatting.GRAY) + " ").setColor(EnumChatFormatting.RED).build().getChatComponent());
            }
            this.useDefaultMessageColor = true;
        }

        public ChatMessageBuilder(String text) {
            this.theMessage.appendSibling(new ChatMessageBuilder(false).appendText((Object)((Object)EnumChatFormatting.GRAY) + "[" + (Object)((Object)EnumChatFormatting.RED) + text + (Object)((Object)EnumChatFormatting.GRAY) + "]" + (Object)((Object)EnumChatFormatting.GRAY) + " ").setColor(EnumChatFormatting.RED).build().getChatComponent());
            this.useDefaultMessageColor = true;
        }

        public ChatMessageBuilder appendText(String text) {
            this.appendSibling();
            this.workerMessage = new ChatComponentText(text);
            this.workingStyle = new ChatStyle();
            if (this.useDefaultMessageColor) {
                this.setColor(defaultMessageColor);
            }
            return this;
        }

        public ChatMessageBuilder setColor(EnumChatFormatting color) {
            this.workingStyle.setColor(color);
            return this;
        }

        public ChatMessageBuilder bold() {
            this.workingStyle.setBold(true);
            return this;
        }

        public ChatMessageBuilder italic() {
            this.workingStyle.setItalic(true);
            return this;
        }

        public ChatMessageBuilder strikethrough() {
            this.workingStyle.setStrikethrough(true);
            return this;
        }

        public ChatMessageBuilder underline() {
            this.workingStyle.setUnderlined(true);
            return this;
        }

        public ChatUtils build() {
            this.appendSibling();
            return new ChatUtils(this.theMessage);
        }

        private void appendSibling() {
            this.theMessage.appendSibling(this.workerMessage.setChatStyle(this.workingStyle));
        }
    }
}

