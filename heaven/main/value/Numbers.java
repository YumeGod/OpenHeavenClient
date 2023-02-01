/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.value;

import heaven.main.value.Value;
import java.util.function.Supplier;

public class Numbers<T extends Number>
extends Value<T> {
    public T min;
    public T max;
    public T inc;
    private final boolean integer;

    public Numbers(String name, T value, T min, T max, T inc) {
        super(name, value, () -> true, () -> true, () -> true);
        this.min = min;
        this.max = max;
        this.inc = inc;
        this.integer = false;
    }

    public Numbers(String name, T value, T min, T max, T inc, Supplier<Boolean> visitable) {
        super(name, value, visitable, () -> true, () -> true);
        this.min = min;
        this.max = max;
        this.inc = inc;
        this.integer = false;
    }

    public Numbers(String name, T value, T min, T max, T inc, Supplier<Boolean> visitable, Supplier<Boolean> visitable2) {
        super(name, value, visitable, visitable2, () -> true);
        this.min = min;
        this.max = max;
        this.inc = inc;
        this.integer = false;
    }

    public Numbers(String name, T value, T min, T max, T inc, Supplier<Boolean> visitable, Supplier<Boolean> visitable2, Supplier<Boolean> visitable3) {
        super(name, value, visitable, visitable2, visitable3);
        this.min = min;
        this.max = max;
        this.inc = inc;
        this.integer = false;
    }

    public T getMinimum() {
        return this.min;
    }

    public T getMaximum() {
        return this.max;
    }

    public void setIncrement(T inc) {
        this.inc = inc;
    }

    public T getIncrement() {
        return this.inc;
    }

    public boolean isInteger() {
        return this.integer;
    }
}

