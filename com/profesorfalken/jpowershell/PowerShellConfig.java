/*
 * Decompiled with CFR 0.152.
 */
package com.profesorfalken.jpowershell;

import com.profesorfalken.jpowershell.PowerShell;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

final class PowerShellConfig {
    private static final String CONFIG_FILENAME = "jpowershell.properties";
    private static Properties config;

    PowerShellConfig() {
    }

    public static Properties getConfig() {
        if (config == null) {
            config = new Properties();
            try {
                config.load(PowerShellConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILENAME));
            }
            catch (IOException ex) {
                Logger.getLogger(PowerShell.class.getName()).log(Level.SEVERE, "Cannot read config values from file:jpowershell.properties", ex);
            }
        }
        return config;
    }
}

