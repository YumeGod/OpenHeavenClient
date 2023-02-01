/*
 * Decompiled with CFR 0.152.
 */
package com.darkmagician6.eventapi.types;

public enum EventType {
    PRE,
    POST,
    SEND,
    RECIEVE;

    private static final EventType[] $VALUES;

    static {
        $VALUES = new EventType[]{PRE, POST, SEND, RECIEVE};
    }
}

