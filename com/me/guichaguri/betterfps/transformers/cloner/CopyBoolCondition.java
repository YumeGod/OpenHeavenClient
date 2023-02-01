/*
 * Decompiled with CFR 0.152.
 */
package com.me.guichaguri.betterfps.transformers.cloner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface CopyBoolCondition {
    public String key();

    public boolean value() default true;
}

