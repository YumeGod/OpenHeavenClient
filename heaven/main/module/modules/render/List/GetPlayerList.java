/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.render.List;

public class GetPlayerList {
    public final String name;
    public int killed;

    public GetPlayerList(String n, int k) {
        this.name = n;
        this.killed = k + this.killed;
    }
}

