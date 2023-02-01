/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.value;

import heaven.main.management.EventManager;
import heaven.main.value.Mode;
import java.util.ArrayList;
import java.util.function.Supplier;

public abstract class Value<V> {
    private String name;
    private V value;
    private final Supplier<Boolean> visitable;
    private final Supplier<Boolean> visitable2;
    private final Supplier<Boolean> visitable3;
    public boolean Downopen;
    public ArrayList<String> mode = new ArrayList();
    public int current;
    private String modeTitle;

    public Value(String name, V value, Supplier<Boolean> visitable, Supplier<Boolean> visitable2, Supplier<Boolean> visitable3) {
        this.name = name;
        this.visitable = visitable;
        this.visitable2 = visitable2;
        this.visitable3 = visitable3;
        this.value = value;
        EventManager.register(this);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public V get() {
        return this.value;
    }

    public V getValue() {
        return this.value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public boolean isVisitable() {
        return this.visitable.get() != false && this.visitable2.get() != false && this.visitable3.get() != false;
    }

    public ArrayList<String> listModes() {
        return this.mode;
    }

    public String getModeAt(String modeName) {
        for (int i = 0; i < this.mode.size(); ++i) {
            if (!this.mode.get(i).equalsIgnoreCase(modeName)) continue;
            return this.mode.get(i);
        }
        return "NULL";
    }

    public String getModeAt(int index) {
        return this.mode.get(index);
    }

    public String getModeTitle() {
        return this.modeTitle;
    }

    public int getCurrentMode() {
        return this.current;
    }

    public String getValue2() {
        return this.getModeAt(this.current);
    }

    public String getDisplayTitle() {
        if (this.value instanceof Mode) {
            return this.name;
        }
        return this.name.split("_")[1];
    }

    public boolean isDownopen() {
        return this.Downopen;
    }

    public void setDownopen(boolean b) {
        this.Downopen = b;
    }
}

