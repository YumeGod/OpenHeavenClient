/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.management;

import heaven.main.Client;
import heaven.main.ui.gui.login.Alt;
import heaven.main.ui.gui.login.AltManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;

public class FileManager {
    public static final File dir;
    private static final File ALT;
    private static final File LASTALT;
    private static final Pattern COMPILE;
    private static final Pattern PATTERN;

    private static void loadLastAlt() {
        try {
            if (!LASTALT.exists()) {
                PrintWriter printWriter = new PrintWriter(new FileWriter(LASTALT));
                printWriter.println();
                printWriter.close();
            } else if (LASTALT.exists()) {
                String s;
                BufferedReader bufferedReader = new BufferedReader(new FileReader(LASTALT));
                while ((s = bufferedReader.readLine()) != null) {
                    if (s.contains("\t")) {
                        s = s.replace("\t", "    ");
                    }
                    if (s.contains("    ")) {
                        String[] parts = s.split(" {4}");
                        String[] account = parts[1].split(":");
                        if (account.length == 2) {
                            AltManager.setLastAlt(new Alt(account[0], account[1], parts[0]));
                            continue;
                        }
                        String pw = account[1];
                        for (int i = 2; i < account.length; ++i) {
                            pw = pw + ":" + account[i];
                        }
                        AltManager.setLastAlt(new Alt(account[0], pw, parts[0]));
                        continue;
                    }
                    String[] account2 = s.split(":");
                    if (account2.length == 1) {
                        AltManager.setLastAlt(new Alt(account2[0], ""));
                        continue;
                    }
                    if (account2.length == 2) {
                        AltManager.setLastAlt(new Alt(account2[0], account2[1]));
                        continue;
                    }
                    String pw2 = account2[1];
                    for (int j = 2; j < account2.length; ++j) {
                        pw2 = pw2 + ":" + account2[j];
                    }
                    AltManager.setLastAlt(new Alt(account2[0], pw2));
                }
                bufferedReader.close();
            }
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static void saveLastAlt() {
        try {
            PrintWriter printWriter = new PrintWriter(LASTALT);
            Alt alt = AltManager.getLastAlt();
            if (alt != null) {
                if (alt.getMask().isEmpty()) {
                    printWriter.println(alt.getUsername() + ":" + alt.getPassword());
                } else {
                    printWriter.println(alt.getMask() + "    " + alt.getUsername() + ":" + alt.getPassword());
                }
            }
            printWriter.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void loadAlts() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(ALT));
            if (!ALT.exists()) {
                PrintWriter printWriter = new PrintWriter(new FileWriter(ALT));
                printWriter.println();
                printWriter.close();
            } else if (ALT.exists()) {
                String s;
                while ((s = bufferedReader.readLine()) != null) {
                    if (s.contains("\t")) {
                        s = s.replace("\t", "    ");
                    }
                    if (s.contains("    ")) {
                        String[] parts = s.split(" {4}");
                        String[] account = parts[1].split(":");
                        if (account.length == 2) {
                            AltManager.getAlts().add(new Alt(account[0], account[1], parts[0]));
                            continue;
                        }
                        String pw = account[1];
                        for (int i = 2; i < account.length; ++i) {
                            pw = pw + ":" + account[i];
                        }
                        AltManager.getAlts().add(new Alt(account[0], pw, parts[0]));
                        continue;
                    }
                    String[] account2 = s.split(":");
                    if (account2.length == 1) {
                        AltManager.getAlts().add(new Alt(account2[0], ""));
                        continue;
                    }
                    if (account2.length == 2) {
                        try {
                            AltManager.getAlts().add(new Alt(account2[0], account2[1]));
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    String pw2 = account2[1];
                    for (int j = 2; j < account2.length; ++j) {
                        pw2 = pw2 + ":" + account2[j];
                    }
                    AltManager.getAlts().add(new Alt(account2[0], pw2));
                }
            }
            bufferedReader.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static void saveAlts() {
        try {
            PrintWriter printWriter = new PrintWriter(ALT);
            for (Alt alt : AltManager.getAlts()) {
                if (alt.getMask().isEmpty()) {
                    printWriter.println(alt.getUsername() + ":" + alt.getPassword());
                    continue;
                }
                printWriter.println(alt.getMask() + "    " + alt.getUsername() + ":" + alt.getPassword());
            }
            printWriter.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static File getConfigFile(String name) {
        File file = new File(dir, String.format("%s.txt", name));
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        return file;
    }

    public static void init() {
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static List<String> read(String file) {
        ArrayList<String> out = new ArrayList<String>();
        try {
            File f;
            if (!dir.exists()) {
                dir.mkdir();
            }
            if (!(f = new File(dir, file)).exists()) {
                f.createNewFile();
            }
            try (FileInputStream fis = new FileInputStream(f);){
                try (InputStreamReader isr = new InputStreamReader(fis);
                     BufferedReader br = new BufferedReader(isr);){
                    String line;
                    while ((line = br.readLine()) != null) {
                        out.add(line);
                    }
                }
                fis.close();
                ArrayList<String> arrayList = out;
                return arrayList;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return out;
        }
    }

    public static void save(String file, String content, boolean append) {
        try {
            File f = new File(dir, file);
            if (!f.exists()) {
                f.createNewFile();
            }
            try (FileWriter writer = new FileWriter(f, append);){
                writer.write(content);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Loose catch block
     */
    public static List<String> read(File dir, String file) {
        ArrayList<String> arrayList;
        Throwable throwable;
        FileInputStream fis;
        ArrayList<String> out;
        block50: {
            block51: {
                out = new ArrayList<String>();
                if (!dir.exists()) {
                    return null;
                }
                File f = new File(FileManager.dir, file);
                if (!f.exists()) {
                    f.createNewFile();
                }
                fis = new FileInputStream(f);
                throwable = null;
                try (InputStreamReader isr = new InputStreamReader(fis);
                     BufferedReader br = new BufferedReader(isr);){
                    String line;
                    while ((line = br.readLine()) != null) {
                        out.add(line);
                    }
                }
                fis.close();
                arrayList = out;
                if (fis == null) break block50;
                if (throwable == null) break block51;
                try {
                    fis.close();
                }
                catch (Throwable throwable2) {
                    throwable.addSuppressed(throwable2);
                }
                break block50;
            }
            fis.close();
        }
        return arrayList;
        catch (Throwable throwable3) {
            try {
                try {
                    throwable = throwable3;
                    throw throwable3;
                }
                catch (Throwable throwable4) {
                    if (fis != null) {
                        if (throwable != null) {
                            try {
                                fis.close();
                            }
                            catch (Throwable throwable5) {
                                throwable.addSuppressed(throwable5);
                            }
                        } else {
                            fis.close();
                        }
                    }
                    throw throwable4;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                return out;
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void save(File dir, String file, String content, boolean append) {
        try {
            File f = new File(dir, file);
            if (!f.exists()) {
                f.createNewFile();
            }
            try (FileWriter writer = new FileWriter(f, append);){
                writer.write(content);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    static {
        COMPILE = Pattern.compile(" {4}");
        PATTERN = Pattern.compile(" {4}");
        File mcDataDir = Minecraft.getMinecraft().mcDataDir;
        dir = new File(mcDataDir, Client.instance.name);
        ALT = FileManager.getConfigFile("Alts");
        LASTALT = FileManager.getConfigFile("LastAlt");
    }
}

