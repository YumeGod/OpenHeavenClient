/*
 * Decompiled with CFR 0.152.
 */
package net.optifine.shaders;

public enum ProgramStage {
    NONE(""),
    SHADOW("shadow"),
    GBUFFERS("gbuffers"),
    DEFERRED("deferred"),
    COMPOSITE("composite");

    private final String name;

    private ProgramStage(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}

