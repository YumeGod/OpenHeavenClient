/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.Agent
 *  com.mojang.authlib.exceptions.AuthenticationException
 *  com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
 *  com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication
 */
package heaven.main.ui.gui.login;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import heaven.main.management.FileManager;
import heaven.main.ui.gui.login.Alt;
import heaven.main.ui.gui.login.AltManager;
import java.net.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class AltLoginThread
extends Thread {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final String password;
    private String status;
    private final String username;

    public AltLoginThread(String username, String password) {
        super("Alt Login Thread");
        this.username = username;
        this.password = password;
        this.status = "\u00a7eWaiting...";
    }

    private static Session createSession(String username, String password) {
        YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(username);
        auth.setPassword(password);
        try {
            auth.logIn();
            return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
        }
        catch (AuthenticationException authenticationException) {
            return null;
        }
    }

    public String getStatus() {
        return this.status;
    }

    @Override
    public void run() {
        if (this.password.isEmpty()) {
            this.mc.session = new Session(this.username, "", "", "mojang");
            this.status = "\u00a7aUser is (" + this.username + ") now!";
            return;
        }
        this.status = "\u00a7eTry Logging...";
        Session auth = AltLoginThread.createSession(this.username, this.password);
        if (auth == null) {
            this.status = "\u00a7cLogin Error!";
        } else {
            AltManager.setLastAlt(new Alt(this.username, this.password));
            FileManager.saveLastAlt();
            this.status = "\u00a7aUser is (" + auth.getUsername() + ") now!";
            this.mc.session = auth;
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

