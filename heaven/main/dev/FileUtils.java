/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.dev;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

public class FileUtils {
    public static File writeInputStreamTo(InputStream inputStream, String fileName) {
        return FileUtils.writeInputStreamTo(inputStream, "", fileName);
    }

    public static File writeInputStreamTo(InputStream inputStream, String path, String fileName) {
        new File(path).mkdirs();
        File writingFile = new File(path + fileName);
        try {
            int length;
            FileOutputStream os = new FileOutputStream(writingFile);
            byte[] b = new byte[1024];
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
                os.flush();
            }
            os.close();
            inputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return writingFile;
    }

    public static void writeStringTo(String str, String path, String fileName) {
        try {
            new File(path).mkdirs();
            File writingFile = new File(path + fileName);
            if (!writingFile.exists()) {
                writingFile.createNewFile();
            }
            try (FileWriter writer = new FileWriter(writingFile);){
                writer.write(str);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String read(File f) throws IOException {
        String temp;
        StringBuilder stringBuffer = new StringBuilder();
        FileReader fileReader = new FileReader(f);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        while ((temp = bufferedReader.readLine()) != null) {
            stringBuffer.append(temp).append("\n");
        }
        bufferedReader.close();
        fileReader.close();
        return stringBuffer.toString();
    }

    public static String read(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : FileUtils.readLine(inputStream)) {
            stringBuilder.append(s).append("\n");
        }
        return stringBuilder.toString();
    }

    public static List<String> readLine(InputStream inputStream) {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<String>();
        try {
            String line;
            InputStreamReader isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            br.close();
            isr.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static File browseFile(int mode, String dialogTitle) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(mode);
        fileChooser.setCurrentDirectory(new File("").getAbsoluteFile());
        fileChooser.setDialogTitle(dialogTitle);
        fileChooser.setApproveButtonText("\u786e\u5b9a");
        fileChooser.showOpenDialog(new JLabel());
        return fileChooser.getSelectedFile();
    }

    public static File[] browseFiles(int mode, String dialogTitle) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(mode);
        fileChooser.setCurrentDirectory(new File("").getAbsoluteFile());
        fileChooser.setDialogTitle(dialogTitle);
        fileChooser.setApproveButtonText("\u786e\u5b9a");
        fileChooser.showOpenDialog(new JLabel());
        return fileChooser.getSelectedFiles();
    }

    public static void downloadURL(String url, String path, String fileName) throws IOException {
        FileUtils.downloadURL(new URL(url), path, fileName);
    }

    public static void downloadURL(URL url, String path, String fileName) throws IOException {
        FileUtils.writeInputStreamTo(url.openStream(), path, fileName);
    }

    public static boolean deleteFile(File dirFile) {
        if (!dirFile.exists()) {
            return false;
        }
        if (dirFile.isFile()) {
            return dirFile.delete();
        }
        for (File file : Objects.requireNonNull(dirFile.listFiles())) {
            FileUtils.deleteFile(file);
        }
        return dirFile.delete();
    }
}

