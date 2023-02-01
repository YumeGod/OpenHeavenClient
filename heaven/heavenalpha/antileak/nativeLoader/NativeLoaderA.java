/*
 * Decompiled with CFR 0.152.
 */
package heaven.heavenalpha.antileak.nativeLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class NativeLoaderA {
    public static void startLoadAllDLL(String resourceName, String writeName) throws IOException {
        File file = new File(writeName);
        InputStream resource = System.class.getResourceAsStream("/" + resourceName);
        if (resource != null) {
            int i;
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[128];
            while ((i = resource.read(buffer)) != -1) {
                fos.write(buffer, 0, i);
            }
            fos.close();
            resource.close();
            System.load(file.getAbsolutePath());
        } else {
            System.err.println("DLL is Null -> " + resourceName);
        }
    }
}

