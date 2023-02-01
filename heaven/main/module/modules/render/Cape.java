/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRenderCape;
import heaven.main.management.FriendManager;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.value.Mode;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class Cape
extends Module {
    private final Mode<String> mode = new Mode("Mode", new String[]{"Enderman", "Creeper", "OneTap", "Astolfo", "Novoline", "PowerX", "LBDonate", "Exhibition", "Rise", "Vape", "Moon", "Valentine"}, "Enderman");
    private final ResourceLocation Enderman = new ResourceLocation("client/capes/Enderman.png");
    private final ResourceLocation otc = new ResourceLocation("client/capes/onetap.png");
    private final ResourceLocation ast = new ResourceLocation("client/capes/astolfo.png");
    private final ResourceLocation pxs = new ResourceLocation("client/capes/px.png");
    private final ResourceLocation novo = new ResourceLocation("client/capes/novoline.png");
    private final ResourceLocation lb = new ResourceLocation("client/capes/LBDonate.png");
    private final ResourceLocation crp = new ResourceLocation("client/capes/creeper.png");
    private final ResourceLocation vne = new ResourceLocation("client/capes/Valentine.png");
    private final ResourceLocation ex = new ResourceLocation("client/capes/exhi.png");
    private final ResourceLocation rise = new ResourceLocation("client/capes/rise.png");
    private final ResourceLocation vape = new ResourceLocation("client/capes/vape.png");
    private final ResourceLocation moon = new ResourceLocation("client/capes/moon.png");

    public Cape() {
        super("Cape", ModuleType.Render);
        this.addValues(this.mode);
    }

    @EventHandler
    public void onRender(EventRenderCape event) {
        block15: {
            block17: {
                block16: {
                    if (Minecraft.theWorld == null) break block15;
                    if (FriendManager.isFriend(event.getPlayer().getName())) break block16;
                    if (event.getPlayer() != Minecraft.thePlayer) break block17;
                }
                if (this.mode.isCurrentMode("Enderman")) {
                    event.setLocation(this.Enderman);
                }
            }
            if (this.mode.isCurrentMode("OneTap")) {
                event.setLocation(this.otc);
            }
            if (this.mode.isCurrentMode("Astolfo")) {
                event.setLocation(this.ast);
            }
            if (this.mode.isCurrentMode("Novoline")) {
                event.setLocation(this.novo);
            }
            if (this.mode.isCurrentMode("PowerX")) {
                event.setLocation(this.pxs);
            }
            if (this.mode.isCurrentMode("LBDonate")) {
                event.setLocation(this.lb);
            }
            if (this.mode.isCurrentMode("Exhibition")) {
                event.setLocation(this.ex);
            }
            if (this.mode.isCurrentMode("Rise")) {
                event.setLocation(this.rise);
            }
            if (this.mode.isCurrentMode("Vape")) {
                event.setLocation(this.vape);
            }
            if (this.mode.isCurrentMode("Creeper")) {
                event.setLocation(this.crp);
            }
            if (this.mode.isCurrentMode("Moon")) {
                event.setLocation(this.moon);
            }
            if (this.mode.isCurrentMode("Valentine")) {
                event.setLocation(this.vne);
            }
            event.setCancelled(true);
        }
    }
}

