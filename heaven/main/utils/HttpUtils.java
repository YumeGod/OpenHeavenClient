/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 */
package heaven.main.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class HttpUtils {
    public static String get(String url, Map<String, String> params) {
        return HttpUtils.get(url, params, null);
    }

    public static String get(String url, Map<String, String> params, Map<String, String> headers) {
        return HttpUtils.request(HttpUtils.mapToString(url, params, "?"), null, headers, "GET");
    }

    public static void getAsyn(String url, Map<String, String> params, OnHttpResult onHttpResult) {
        HttpUtils.getAsyn(url, params, null, onHttpResult);
    }

    public static void getAsyn(String url, Map<String, String> params, Map<String, String> headers, OnHttpResult onHttpResult) {
        HttpUtils.requestAsyn(HttpUtils.mapToString(url, params, "?"), null, headers, "GET", onHttpResult);
    }

    public static String post(String url, Map<String, String> params) {
        return HttpUtils.post(url, params, null);
    }

    public static String post(String url, Map<String, String> params, Map<String, String> headers) {
        return HttpUtils.request(url, HttpUtils.mapToString(null, params, null), headers, "POST");
    }

    public static void postAsyn(String url, Map<String, String> params, OnHttpResult onHttpResult) {
        HttpUtils.postAsyn(url, params, null, onHttpResult);
    }

    public static void postAsyn(String url, Map<String, String> params, Map<String, String> headers, OnHttpResult onHttpResult) {
        HttpUtils.requestAsyn(url, HttpUtils.mapToString(null, params, null), headers, "POST", onHttpResult);
    }

    public static String put(String url, Map<String, String> params) {
        return HttpUtils.put(url, params, null);
    }

    public static String put(String url, Map<String, String> params, Map<String, String> headers) {
        return HttpUtils.request(url, HttpUtils.mapToString(null, params, null), headers, "PUT");
    }

    public static void putAsyn(String url, Map<String, String> params, OnHttpResult onHttpResult) {
        HttpUtils.putAsyn(url, params, null, onHttpResult);
    }

    public static void putAsyn(String url, Map<String, String> params, Map<String, String> headers, OnHttpResult onHttpResult) {
        HttpUtils.requestAsyn(url, HttpUtils.mapToString(null, params, null), headers, "PUT", onHttpResult);
    }

    public static String delete(String url, Map<String, String> params) {
        return HttpUtils.delete(url, params, null);
    }

    public static String delete(String url, Map<String, String> params, Map<String, String> headers) {
        return HttpUtils.request(HttpUtils.mapToString(url, params, "?"), null, headers, "DELETE");
    }

    public static void deleteAsyn(String url, Map<String, String> params, OnHttpResult onHttpResult) {
        HttpUtils.deleteAsyn(url, params, null, onHttpResult);
    }

    public static void deleteAsyn(String url, Map<String, String> params, Map<String, String> headers, OnHttpResult onHttpResult) {
        HttpUtils.requestAsyn(HttpUtils.mapToString(url, params, "?"), null, headers, "DELETE", onHttpResult);
    }

    public static String request(String url, String params, Map<String, String> headers, String method) {
        return HttpUtils.request(url, params, headers, method, "application/x-www-form-urlencoded");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String request(String url, String params, Map<String, String> headers, String method, String mediaType) {
        String result = null;
        if (url == null || url.trim().isEmpty()) {
            return null;
        }
        method = method.toUpperCase();
        OutputStreamWriter writer = null;
        BufferedReader in = null;
        ByteArrayOutputStream resOut = null;
        try {
            URL httpUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)httpUrl.openConnection();
            if (method.equals("POST") || method.equals("PUT")) {
                conn.setDoOutput(true);
                conn.setUseCaches(false);
            }
            conn.setReadTimeout(8000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod(method);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("Content-Type", mediaType);
            if (headers != null) {
                for (String key : headers.keySet()) {
                    conn.setRequestProperty(key, headers.get(key));
                }
            }
            if (params != null) {
                conn.setRequestProperty("Content-Length", String.valueOf(params.length()));
                writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(params);
                writer.flush();
            }
            if (conn.getResponseCode() >= 300) {
                throw new RuntimeException("HTTP Request is not success, Response code is " + conn.getResponseCode());
            }
            StringBuffer sb = new StringBuffer("");
            InputStream input = conn.getInputStream();
            in = new BufferedReader(new InputStreamReader(input, "utf-8"));
            String line = "";
            while ((line = in.readLine()) != null) {
                sb.append(line + System.lineSeparator());
            }
            result = sb.toString();
            conn.disconnect();
            in.close();
            input.close();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (resOut != null) {
                try {
                    resOut.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static void requestAsyn(String url, String params, Map<String, String> headers, String method, OnHttpResult onHttpResult) {
        HttpUtils.requestAsyn(url, params, headers, method, "application/x-www-form-urlencoded", onHttpResult);
    }

    public static Gson gson() {
        return new GsonBuilder().disableHtmlEscaping().create();
    }

    public static void requestAsyn(final String url, final String params, final Map<String, String> headers, final String method, final String mediaType, final OnHttpResult onHttpResult) {
        new Thread(new Runnable(){

            @Override
            public void run() {
                try {
                    String result = HttpUtils.request(url, params, headers, method, mediaType);
                    onHttpResult.onSuccess(result);
                }
                catch (Exception e) {
                    onHttpResult.onError(e.getMessage());
                }
            }
        }).start();
    }

    private static String mapToString(String url, Map<String, String> params, String first) {
        StringBuilder sb = url != null ? new StringBuilder(url) : new StringBuilder();
        if (params != null) {
            boolean isFirst = true;
            for (String key : params.keySet()) {
                if (isFirst) {
                    if (first != null) {
                        sb.append(first);
                    }
                    isFirst = false;
                } else {
                    sb.append("&");
                }
                sb.append(key);
                sb.append("=");
                sb.append(params.get(key));
            }
        }
        return sb.toString();
    }

    public static interface OnHttpResult {
        public void onSuccess(String var1);

        public void onError(String var1);
    }
}

