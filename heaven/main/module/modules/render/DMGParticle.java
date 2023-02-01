/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender3D;
import heaven.main.event.events.world.EventLivingUpdate;
import heaven.main.event.events.world.EventPacketSend;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.ParticlesUtils.Location;
import heaven.main.module.modules.render.ParticlesUtils.Particles;
import heaven.main.utils.render.RenderUtil;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class DMGParticle
extends Module {
    private final Mode<String> mode = new Mode("DisplayMode", new String[]{"Red", "Gold", "Texture"}, "Red");
    public final Numbers<Double> sizeV = new Numbers<Double>("Size", 3.0, 1.0, 10.0, 1.0);
    private final HashMap<EntityLivingBase, Float> healthMap;
    private final List<Particles> particles;
    private boolean isCriti;

    public DMGParticle() {
        super("DamageParticle", new String[]{"DMGParticle"}, ModuleType.Render);
        this.addValues(this.mode, this.sizeV);
        this.healthMap = new HashMap();
        this.particles = new ArrayList<Particles>();
    }

    @EventHandler
    public void onLivingUpdate(EventLivingUpdate e) {
        float health;
        float floatValue;
        EntityLivingBase entity = (EntityLivingBase)e.getEntity();
        if (entity == Minecraft.thePlayer) {
            return;
        }
        if (!this.healthMap.containsKey(entity)) {
            this.healthMap.put(entity, Float.valueOf(entity.getHealth()));
        }
        if ((floatValue = this.healthMap.get(entity).floatValue()) != (health = entity.getHealth())) {
            try {
                String text;
                if (floatValue - health < 0.0f) {
                    text = (Object)((Object)EnumChatFormatting.GREEN) + String.valueOf(this.roundToPlace((floatValue - health) * -1.0f, 1));
                } else if (this.isCriti) {
                    text = (Object)((Object)EnumChatFormatting.AQUA) + String.valueOf(this.roundToPlace(floatValue - health, 1));
                    this.isCriti = false;
                } else {
                    text = (Object)((Object)(this.mode.isCurrentMode("Red") ? EnumChatFormatting.RED : EnumChatFormatting.YELLOW)) + String.valueOf(this.roundToPlace(floatValue - health, 1));
                }
                Location location = new Location(entity);
                location.setY(entity.getEntityBoundingBox().minY + (entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) / 2.0);
                location.setX(location.getX() - 0.5 + (double)new Random(System.currentTimeMillis()).nextInt(5) * 0.1);
                location.setZ(location.getZ() - 0.5 + (double)new Random(System.currentTimeMillis() + 1L).nextInt(5) * 0.1);
                this.particles.add(new Particles(location, text));
                this.healthMap.remove(entity);
                this.healthMap.put(entity, Float.valueOf(entity.getHealth()));
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
    }

    @EventHandler
    public void onSendPacket(EventPacketSend e) {
        C02PacketUseEntity c02;
        if (e.getPacket() instanceof C02PacketUseEntity && (c02 = (C02PacketUseEntity)e.getPacket()).getAction() == C02PacketUseEntity.Action.INTERACT) {
            this.isCriti = true;
        }
    }

    @EventHandler
    public void onRender(EventRender3D e) {
        for (Particles p : this.particles) {
            double x = p.location.getX();
            double n = x - RenderManager.renderPosX;
            double y = p.location.getY();
            double n2 = y - RenderManager.renderPosY;
            double z = p.location.getZ();
            double n3 = z - RenderManager.renderPosZ;
            GlStateManager.pushMatrix();
            GlStateManager.enablePolygonOffset();
            GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
            GlStateManager.translate((float)n, (float)n2, (float)n3);
            mc.getRenderManager();
            GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
            float textY = DMGParticle.mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f;
            mc.getRenderManager();
            GlStateManager.rotate(RenderManager.playerViewX, textY, 0.0f, 0.0f);
            double size = (Double)this.sizeV.getValue() / 100.0;
            GlStateManager.scale(-size, -size, size);
            RenderUtil.enableGL2D();
            RenderUtil.disableGL2D();
            GL11.glDepthMask((boolean)false);
            if (this.mode.isCurrentMode("Texture")) {
                mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/icons.png"));
                if (p.text.contains(String.valueOf((Object)EnumChatFormatting.GREEN))) {
                    DMGParticle.mc.ingameGUI.drawTexturedModalRect(-(Minecraft.fontRendererObj.getStringWidth(p.text) / 2), -(Minecraft.fontRendererObj.FONT_HEIGHT - 1), 52, 0, 9, 9);
                } else {
                    DMGParticle.mc.ingameGUI.drawTexturedModalRect(-(Minecraft.fontRendererObj.getStringWidth(p.text) / 2), -(Minecraft.fontRendererObj.FONT_HEIGHT - 1), 124, 0, 9, 9);
                }
            } else {
                Minecraft.fontRendererObj.drawStringWithShadow(p.text, -(Minecraft.fontRendererObj.getStringWidth(p.text) / 2), -(Minecraft.fontRendererObj.FONT_HEIGHT - 1), 0);
            }
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glDepthMask((boolean)true);
            GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
            GlStateManager.disablePolygonOffset();
            GlStateManager.popMatrix();
        }
    }

    public double roundToPlace(double p_roundToPlace_0_, int p_roundToPlace_2_) {
        if (p_roundToPlace_2_ < 0) {
            throw new IllegalArgumentException();
        }
        return new BigDecimal(p_roundToPlace_0_).setScale(p_roundToPlace_2_, RoundingMode.HALF_UP).doubleValue();
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        try {
            this.particles.forEach(this::lambda$onUpdate$0);
        }
        catch (ConcurrentModificationException concurrentModificationException) {
            // empty catch block
        }
    }

    private void lambda$onUpdate$0(Particles update) {
        ++update.ticks;
        if (update.ticks <= 10) {
            update.location.setY(update.location.getY() + (double)update.ticks * 0.005);
        }
        if (update.ticks > 20) {
            this.particles.remove(update);
        }
    }
}

