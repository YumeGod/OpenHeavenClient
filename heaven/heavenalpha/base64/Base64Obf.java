/*
 * Decompiled with CFR 0.152.
 */
package heaven.heavenalpha.base64;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import net.minecraft.client.Minecraft;

public class Base64Obf {
    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final Base64.Decoder decoder = Base64.getDecoder();

    public static String obfTurnIO(String text) {
        if (text.isEmpty()) {
            Minecraft.getMinecraft().shutdown();
        }
        return Base64Obf.decode(Base64Obf.encode(text));
    }

    public static String encode(String text) {
        if (text.isEmpty()) {
            Minecraft.getMinecraft().shutdown();
        }
        byte[] textByte = text.getBytes(StandardCharsets.UTF_8);
        return encoder.encodeToString(textByte);
    }

    public static String decode(String encodedText) {
        if (encodedText.isEmpty()) {
            Minecraft.getMinecraft().shutdown();
        }
        String text = new String(decoder.decode(encodedText), StandardCharsets.UTF_8);
        return text;
    }
}

