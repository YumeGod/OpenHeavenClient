/*
 * Decompiled with CFR 0.152.
 */
package com.me.guichaguri.betterfps.installer;

import com.me.guichaguri.betterfps.installer.BetterFpsInstaller;
import com.me.guichaguri.betterfps.installer.json.JSONArray;
import com.me.guichaguri.betterfps.installer.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class InstanceInstaller {
    private static final String LIBRARY_IDENTIFIER = "betterfps";
    private static final String LIBRARY_NAME = "BetterFps";
    private static final String VERSION_NAME = "1.2.1";
    private static final String TWEAKER = "com.me.guichaguri.betterfps.tweaker.BetterFpsTweaker";
    private static final String[] LIBRARIES_NAMES = new String[]{"org.ow2.asm:asm-all:5.0.3", "net.minecraft:launchwrapper:1.11"};
    private final File mcFolder;
    private final File versionsFolder;
    private final String version;
    private final File oldVersionFolder;
    private File versionFolder = null;
    private final JSONObject versionJson;

    public static List<String> getVersions(File mcFolder) {
        File versionFolder = new File(mcFolder, "versions");
        if (!versionFolder.exists() || !versionFolder.isDirectory()) {
            return null;
        }
        ArrayList<String> versions = new ArrayList<String>();
        for (File f : versionFolder.listFiles()) {
            if (!f.isDirectory() || !f.getName().startsWith("1.8.9")) continue;
            versions.add(f.getName());
        }
        return versions;
    }

    public static void install(File mcFolder, String version) throws Exception {
        InstanceInstaller installer = new InstanceInstaller(mcFolder, version);
        installer.setupJson();
        installer.copyLibrary();
        installer.saveNewVersion();
    }

    public static File getSuggestedMinecraftFolder() {
        String userHomeDir = System.getProperty("user.home", ".");
        String osType = System.getProperty("os.name").toLowerCase();
        if (osType.contains("win") && System.getenv("APPDATA") != null) {
            return new File(System.getenv("APPDATA"), ".minecraft");
        }
        if (osType.contains("mac")) {
            return new File(userHomeDir, "Library/Application Support/minecraft");
        }
        return new File(userHomeDir, ".minecraft");
    }

    private InstanceInstaller(File mcFolder, String version) throws Exception {
        String line;
        this.mcFolder = mcFolder;
        this.version = version;
        this.versionsFolder = new File(mcFolder, "versions");
        this.oldVersionFolder = new File(this.versionsFolder, version);
        File versionJsonFile = new File(this.oldVersionFolder, version + ".json");
        if (!versionJsonFile.exists()) {
            throw new FileNotFoundException();
        }
        BufferedReader br = new BufferedReader(new FileReader(versionJsonFile));
        StringBuilder json = new StringBuilder();
        while ((line = br.readLine()) != null) {
            json.append(line);
        }
        br.close();
        this.versionJson = new JSONObject(json.toString());
    }

    public void setupJson() {
        JSONArray libraries = this.versionJson.getJSONArray("libraries");
        JSONArray newArray = new JSONArray();
        JSONObject betterfpsLib = new JSONObject();
        betterfpsLib.put("name", "betterfps:BetterFps:1.2.1");
        newArray.put(betterfpsLib);
        String[] libNames = new String[LIBRARIES_NAMES.length];
        int i = 0;
        for (String name : LIBRARIES_NAMES) {
            JSONObject lib = new JSONObject();
            lib.put("name", name);
            newArray.put(lib);
            libNames[i] = name.split(":")[1];
            ++i;
        }
        block1: for (i = 0; i < libraries.length(); ++i) {
            JSONObject o = libraries.getJSONObject(i);
            String name = o.getString("name").split(":")[1];
            for (String ln : libNames) {
                if (name.equals(ln)) continue block1;
            }
            newArray.put(o);
        }
        this.versionJson.put("libraries", newArray);
        this.versionJson.put("mainClass", "net.minecraft.launchwrapper.Launch");
        String jar = this.versionJson.has("jar") ? this.versionJson.getString("jar") : this.version;
        this.versionJson.put("jar", jar);
        String arguments = this.versionJson.getString("minecraftArguments");
        arguments = arguments + " --tweakClass com.me.guichaguri.betterfps.tweaker.BetterFpsTweaker";
        this.versionJson.put("minecraftArguments", arguments);
    }

    public void copyLibrary() throws Exception {
        int length;
        URL modFile = BetterFpsInstaller.class.getProtectionDomain().getCodeSource().getLocation();
        File libraries = new File(this.mcFolder, "libraries");
        File libraryDir = new File(libraries, "betterfps/BetterFps/1.2.1");
        libraryDir.mkdirs();
        File library = new File(libraryDir, "BetterFps-1.2.1.jar");
        InputStream is = modFile.openStream();
        FileOutputStream os = new FileOutputStream(library);
        byte[] buffer = new byte[1024];
        while ((length = is.read(buffer)) > 0) {
            ((OutputStream)os).write(buffer, 0, length);
        }
        is.close();
        ((OutputStream)os).close();
    }

    public void saveNewVersion() throws Exception {
        String versionName = this.version + "-BetterFps-" + VERSION_NAME;
        this.versionJson.put("id", versionName);
        this.versionFolder = new File(this.versionsFolder, versionName);
        this.versionFolder.mkdirs();
        File json = new File(this.versionFolder, versionName + ".json");
        BufferedWriter bw = new BufferedWriter(new FileWriter(json));
        bw.write(this.versionJson.toString());
        bw.close();
    }
}

