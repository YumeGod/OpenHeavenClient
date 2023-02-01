/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  org.apache.commons.io.FileUtils
 */
package com.me.guichaguri.betterfps;

import com.google.gson.Gson;
import com.me.guichaguri.betterfps.BetterFpsConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.Properties;
import org.apache.commons.io.FileUtils;

public class BetterFpsHelper {
    public static final String MC_VERSION = "1.8.9";
    public static final String VERSION = "1.2.1";
    public static final String URL = "http://guichaguri.github.io/BetterFps/";
    public static final LinkedHashMap<String, String> helpers = new LinkedHashMap();
    public static final LinkedHashMap<String, String> displayHelpers = new LinkedHashMap();
    public static File LOC;
    public static File MCDIR;
    private static File CONFIG_FILE;

    public static void init() {
    }

    public static BetterFpsConfig loadConfig() {
        CONFIG_FILE = MCDIR == null ? new File("config" + File.pathSeparator + "betterfps.json") : new File(MCDIR, "config" + File.pathSeparator + "betterfps.json");
        try {
            if (CONFIG_FILE.exists()) {
                Gson gson = new Gson();
                BetterFpsConfig.instance = (BetterFpsConfig)gson.fromJson((Reader)new FileReader(CONFIG_FILE), BetterFpsConfig.class);
            } else {
                BetterFpsConfig.instance = new BetterFpsConfig();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            Properties prop = new Properties();
            File oldConfigFile = MCDIR == null ? new File("betterfps.txt") : new File(MCDIR, "betterfps.txt");
            if (oldConfigFile.exists() && !CONFIG_FILE.exists()) {
                prop.load(new FileInputStream(oldConfigFile));
                BetterFpsConfig.instance.algorithm = prop.getProperty("algorithm", "rivens-half");
            }
        }
        catch (Exception ex) {
            System.err.println("Could not import the old config format");
        }
        BetterFpsHelper.saveConfig();
        return BetterFpsConfig.instance;
    }

    public static void saveConfig() {
        try {
            if (!CONFIG_FILE.exists()) {
                CONFIG_FILE.createNewFile();
            }
            Gson gson = new Gson();
            FileUtils.writeStringToFile((File)CONFIG_FILE, (String)gson.toJson((Object)BetterFpsConfig.instance));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static {
        helpers.put("vanilla", "VanillaMath");
        helpers.put("rivens", "RivensMath");
        helpers.put("taylors", "TaylorMath");
        helpers.put("libgdx", "LibGDXMath");
        helpers.put("rivens-full", "RivensFullMath");
        helpers.put("rivens-half", "RivensHalfMath");
        helpers.put("java", "JavaMath");
        helpers.put("random", "RandomMath");
        displayHelpers.put("vanilla", "Vanilla Algorithm");
        displayHelpers.put("rivens", "Riven's Algorithm");
        displayHelpers.put("taylors", "Taylor's Algorithm");
        displayHelpers.put("libgdx", "LibGDX's Algorithm");
        displayHelpers.put("rivens-full", "Riven's \"Full\" Algorithm");
        displayHelpers.put("rivens-half", "Riven's \"Half\" Algorithm");
        displayHelpers.put("java", "Java Math");
        displayHelpers.put("random", "Random Math");
        MCDIR = null;
        CONFIG_FILE = null;
    }
}

