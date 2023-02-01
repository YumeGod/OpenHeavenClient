/*
 * Decompiled with CFR 0.152.
 */
package com.me.guichaguri.betterfps.transformers.cloner;

import com.me.guichaguri.betterfps.tweaker.Naming;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.FIELD, ElementType.METHOD})
public @interface Named {
    public Naming value();
}

