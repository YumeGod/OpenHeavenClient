/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.ui.gui.alt.saveSystem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class FileWrite {
    private static final HashMap<String, File> pathMap = new HashMap();
    private static final HashMap<String, File> fileMap = new HashMap();

    public static void writeStringTo(String str, String path, String fileName) {
        try {
            File pathFile = pathMap.get(path);
            if (pathFile == null) {
                pathFile = new File(path);
                pathMap.put(path, pathFile);
            }
            pathFile.mkdirs();
            String pathFileName = path + fileName;
            File writingFile = fileMap.get(pathFileName);
            if (writingFile == null) {
                writingFile = new File(pathFileName);
                fileMap.put(pathFileName, writingFile);
            }
            if (!writingFile.exists()) {
                writingFile.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter((OutputStream)new FileOutputStream(writingFile), StandardCharsets.UTF_8));
            writer.write(str);
            writer.flush();
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        pathMap.clear();
        fileMap.clear();
    }
}

