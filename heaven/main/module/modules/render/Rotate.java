/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.render;

import heaven.main.Client;
import heaven.main.event.EventCancel;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.CustomAura;
import heaven.main.module.modules.world.BedFucker;
import heaven.main.module.modules.world.ChestAura;
import heaven.main.module.modules.world.Scaffold;
import heaven.main.value.Mode;

public class Rotate
extends Module {
    public static final Mode<String> cuka = new Mode("Custom Aura", new String[]{"Off", "Body", "Head", "Bitch"}, "Body");
    public static final Mode<String> ka = new Mode("Kill/TP Aura", new String[]{"Off", "Body", "Head", "Bitch"}, "Body");
    public static final Mode<String> scaffold = new Mode("Scaffold", new String[]{"Off", "Body", "Head", "Bitch"}, "Body");
    public static final Mode<String> fucker = new Mode("BedFucker", new String[]{"Off", "Body", "Head", "Bitch"}, "Body");
    public static final Mode<String> chest = new Mode("ChestAura", new String[]{"Off", "Body", "Head", "Bitch"}, "Body");
    public static final Mode<String> fire = new Mode("AntiFireBall", new String[]{"Off", "Body", "Head", "Bitch"}, "Body");
    public static float yaw;
    public float pitch;

    public Rotate() {
        super("RenderRotate", ModuleType.Render);
        this.addValues(ka, cuka, scaffold, fucker, chest, fire);
    }

    private boolean shouldRotate(String mode) {
        if (Client.instance.getModuleManager().getModuleByClass(CustomAura.class).isEnabled() && cuka.is(mode)) {
            return true;
        }
        if (Client.instance.getModuleManager().getModuleByClass(Scaffold.class).isEnabled() && scaffold.is(mode)) {
            return true;
        }
        if (Client.instance.getModuleManager().getModuleByClass(ChestAura.class).isEnabled() && chest.is(mode) && ChestAura.lastBlock != null) {
            return true;
        }
        return Client.instance.getModuleManager().getModuleByClass(BedFucker.class).isEnabled() && fucker.is(mode) && BedFucker.fucking != null && BedFucker.ready != null && BedFucker.self != null;
    }

    @EventCancel
    public void onUpdate() {
        if (this.shouldRotate("Bitch")) {
            yaw += 30.0f;
            this.pitch = this.pitch < -80.0f ? (this.pitch -= 10.0f) : 90.0f;
        }
    }
}

