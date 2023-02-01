/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 */
package heaven.main.ui.gui.alt;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpServer;
import heaven.main.utils.HttpUtils;
import java.awt.Desktop;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Get_Microsoft_base {

    public static class OpenExplorer {
        public static void open_Microsoft_Url() throws IOException {
            if (Desktop.isDesktopSupported()) {
                try {
                    URI uri = URI.create("https://login.live.com/oauth20_authorize.srf?scope=XboxLive.signin+offline_access&redirect_uri=http://127.0.0.1:30827&response_type=code&client_id=d91b4e68-6dc3-4321-a90f-d68b6769ff54&");
                    Desktop dp = Desktop.getDesktop();
                    if (dp.isSupported(Desktop.Action.BROWSE)) {
                        dp.browse(uri);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(30827), 0);
            httpServer.createContext("/", exchange -> {
                String code = exchange.getRequestURI().toString();
                code = code.substring(code.lastIndexOf(61) + 1);
                String Token_Url = "https://login.live.com/oauth20_token.srf";
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("client_id", "d91b4e68-6dc3-4321-a90f-d68b6769ff54");
                map.put("code", code);
                map.put("grant_type", "authorization_code");
                map.put("redirect_uri", "http://127.0.0.1:30827");
                String oauth = HttpUtils.post("https://login.live.com/oauth20_token.srf", map);
                String access_token = ((JsonObject)HttpUtils.gson().fromJson(oauth, JsonObject.class)).get("access_token").getAsString();
                HashMap<String, String> map1 = new HashMap<String, String>();
                map1.put("AuthMethod", "RPS");
                map1.put("SiteName", "user.auth.xboxlive.com");
                map1.put("RpsTicket", "d=" + access_token);
                HashMap<String, String> map2 = new HashMap<String, String>();
                map2.put("Properties", (String)((Object)map1));
                map2.put("RelyingParty", "http://auth.xboxlive.com");
                map2.put("TokenType", "JWT");
                JsonObject xbl = (JsonObject)HttpUtils.gson().fromJson(HttpUtils.post("https://user.auth.xboxlive.com/user/authenticate", map2), JsonObject.class);
                String xbl_token = xbl.get("Token").getAsString();
                String xbl_uhs = xbl.get("DisplayClaims").getAsJsonObject().get("xui").getAsJsonArray().get(0).getAsJsonObject().get("uhs").getAsString();
                HashMap<String, Object> map3 = new HashMap<String, Object>();
                map3.put("SandboxId", "RETAIL");
                map3.put("UserTokens", new String[]{xbl_token});
                HashMap<String, String> map4 = new HashMap<String, String>();
                map4.put("Properties", (String)((Object)map3));
                map4.put("RelyingParty", "rp://api.minecraftservices.com/");
                map4.put("TokenType", "JWT");
                JsonObject xsts = (JsonObject)HttpUtils.gson().fromJson(HttpUtils.post("https://xsts.auth.xboxlive.com/xsts/authorize", map4), JsonObject.class);
                String xsts_token = xsts.get("Token").getAsString();
                String xsts_uhs = xsts.get("DisplayClaims").getAsJsonObject().get("xui").getAsJsonArray().get(0).getAsJsonObject().get("uhs").getAsString();
                HashMap<String, String> map5 = new HashMap<String, String>();
                map5.put("identityToken", String.format("XBL3.0 x=%s;%s", xsts_uhs, xsts_token));
                System.out.println(HttpUtils.post("https://api.minecraftservices.com/authentication/login_with_xbox", map5));
                String success = "Success";
                exchange.sendResponseHeaders(200, success.length());
                OutputStream responseBody = exchange.getResponseBody();
                responseBody.write(success.getBytes(StandardCharsets.UTF_8));
                responseBody.close();
                httpServer.stop(2);
            });
            httpServer.setExecutor(null);
            httpServer.start();
        }
    }
}

