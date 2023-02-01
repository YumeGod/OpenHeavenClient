/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.io.Charsets
 *  org.apache.commons.io.IOUtils
 *  org.apache.commons.lang3.Validate
 */
package heaven.heavenalpha.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;

public class HttpUtil {
    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final Base64.Decoder decoder = Base64.getDecoder();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String sendGet(String url) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            try {
                String line;
                URL realUrl = new URL(url);
                URLConnection connection = realUrl.openConnection();
                connection.setDoOutput(true);
                connection.setReadTimeout(99781);
                connection.setRequestProperty("accept", "*/*");
                connection.setRequestProperty("connection", "Keep-Alive");
                connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1) ZiMinClient;Chrome 69");
                connection.connect();
                connection.getHeaderFields();
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = in.readLine()) != null) {
                    result.append(line).append("\n");
                }
            }
            catch (Exception exception) {
                try {
                    if (in != null) {
                        in.close();
                    }
                }
                catch (Exception exception2) {
                    // empty catch block
                }
            }
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            }
            catch (Exception exception) {}
        }
        return HttpUtil.decode(HttpUtil.encode(result.toString()));
    }

    private static String encode(String text) {
        byte[] textByte = text.getBytes(StandardCharsets.UTF_8);
        return encoder.encodeToString(textByte);
    }

    public static String decode(String encodedText) {
        String text = new String(decoder.decode(encodedText), StandardCharsets.UTF_8);
        return text;
    }

    public static HttpURLConnection createUrlConnection(URL url) throws IOException {
        Validate.notNull((Object)url);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        connection.setUseCaches(false);
        return connection;
    }

    public static String performGetRequest(URL url) throws IOException {
        return new HttpUtil().performGetRequestWithoutStatic(url, false);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String performGetRequestWithoutStatic(URL url, boolean withKey) throws IOException {
        String var6;
        Validate.notNull((Object)url);
        HttpURLConnection connection = HttpUtil.createUrlConnection(url);
        InputStream inputStream = null;
        connection.setRequestProperty("user-agent", "Mozilla/5.0 AppIeWebKit");
        if (withKey) {
            connection.setRequestProperty("xf-api-key", "LnM-qSeQqtJlJmJnVt76GhU-SoiolWs9");
        }
        try {
            inputStream = connection.getInputStream();
            String string = IOUtils.toString((InputStream)inputStream, (Charset)Charsets.UTF_8);
            return string;
        }
        catch (IOException var10) {
            String result;
            IOUtils.closeQuietly((InputStream)inputStream);
            inputStream = connection.getErrorStream();
            if (inputStream == null) {
                throw var10;
            }
            var6 = result = IOUtils.toString((InputStream)inputStream, (Charset)Charsets.UTF_8);
        }
        finally {
            IOUtils.closeQuietly((InputStream)inputStream);
        }
        return var6;
    }

    public static String performGetRequest(URL url, boolean withKey) throws IOException {
        return new HttpUtil().performGetRequestWithoutStatic(url, withKey);
    }
}

