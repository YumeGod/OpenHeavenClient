/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.utils.chat;

import heaven.main.utils.chat.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

public class Helper {
    public static final Minecraft mc = Minecraft.getMinecraft();

    public static void sendMessage(String message) {
        new ChatUtils.ChatMessageBuilder(true).appendText(message).setColor(EnumChatFormatting.GRAY).build().displayClientSided();
    }

    public static void sendMessage(String prefix, String message) {
        new ChatUtils.ChatMessageBuilder(prefix).appendText(message).setColor(EnumChatFormatting.GRAY).build().displayClientSided();
    }

    public static void sendMessageWithoutPrefix(String message) {
        new ChatUtils.ChatMessageBuilder(false).appendText(message).setColor(EnumChatFormatting.GRAY).build().displayClientSided();
    }

    public static void addChat(String text) {
        Helper.sendMessage(text);
    }
}

