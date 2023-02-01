/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.ui.gui.alt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;

public class Alt {
    private final String account;
    private final String password;
    private final String userName;
    private final ResourceLocation head;
    private final ThreadDownloadImageData imageData;

    public Alt(String account, String password, String userName) {
        this.account = account;
        this.password = password;
        this.userName = userName;
        this.head = new ResourceLocation("heads/" + userName);
        this.imageData = new ThreadDownloadImageData(null, "https://minotar.net/avatar/" + userName, null, null);
    }

    public void loadHead() {
        Minecraft.getMinecraft();
        Minecraft.getTextureManager().loadTexture(this.head, this.imageData);
    }

    public String getAccount() {
        return this.account;
    }

    public String getPassword() {
        return this.password;
    }

    public String getUserName() {
        return this.userName;
    }

    public ResourceLocation getHead() {
        return this.head;
    }

    public ThreadDownloadImageData getImageData() {
        return this.imageData;
    }
}

