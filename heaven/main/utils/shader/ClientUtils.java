/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package heaven.main.utils.shader;

import heaven.main.utils.shader.MinecraftInstance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientUtils
extends MinecraftInstance {
    private static final Logger logger = LogManager.getLogger((String)"Bluelun");

    public static Logger getLogger() {
        return logger;
    }
}

