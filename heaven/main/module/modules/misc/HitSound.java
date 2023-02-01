/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.misc;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.KillAura;
import heaven.main.utils.chat.Helper;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class HitSound
extends Module {
    public HitSound() {
        super("HitSound", ModuleType.Misc);
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        if (KillAura.target != null && KillAura.target.hurtResistantTime < 9) {
            this.doSound(KillAura.target);
        }
    }

    public void doSound(EntityLivingBase target) {
        double x = target.posX;
        double y = target.posY;
        double z = target.posZ;
        try {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource("client/audio/hitsound/skeet.wav"));
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            }
            catch (IOException | UnsupportedAudioFileException e) {
                e.printStackTrace();
            }
        }
        catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        Helper.addChat("TECHNO DOESNT KNOW WHAT A PENIS IS");
        mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("client/audio/hitsound/skeet.wav"), (float)x, (float)y, (float)z));
    }
}

