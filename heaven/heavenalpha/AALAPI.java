/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.Display
 */
package heaven.heavenalpha;

import heaven.heavenalpha.antileak.AntiLeak;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;

public class AALAPI {
    public static boolean obfuscateCheck() {
        Class<?> api;
        try {
            api = Class.forName("Start");
        }
        catch (ClassNotFoundException ignored) {
            api = null;
        }
        AALAPI.ho3ClassIsTureFirst();
        if (api == null) {
            AALAPI.ho3ClassIsTure();
            return true;
        }
        return false;
    }

    public static void ho3ClassIsTureFirst() {
        Class<?> api;
        try {
            api = Class.forName("Ho3");
        }
        catch (ClassNotFoundException ignored) {
            api = null;
        }
        if (api != null) {
            AALAPI.tryBackTitleNormal();
            new AntiLeak().publicResetAllCheck();
        }
    }

    public static void ho3ClassIsTure() {
        Class<?> api;
        try {
            api = Class.forName("Ho3");
        }
        catch (ClassNotFoundException ignored) {
            api = null;
        }
        if (api != null) {
            AALAPI.tryBackTitleNormal();
            Minecraft.getMinecraft().shutdown();
        }
    }

    private static void tryBackTitleNormal() {
        try {
            Field display_impl = Display.class.getDeclaredField("display_impl");
            display_impl.setAccessible(true);
            Object obj = display_impl.get(Display.class);
            Method setTitle = obj.getClass().getDeclaredMethod("setTitle", String.class);
            setTitle.setAccessible(true);
            setTitle.invoke(obj, "Minecraft 1.8.9");
        }
        catch (Exception err) {
            err.printStackTrace();
            new AntiLeak().publicResetAllCheck();
        }
    }
}

