/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventAttack;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import net.minecraft.util.EnumParticleTypes;

public class MoreParticles
extends Module {
    private final Option<Boolean> critp = new Option<Boolean>("CritParticle", false);
    private final Option<Boolean> np = new Option<Boolean>("NormalParticle", false);
    private final Numbers<Double> crack = new Numbers<Double>("CrackSize", 2.0, 1.0, 10.0, 1.0);

    public MoreParticles() {
        super("MoreParticles", new String[]{"mp"}, ModuleType.Render);
        this.addValues(this.crack, this.critp, this.np);
    }

    @EventHandler
    private void onAttack(EventAttack e) {
        this.setSuffix(Integer.valueOf(((Double)this.crack.getValue()).intValue()));
        int i = ((Double)this.crack.getValue()).intValue();
        for (int index = 0; index < i; ++index) {
            if (((Boolean)this.critp.getValue()).booleanValue()) {
                MoreParticles.mc.effectRenderer.emitParticleAtEntity(e.getEntity(), EnumParticleTypes.CRIT);
            }
            if (!((Boolean)this.np.getValue()).booleanValue()) continue;
            MoreParticles.mc.effectRenderer.emitParticleAtEntity(e.getEntity(), EnumParticleTypes.CRIT_MAGIC);
        }
    }
}

