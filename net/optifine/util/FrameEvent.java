/*
 * Decompiled with CFR 0.152.
 */
package net.optifine.util;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;

public class FrameEvent {
    private static final Map<String, Integer> mapEventFrames = new HashMap<String, Integer>();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean isActive(String name, int frameInterval) {
        Map<String, Integer> map = mapEventFrames;
        synchronized (map) {
            int j;
            int i = Minecraft.getMinecraft().entityRenderer.frameCount;
            Integer integer = mapEventFrames.get(name);
            if (integer == null) {
                integer = new Integer(i);
                mapEventFrames.put(name, integer);
            }
            if (i > (j = integer.intValue()) && i < j + frameInterval) {
                return false;
            }
            mapEventFrames.put(name, new Integer(i));
            return true;
        }
    }
}

