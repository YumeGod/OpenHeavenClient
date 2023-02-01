/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.misc;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.timer.TimeHelper;
import heaven.main.value.Numbers;
import java.util.Random;
import net.minecraft.client.Minecraft;

public class Spammer
extends Module {
    private final TimeHelper timer = new TimeHelper();
    private final Numbers<Double> delay = new Numbers<Double>("Delay", 1.0, 0.1, 10.0, 0.1);
    private final Numbers<Double> random = new Numbers<Double>("Random", 6.0, 1.0, 36.0, 1.0);
    public String message;
    public static String bindmessage;

    public Spammer() {
        super("Spammer", new String[]{"spammer"}, ModuleType.Misc);
        this.addValues(this.delay, this.random);
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        this.message = bindmessage != null ? "[" + Client.instance.name + "] " + bindmessage : "[" + Client.instance.name + "] Get Good Get HeavenClient.QQgroup:142766499";
        long delayNew = (long)((Double)this.delay.getValue() * 1000.0);
        if (this.timer.isDelayComplete(delayNew)) {
            Minecraft.thePlayer.sendChatMessage(this.message + " <" + Spammer.getRandomString((Double)this.random.getValue()) + ">");
            this.timer.reset();
        }
    }

    private static String getRandomString(double d) {
        String str = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while ((double)i < d) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
            ++i;
        }
        return sb.toString();
    }
}

