/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 */
package heaven.main.utils.string;

import heaven.main.utils.HttpUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class StringUtils {
    public static String translate(String string) {
        String text = StringUtils.getURLEncoderString(string);
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Connection", " keep-alive");
        header.put("Referer", "http://fanyi.youdao.com/translate");
        header.put("Accept-Language", " zh-CN,zh;q=0.8");
        return org.apache.commons.lang3.StringUtils.substringAfterLast((String)HttpUtils.get("http://fanyi.youdao.com//translate?i=" + text + "&type=AUTO&doctype=text&xmlVersion=1.1&keyfrom=360se", null, header), (String)"result=");
    }

    public static String getURLEncoderString(String str) {
        String result = "";
        try {
            result = URLEncoder.encode(str, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}

