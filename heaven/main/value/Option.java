/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.value;

import heaven.main.value.Value;
import java.util.function.Supplier;

public class Option<V>
extends Value<V> {
    public Option(String name, V enabled) {
        super(name, enabled, () -> true, () -> true, () -> true);
    }

    public Option(String name, V enabled, Supplier<Boolean> visitable) {
        super(name, enabled, visitable, () -> true, () -> true);
    }

    public Option(String name, V enabled, Supplier<Boolean> visitable, Supplier<Boolean> visitable2) {
        super(name, enabled, visitable, visitable2, () -> true);
    }

    public Option(String name, V enabled, Supplier<Boolean> visitable, Supplier<Boolean> visitable2, Supplier<Boolean> visitable3) {
        super(name, enabled, visitable, visitable2, visitable3);
    }
}

