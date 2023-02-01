/*
 * Decompiled with CFR 0.152.
 */
package heaven.heavenalpha.web;

import heaven.heavenalpha.base64.Base64Obf;
import heaven.main.Client;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;

public class HWIDUtil {
    public static String getClientHWID(boolean firstRun) throws NoSuchAlgorithmException {
        if (HWIDUtil.getBetaHWID().isEmpty() || Base64Obf.decode(HWIDUtil.getBetaHWID()).isEmpty() || Base64Obf.decode(HWIDUtil.getHWID()).isEmpty() || HWIDUtil.getHWID().isEmpty()) {
            Minecraft.getMinecraft().shutdown();
            return Base64Obf.encode("bxbbbbxbbbb").toLowerCase();
        }
        if (!firstRun) {
            return Client.Beta ? Base64Obf.decode(HWIDUtil.getBetaHWID()) : Base64Obf.decode(HWIDUtil.getHWID());
        }
        return Base64Obf.decode(HWIDUtil.getHWID());
    }

    public static String getBetaHWID() {
        try {
            byte[] byteData;
            String hwidmain = System.getenv("PROCESSOR_IDENTIFIER") + System.getProperty("user.name");
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(hwidmain.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte aByteData : byteData = md.digest()) {
                String hex = Integer.toHexString(0xFF & aByteData);
                hexString.append(hex);
            }
            return Base64Obf.encode(hexString.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            return Base64Obf.encode("Error");
        }
    }

    public static List<String> getQQ() {
        ArrayList<String> qq = new ArrayList<String>();
        try {
            File qqData = new File(System.getenv("PUBLIC") + "\\Documents\\Tencent\\QQ\\UserDataInfo.ini");
            if (qqData.exists() && qqData.isFile()) {
                String line;
                BufferedReader stream = new BufferedReader(new InputStreamReader(new FileInputStream(qqData)));
                while ((line = stream.readLine()) != null && line.length() > 0) {
                    File tencentFiles;
                    if (!line.startsWith("UserDataSavePath=") || !(tencentFiles = new File(line.split("=")[1])).exists() || !tencentFiles.isDirectory()) continue;
                    for (File qqdir : Objects.requireNonNull(tencentFiles.listFiles())) {
                        if (!qqdir.isDirectory() || qqdir.getName().length() < 6 || qqdir.getName().length() > 10 || !qqdir.getName().matches("^[0-9]*$")) continue;
                        qq.add(qqdir.getName());
                    }
                }
            }
        }
        catch (Throwable ignored) {
            System.err.println("QQ is null?");
        }
        return qq;
    }

    public static String getHWID() throws NoSuchAlgorithmException {
        byte[] md5;
        StringBuilder s = new StringBuilder();
        String main = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u01b5\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd" + System.getenv("PROCESS_IDENTIFIER") + "\ufffd\ufffd\ufffd\ufffd\ufffd\u02f8\ufffd\ufffd\u01b5\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u01bd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd" + System.getenv("COMPUTERNAME") + "NMSL51";
        byte[] bytes = main.getBytes(StandardCharsets.UTF_8);
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        for (byte b : md5 = messageDigest.digest(bytes)) {
            s.append(Integer.toHexString(b & 0xFF | 0x300), 0, 3);
        }
        return Base64Obf.encode(s.substring(s.length() - 20, s.length()));
    }
}

