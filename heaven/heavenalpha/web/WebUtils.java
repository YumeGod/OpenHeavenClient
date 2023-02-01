/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.Display
 */
package heaven.heavenalpha.web;

import heaven.heavenalpha.web.HttpUtil;
import heaven.main.Client;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.JOptionPane;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;

public class WebUtils {
    public static String get(String url) {
        if (url.isEmpty()) {
            Minecraft.getMinecraft().shutdown();
        }
        try {
            return HttpUtil.performGetRequest(new URL(url));
        }
        catch (Throwable e) {
            e.printStackTrace();
            return url;
        }
    }

    public static String get11(String url) {
        try {
            if (Client.instance.Users == null) {
                Field display_impl = Display.class.getDeclaredField("display_impl");
                display_impl.setAccessible(false);
            }
            if (!url.isEmpty()) {
                return HttpUtil.performGetRequest(new URL(url));
            }
            JOptionPane.showInputDialog(null, "Get Error", "Can't get null");
            Minecraft.getMinecraft().shutdown();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis() + "Get Error because URL as null or bad - First";
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static String get2(String url) {
        if (!url.isEmpty()) {
            StringBuilder result = new StringBuilder();
            BufferedReader in = null;
            try {
                try {
                    String line;
                    URL realUrl = new URL(url);
                    URLConnection connection = realUrl.openConnection();
                    connection.setDoOutput(true);
                    connection.setReadTimeout(99781);
                    connection.setRequestProperty("accept", "*/*");
                    connection.setRequestProperty("connection", "Keep-Alive");
                    connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1) ZiMinClient;Chrome 69");
                    connection.connect();
                    connection.getHeaderFields();
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = in.readLine()) != null) {
                        result.append(line).append("\n");
                    }
                }
                catch (Exception exception) {
                    try {
                        if (in != null) {
                            in.close();
                        }
                    }
                    catch (Exception err) {
                        err.printStackTrace();
                    }
                }
            }
            finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                }
                catch (Exception err) {
                    err.printStackTrace();
                }
            }
            return result.toString();
        }
        JOptionPane.showInputDialog(null, "Get Error", "Can't get null");
        Minecraft.getMinecraft().shutdown();
        return System.currentTimeMillis() + "Get Error because URL as null or bad - out";
    }
}

