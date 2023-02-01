/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.globals;

import heaven.main.event.EventHandler;
import heaven.main.event.events.misc.EventChat;
import heaven.main.event.events.world.EventPacketSend;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.chat.Helper;
import heaven.main.value.Mode;
import heaven.main.value.Option;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class Chat
extends Module {
    private final Option<Boolean> antisb = new Option<Boolean>("AntiMotherFuck", false);
    private final Option<Boolean> sayColor = new Option<Boolean>("SayColor", false);
    public static final Option<Boolean> bgshadow = new Option<Boolean>("ChatBGShadow", false);
    public static final Mode<String> animations = new Mode("ChatAnimations", new String[]{"Off", "HeightSmooth"}, "Off");
    public static final Mode<String> font = new Mode("ChatFont", new String[]{"Normal", "Crack"}, "Normal");

    public Chat() {
        super("Chat", ModuleType.Globals);
        this.addValues(font, animations, bgshadow, this.antisb, this.sayColor);
        this.setRemoved(true);
    }

    @EventHandler
    public void onPacketSend(EventPacketSend event) {
        if (((Boolean)this.sayColor.getValue()).booleanValue() && event.getPacket() instanceof C01PacketChatMessage) {
            C01PacketChatMessage c01 = (C01PacketChatMessage)event.getPacket();
            Helper.sendMessage("1");
            event.setCancelled(true);
            mc.getNetHandler().addToSendQueue(new C01PacketChatMessage("\ufffd\ufffda" + c01.getMessage()));
        }
    }

    @EventHandler
    public void onChat(EventChat e) {
        if (((Boolean)this.antisb.getValue()).booleanValue() && (e.getMessage().contains("NMSL") || e.getMessage().contains("cnm") || e.getMessage().contains("nmsl") || e.getMessage().contains("NM$L") || e.getMessage().contains("nm$l") || e.getMessage().contains("fw") || e.getMessage().contains("FW") || e.getMessage().contains("Fw") || e.getMessage().contains("Nmsl") || e.getMessage().contains("Nm$l") || e.getMessage().contains("lj") || e.getMessage().contains("LJ") || e.getMessage().contains("hax") || e.getMessage().contains("\ufffd\ufffd\ufffd\ufffd") || e.getMessage().contains("\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd") || e.getMessage().contains("\ufffd\ufffd\ufffd") || e.getMessage().contains("\ufffd\ufffd\ufffd\ufffd") || e.getMessage().contains("[PowerX]") || e.getMessage().contains("biss") || e.getMessage().contains("[LiquidBounce]") || e.getMessage().contains("\ufffd\ufffd\ufffd\ufffd") || e.getMessage().contains("\u022b\ufffd\ufffd") || e.getMessage().contains("\ufffd\ufffd\ufffd") || e.getMessage().contains("\ufffd\ufffd\u0331") || e.getMessage().contains("gck") || e.getMessage().contains("\ufffd\u037b\ufffd\ufffd\ufffd") || e.getMessage().contains("RMB") || e.getMessage().contains("wdnmd") || e.getMessage().contains("CNY") || e.getMessage().contains("\ufffd\ufffd\ufffd\ufffd") || e.getMessage().contains("\ufffd\ufffd\u047b\ufffd\u0221") || e.getMessage().contains("\ufffd\u06b2\ufffd") || e.getMessage().contains("\ufffd\ufffdQ\u023a") || e.getMessage().contains("\ufffd\ufffd\ufffd\u07f3\ufffd\u016d") || e.getMessage().contains("maikama.cn") || e.getMessage().contains("\ufffd\ufffd16") || e.getMessage().contains("\ufffd\u06bf\ufffd") || e.getMessage().contains("\ufffd\u01b9\ufffd") || e.getMessage().contains("\ufffd\u01b9\ufffd") || e.getMessage().contains("\ufffd\u0532\ufffd") || e.getMessage().contains("\ufffd\u0236\ufffd") || e.getMessage().contains("\ufffd\ufffd") || e.getMessage().contains("url.cn") || e.getMessage().contains("anpaiba.top") || e.getMessage().contains("\u0271\ufffd\ufffd") || e.getMessage().contains("nm sl") || e.getMessage().contains("https://") || e.getMessage().contains("sb") || e.getMessage().contains("\ufffd\ufffd\ufffd") || e.getMessage().contains("\ufffd\ufffd\ufffd\ufffd") || e.getMessage().contains("QQ\u023a") || e.getMessage().contains("LLL") || e.getMessage().contains("fW") || e.getMessage().contains("Client"))) {
            e.setCancelled(true);
        }
    }
}

