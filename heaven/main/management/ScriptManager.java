/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  utils.hodgepodge.io.FileUtils
 */
package heaven.main.management;

import com.compiler.CompilerUtils;
import heaven.main.Client;
import heaven.main.management.FileManager;
import heaven.main.module.Module;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import utils.hodgepodge.io.FileUtils;

public final class ScriptManager {
    public static final ScriptManager Instance = new ScriptManager();
    private final File scriptFolder = new File(FileManager.dir, "scripts/");

    private ScriptManager() {
    }

    public void loadScript() throws Exception {
        if (this.scriptFolder.exists()) {
            File[] scriptFiles = this.scriptFolder.listFiles();
            if (scriptFiles != null && scriptFiles.length != 0) {
                for (File scriptFile : scriptFiles) {
                    if (!scriptFile.getName().endsWith(".java")) continue;
                    String code = FileUtils.readFileAsString((File)scriptFile, (Charset)StandardCharsets.UTF_8);
                    Class clazz = CompilerUtils.loadFromJava(scriptFile.getName().split(".java")[0], code);
                    Object instance = clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                    if (!(instance instanceof Module)) continue;
                    Client.instance.getModuleManager().regMods((Module)instance);
                    System.out.println("Load script {}" + scriptFile.getAbsolutePath());
                }
            }
        } else {
            this.scriptFolder.mkdirs();
        }
    }
}

