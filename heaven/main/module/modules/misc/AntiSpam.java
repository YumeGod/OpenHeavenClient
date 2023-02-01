/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.misc;

import heaven.main.event.EventHandler;
import heaven.main.event.events.misc.EventChat;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.util.IChatComponent;

public class AntiSpam
extends Module {
    public AntiSpam() {
        super("AntiSpam", ModuleType.Misc);
    }

    @EventHandler
    public void onChat(EventChat event) {
        List<ChatLine> chatLines = event.getChatLines();
        if (chatLines.isEmpty()) {
            return;
        }
        GuiNewChat chatGUI = AntiSpam.mc.ingameGUI.getChatGUI();
        List<IChatComponent> splitText = GuiUtilRenderComponents.splitText(event.getComponent(), AntiSpam.floor((float)chatGUI.getChatWidth() / chatGUI.getChatScale()), Minecraft.fontRendererObj, false, false);
        int n = 1;
        int n2 = 0;
        for (int i = chatLines.size() - 1; i >= 0; --i) {
            String unformattedText = chatLines.get(i).getChatComponent().getUnformattedText();
            if (n2 <= splitText.size() - 1) {
                String substring2;
                String substring;
                String unformattedText2 = splitText.get(n2).getUnformattedText();
                if (n2 < splitText.size() - 1) {
                    if (unformattedText.equals(unformattedText2)) {
                        ++n2;
                        continue;
                    }
                    n2 = 0;
                    continue;
                }
                if (!unformattedText.startsWith(unformattedText2)) {
                    n2 = 0;
                    continue;
                }
                if (i > 0 && n2 == splitText.size() - 1 && (substring = (unformattedText + chatLines.get(i - 1).getChatComponent().getUnformattedText()).substring(unformattedText2.length())).startsWith(" [x") && substring.charAt(substring.length() - 1) == ']' && AntiSpam.isInteger(substring2 = substring.substring(3, substring.length() - 1))) {
                    n += Integer.parseInt(substring2);
                    ++n2;
                    continue;
                }
                if (unformattedText.length() == unformattedText2.length()) {
                    ++n;
                } else {
                    String substring3 = unformattedText.substring(unformattedText2.length());
                    if (!substring3.startsWith(" [x") || substring3.charAt(substring3.length() - 1) != ']') {
                        n2 = 0;
                        continue;
                    }
                    String substring4 = substring3.substring(3, substring3.length() - 1);
                    if (!AntiSpam.isInteger(substring4)) {
                        n2 = 0;
                        continue;
                    }
                    n += Integer.parseInt(substring4);
                }
            }
            for (int j = i + n2; j >= i; --j) {
                chatLines.remove(j);
            }
            n2 = 0;
        }
        if (n > 1) {
            event.getComponent().appendText(" [x" + n + "]");
        }
    }

    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        }
        catch (NumberFormatException ex) {
            return false;
        }
    }

    private static int floor(float n) {
        int n2 = (int)n;
        int n3 = n < (float)n2 ? n2 - 1 : n2;
        return n3;
    }
}

