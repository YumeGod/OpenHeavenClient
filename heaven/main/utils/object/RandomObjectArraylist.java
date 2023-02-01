/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.utils.object;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class RandomObjectArraylist<E>
extends ArrayList<E> {
    @SafeVarargs
    public RandomObjectArraylist(E ... things) {
        this.addAll(Arrays.asList(things));
    }

    public E getRandomObject() {
        return this.size() == 0 ? null : (E)this.get(new Random().nextInt(this.size() - 1));
    }
}

