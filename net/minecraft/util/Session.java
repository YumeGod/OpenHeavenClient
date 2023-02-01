/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  com.mojang.authlib.GameProfile
 *  com.mojang.util.UUIDTypeAdapter
 */
package net.minecraft.util;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.util.UUIDTypeAdapter;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;

public class Session {
    private final String username;
    private final String playerID;
    private final String token;
    private final Type sessionType;
    private final ResourceLocation head;
    private final ThreadDownloadImageData imageData;

    public Session(String usernameIn, String playerIDIn, String tokenIn, String sessionTypeIn) {
        this.username = usernameIn;
        this.playerID = playerIDIn;
        this.token = tokenIn;
        this.sessionType = Type.setSessionType(sessionTypeIn);
        this.head = new ResourceLocation("heads/" + usernameIn);
        this.imageData = new ThreadDownloadImageData(null, "https://minotar.net/avatar/" + usernameIn, null, null);
    }

    public String getSessionID() {
        return "token:" + this.token + ":" + this.playerID;
    }

    public String getPlayerID() {
        return this.playerID;
    }

    public String getUsername() {
        return this.username;
    }

    public String getToken() {
        return this.token;
    }

    public GameProfile getProfile() {
        try {
            UUID uuid = UUIDTypeAdapter.fromString((String)this.playerID);
            return new GameProfile(uuid, this.username);
        }
        catch (IllegalArgumentException var2) {
            return new GameProfile((UUID)null, this.username);
        }
    }

    public Type getSessionType() {
        return this.sessionType;
    }

    public void loadHead() {
        Minecraft.getMinecraft();
        Minecraft.getTextureManager().loadTexture(this.head, this.imageData);
    }

    public ResourceLocation getHead() {
        return this.head;
    }

    public static enum Type {
        LEGACY("legacy"),
        MOJANG("mojang");

        private static final Map<String, Type> SESSION_TYPES;
        private final String sessionType;

        private Type(String sessionTypeIn) {
            this.sessionType = sessionTypeIn;
        }

        public static Type setSessionType(String sessionTypeIn) {
            return SESSION_TYPES.get(sessionTypeIn.toLowerCase());
        }

        static {
            SESSION_TYPES = Maps.newHashMap();
            for (Type session$type : Type.values()) {
                SESSION_TYPES.put(session$type.sessionType, session$type);
            }
        }
    }
}

