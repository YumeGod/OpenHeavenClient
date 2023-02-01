/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.value;

import heaven.main.value.Value;
import java.util.function.Supplier;

public class Mode<V extends String>
extends Value<V> {
    public final V[] modes;

    public Mode(String name, V[] modes, V value) {
        super(name, value, () -> true, () -> true, () -> true);
        this.modes = modes;
        for (int i = 0; i < this.getModes().length; ++i) {
            this.mode.add(this.getModes()[i]);
        }
    }

    public Mode(String name, V[] modes, V value, Supplier<Boolean> visitable) {
        super(name, value, visitable, () -> true, () -> true);
        this.modes = modes;
        for (int i = 0; i < this.getModes().length; ++i) {
            this.mode.add(this.getModes()[i]);
        }
    }

    public Mode(String name, V[] modes, V value, Supplier<Boolean> visitable, Supplier<Boolean> visitable2) {
        super(name, value, visitable, visitable2, () -> true);
        this.modes = modes;
        for (int i = 0; i < this.getModes().length; ++i) {
            this.mode.add(this.getModes()[i]);
        }
    }

    public Mode(String name, V[] modes, V value, Supplier<Boolean> visitable, Supplier<Boolean> visitable2, Supplier<Boolean> visitable3) {
        super(name, value, visitable, visitable2, visitable3);
        this.modes = modes;
        for (int i = 0; i < this.getModes().length; ++i) {
            this.mode.add(this.getModes()[i]);
        }
    }

    @Override
    public String getModeAt(int index) {
        return (String)this.mode.get(index);
    }

    public V[] getModes() {
        return this.modes;
    }

    public void setCurrentMode(int current) {
        if (current > this.mode.size() - 1) {
            System.out.println("Value is to big! Set to 0. (" + this.mode.size() + ")");
            return;
        }
        this.current = current;
        this.setValue(this.modes[current]);
    }

    public String getModeAsString() {
        return (String)this.getValue();
    }

    public void setMode(String mode) {
        for (V e : this.modes) {
            if (!((String)e).equalsIgnoreCase(mode)) continue;
            this.setValue(e);
        }
    }

    public void setMode(int mode) {
        this.setValue(this.modes[mode]);
    }

    public boolean isValid(String name) {
        for (V e : this.modes) {
            if (!((String)e).equalsIgnoreCase(name)) continue;
            return true;
        }
        return false;
    }

    public boolean is(String mode) {
        return this.isCurrentMode(mode);
    }

    public boolean isCurrentMode(String mode) {
        return ((String)this.getValue()).equalsIgnoreCase(mode);
    }

    public boolean isCurrentModes(String mode) {
        return ((String)this.getValue()).toLowerCase().contains(mode.toLowerCase());
    }
}

