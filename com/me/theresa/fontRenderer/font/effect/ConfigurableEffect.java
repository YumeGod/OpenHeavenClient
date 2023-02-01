/*
 * Decompiled with CFR 0.152.
 */
package com.me.theresa.fontRenderer.font.effect;

import com.me.theresa.fontRenderer.font.effect.Effect;
import java.util.List;

public interface ConfigurableEffect
extends Effect {
    public List getValues();

    public void setValues(List var1);

    public static interface Value {
        public String getName();

        public String getString();

        public void setString(String var1);

        public Object getObject();

        public void showDialog();
    }
}

