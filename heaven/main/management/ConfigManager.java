/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package heaven.main.management;

import heaven.main.Client;
import heaven.main.management.FileManager;
import heaven.main.module.Module;
import heaven.main.module.modules.render.ClickGui;
import heaven.main.utils.chat.Helper;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import heaven.main.value.Value;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import org.lwjgl.input.Keyboard;

public class ConfigManager {
    public void SaveConfig(String dirs) {
        Helper.sendMessage("Trying to save the configuration:" + dirs);
        File dir = new File(FileManager.dir, dirs);
        if (!dir.exists()) {
            dir.mkdir();
        }
        StringBuilder values = new StringBuilder();
        for (Module m : Client.instance.getModuleManager().getModules()) {
            for (Value value : m.getValues()) {
                values.append(String.format("%s:%s:%s%s", m.getName(), value.getName(), value.getValue(), System.lineSeparator()));
            }
        }
        FileManager.save(dir, "Values.txt", values.toString(), false);
        String content = "";
        for (Module m : Client.instance.getModuleManager().getModules()) {
            content = content + String.format("%s:%s%s", m.getName(), Keyboard.getKeyName((int)m.getKey()), System.lineSeparator());
        }
        FileManager.save(dir, "Binds.txt", content, false);
        StringBuilder enabled = new StringBuilder();
        for (Module mod : Client.instance.getModuleManager().getModules()) {
            if (!mod.isEnabled()) continue;
            enabled.append(String.format("%s%s", mod.getName(), System.lineSeparator()));
        }
        FileManager.save(dir, "Enabled.txt", enabled.toString(), false);
        String string = String.format("%s:%s%s", dirs, Client.instance.Users, System.lineSeparator());
        FileManager.save(dir, "Config.info", string, false);
        Helper.sendMessage("Save the configuration:" + dirs + " Successfully");
    }

    public void LoadConfig(String dirs) {
        File dir = new File(FileManager.dir, dirs);
        if (!dir.exists()) {
            Helper.sendMessage("The configuration you loaded does not exist!");
            return;
        }
        try {
            File info = new File(dir, "Config.info");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(info));
            String string = bufferedReader.readLine();
            String[] s2 = string.split(":");
            Helper.sendMessage("Configuration:" + s2[0] + " Author of the configuration:" + s2[1]);
        }
        catch (Exception info) {
            // empty catch block
        }
        List<String> binds = FileManager.read(dir, "Binds.txt");
        assert (binds != null);
        for (String string : binds) {
            String name = string.split(":")[0];
            String bind = string.split(":")[1];
            Module m = Client.instance.getModuleManager().getModuleByName(name);
            if (m == null) continue;
            m.setKey(Keyboard.getKeyIndex((String)bind.toUpperCase()));
        }
        if (Client.instance.getModuleManager().getModuleByClass(ClickGui.class).getKey() == 0) {
            Client.instance.getModuleManager().getModuleByClass(ClickGui.class).setKey(Keyboard.getKeyIndex((String)"RSHIFT"));
        }
        List<String> enabled = FileManager.read(dir, "Enabled.txt");
        for (Module m : Client.instance.getModuleManager().modules) {
            if (!m.isEnabled()) continue;
            m.setEnabled(false);
        }
        assert (enabled != null);
        for (String v : enabled) {
            Module m = Client.instance.getModuleManager().getModuleByName(v);
            if (m == null || m.isEnabled()) continue;
            m.setEnabled(true);
        }
        List<String> list = FileManager.read(dir, "Values.txt");
        assert (list != null);
        for (String v : list) {
            String name = v.split(":")[0];
            String values = v.split(":")[1];
            Module m = Client.instance.getModuleManager().getModuleByName(name);
            if (m == null) continue;
            for (Value value : m.getValues()) {
                if (!value.getName().equalsIgnoreCase(values)) continue;
                if (value instanceof Option) {
                    value.setValue(Boolean.parseBoolean(v.split(":")[2]));
                    continue;
                }
                if (value instanceof Numbers) {
                    value.setValue(Double.parseDouble(v.split(":")[2]));
                    continue;
                }
                ((Mode)value).setMode(v.split(":")[2]);
            }
        }
    }

    public void RemoveConfig(String dirs) {
        File dir = new File(FileManager.dir, dirs);
        if (!dir.exists()) {
            Helper.sendMessage("The configuration you are trying to delete does not exist!");
        } else {
            this.deleteFile(dir);
            Helper.sendMessage("Deleted configuration" + dirs + "Successfully!");
        }
    }

    public void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else {
                File[] listFiles = file.listFiles();
                assert (listFiles != null);
                for (File file2 : listFiles) {
                    this.deleteFile(file2);
                }
            }
            file.delete();
        } else {
            System.err.println("Path does not exist?");
        }
    }
}

