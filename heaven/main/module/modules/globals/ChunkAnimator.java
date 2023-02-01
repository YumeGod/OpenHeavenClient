/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.globals;

import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.AnimationHandler;

public class ChunkAnimator
extends Module {
    public AnimationHandler animation = new AnimationHandler();

    public ChunkAnimator() {
        super("ChunkAnimator", ModuleType.Globals);
        this.setRemoved(true);
    }
}

