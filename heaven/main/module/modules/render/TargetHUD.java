/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ibm.icu.math.BigDecimal
 *  com.ibm.icu.text.NumberFormat
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.module.modules.render;

import com.ibm.icu.math.BigDecimal;
import com.ibm.icu.text.NumberFormat;
import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender2D;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.KillAura;
import heaven.main.module.modules.render.HUD;
import heaven.main.module.modules.render.WingRenderer.ColorUtils;
import heaven.main.ui.font.CFontRenderer;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.ui.simplecore.SimpleRender;
import heaven.main.utils.math.MathUtil;
import heaven.main.utils.render.color.Colors;
import heaven.main.utils.timer.TimerUtils;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TargetHUD
extends Module {
    int animAlpha;
    double anim2;
    double rect;
    double r2;
    float astolfoHelathAnim;
    boolean startAnim;
    boolean stopAnim;
    float animation;
    public EntityLivingBase lastEnt;
    private final CFontRenderer font;
    double anima;
    static EntityLivingBase target;
    private double healthBarWidth;
    private double hudHeight;
    private double healthBarWidth2;
    private static final Mode<String> modes;
    public static final Numbers<Double> xe;
    public static final Numbers<Double> ye;
    private int animWidth;
    float powerxHealthAnim;
    float anim;

    public TargetHUD() {
        super("TargetHUD", new String[]{"targethud"}, ModuleType.Render);
        this.font = Client.instance.FontLoaders.Comfortaa18;
        this.anim = 75.0f;
        this.addValues(modes, xe, ye);
    }

    @EventHandler
    public void onRender(EventRender2D event) {
        if (TargetHUD.mc.currentScreen instanceof GuiChat) {
            target = Minecraft.thePlayer;
        } else {
            EntityLivingBase entityLivingBase = target = KillAura.target != null ? KillAura.target : null;
        }
        if (modes.isCurrentMode("Distance")) {
            this.Flat();
        }
        if (modes.is("hanabi")) {
            this.HanabiFlat();
        }
        if (modes.isCurrentMode("Chocolate")) {
            float scaledWidth = ScaledResolution.getScaledWidth();
            float scaledHeight = ScaledResolution.getScaledHeight();
            CFontRenderer fr = Client.instance.FontLoaders.Comfortaa14;
            if (target != null) {
                if (this.anim > 0.0f) {
                    this.anim -= 5.0f;
                }
                float xOffset = Math.max((float)fr.getStringWidth(target.getName()) + 35.0f, 100.0f);
                float x = scaledWidth / 2.0f + 20.0f;
                float y = scaledHeight / 2.0f + 80.0f + this.anim;
                float health = target.getHealth();
                double hpPercentage = health / target.getMaxHealth();
                hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0, 1.0);
                double hpWidth = (double)(xOffset - 35.0f - 8.0f) * hpPercentage;
                String healthStr = String.valueOf(target.getHealth());
                Gui.drawRect(x, y, x + xOffset, y + 30.0f, new Color(64, 68, 75, 255).getRGB());
                Gui.drawRect(x + 1.0f, y + 1.0f, x + xOffset - 1.0f, y + 30.0f - 1.0f, new Color(30, 30, 30, 255).getRGB());
                Gui.drawRect((double)(x + 32.0f) - 0.2, (double)(y + 15.0f) - 0.2, (double)(x + 32.0f) + hpWidth + 0.2, (double)(y + 16.0f) + 0.2, new Color(64, 68, 75, 255).getRGB());
                RenderUtil.drawGradientSideways(x + 32.0f, y + 15.0f, (double)(x + 32.0f) + hpWidth, y + 16.0f, new Color(255, 120, 255, 255).getRGB(), new Color(120, 120, 255, 255).getRGB());
                fr.drawStringWithShadow("Health : " + healthStr, x + 32.0f, y + 21.0f, -1);
                fr.drawStringWithShadow(target.getName(), x + 32.0f, y + 7.0f, -1);
                ResourceLocation resourceLocation = ((AbstractClientPlayer)target).getLocationSkin();
                mc.getTextureManager().bindTexture(resourceLocation);
                Gui.drawScaledCustomSizeModalRect((int)(x + 6.0f), (int)(y + 6.0f), 8.0f, 8.0f, 8, 8, 18, 18, 64.0f, 64.0f);
            } else {
                this.anim = 100.0f;
            }
        }
        if (modes.is("Zeroday") && target != null) {
            int x = (int)((double)(ScaledResolution.getScaledWidth() / 2) + (Double)xe.getValue());
            int y = (int)((double)ScaledResolution.getScaledHeight() - (Double)ye.getValue());
            int heal = 0;
            int input = 0;
            CFontRenderer font = Client.instance.FontLoaders.Comfortaa16;
            if (TargetHUD.target.hurtResistantTime == 0) {
                heal = (int)target.getHealth();
            }
            if (TargetHUD.target.hurtResistantTime != 0) {
                input = (int)target.getHealth() - heal;
            }
            Gui.drawRect(RenderUtil.width() / 2 + 117 - x, RenderUtil.height() / 2 + 150 - y, RenderUtil.width() / 2 + 280 - x, RenderUtil.height() / 2 + 210 - y, new Color(0, 0, 0, 200).getRGB());
            font.drawStringWithShadow(target.getName(), RenderUtil.width() / 2 + 155 - x, RenderUtil.height() / 2 + 155 - y, -1);
            font.drawStringWithShadow(TargetHUD.target.onGround ? "On Ground | Distance:" + (int)target.getDistanceSqToEntity(Minecraft.thePlayer) + " | Hurt:" + TargetHUD.target.hurtResistantTime : "Off Ground | Distance:" + (int)target.getDistanceSqToEntity(Minecraft.thePlayer) + " | Hurt:" + TargetHUD.target.hurtResistantTime, RenderUtil.width() / 2 + 155 - x, RenderUtil.height() / 2 + 168 - y, -1);
            font.drawStringWithShadow("Damage Output: ", RenderUtil.width() / 2 + 155 - x, RenderUtil.height() / 2 + 177 - y, -1);
            font.drawStringWithShadow("Damage Input: " + input, RenderUtil.width() / 2 + 155 - x, RenderUtil.height() / 2 + 186 - y, -1);
            font.drawStringWithShadow(target.getHealth() > Minecraft.thePlayer.getHealth() ? "You may lose" : (target.getHealth() <= Minecraft.thePlayer.getHealth() ? "You may win" : ""), RenderUtil.width() / 2 + 155 - x, RenderUtil.height() / 2 + 195 - y, -1);
            RenderUtil.drawEntityOnScreen(RenderUtil.width() / 2 + 136 - x, RenderUtil.height() / 2 + 205 - y, 23, 1.0f, 25.0f, target);
            this.anima = AnimationUtil.moveUD((float)this.anima, target.getHealth() * 8.0f, SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
            RenderUtil.drawGradientSideways(RenderUtil.width() / 2 + 117 - x, (double)(RenderUtil.height() / 2) + 208.5 - (double)y, (double)(RenderUtil.width() / 2 + 120 - x) + this.anima, RenderUtil.height() / 2 + 210 - y, new Color(255, 0, 0).getRGB(), new Color(255, 255, 0).getRGB());
        }
        if (modes.isCurrentMode("Lnk")) {
            EntityPlayer ent = (EntityPlayer)target;
            if (target == null) {
                this.animWidth = 0;
                return;
            }
            int modelWidth = 22;
            int height = 38;
            int width = 2 + Math.max(Minecraft.fontRendererObj.getStringWidth(target.getName()) + 2, 85);
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)(ScaledResolution.getScaledWidth() / 2) + 35.0f, (float)(ScaledResolution.getScaledHeight() / 2) + 55.0f, 0.0f);
            RenderUtil.drawRect(0.0, 0.0, (double)(modelWidth + width), (double)height, new Color(0, 0, 0, 150).getRGB());
            GlStateManager.color(1.0f, 1.0f, 1.0f);
            RenderUtil.drawEntityOnScreen(12, 28, 14, ent);
            Minecraft.fontRendererObj.drawStringWithShadow(ent.getName(), modelWidth, 2.0f, -1);
            double healthLocation = (float)(modelWidth + width - 2) * ent.getHealth() / ent.getMaxHealth();
            if ((double)this.animWidth > healthLocation) {
                this.animWidth = MathUtil.getNextPostion(this.animWidth, (int)healthLocation, 2.0);
            }
            if ((double)this.animWidth < healthLocation) {
                this.animWidth = MathUtil.getNextPostion(this.animWidth, (int)healthLocation, 2.0);
            }
            RenderUtil.drawRect(2.0, 32.0, (double)(2 + (modelWidth + width - 2) - 2), 36.0, new Color(0, 0, 0, 35).getRGB());
            RenderUtil.drawRect(2.0, 32.0, (double)(2 + this.animWidth - 2), 36.0, ColorUtils.getHealthColor(ent.getHealth(), ent.getMaxHealth()));
            int armorX = 0;
            int rectX = 0;
            for (int i = 4; i >= 0; --i) {
                Gui.drawRect(modelWidth + rectX, 2 + Minecraft.fontRendererObj.FONT_HEIGHT + 1 - 1, modelWidth + rectX + 16, 2 + Minecraft.fontRendererObj.FONT_HEIGHT + 1 - 1 + 16, new Color(0, 0, 0, 75).getRGB());
                ItemStack itemStack = ent.getEquipmentInSlot(i);
                if (itemStack != null) {
                    RenderUtil.renderItemStack(itemStack, modelWidth + armorX, 2 + Minecraft.fontRendererObj.FONT_HEIGHT + 1 - 1);
                    GlStateManager.pushMatrix();
                    float scale = 0.5f;
                    GlStateManager.scale(scale, scale, scale);
                    Minecraft.fontRendererObj.drawStringWithShadow(String.valueOf(itemStack.getMaxDamage() - itemStack.getItemDamage()), ((float)(modelWidth + armorX) + (16.0f - (float)Minecraft.fontRendererObj.getStringWidth(String.valueOf(itemStack.getMaxDamage() - itemStack.getItemDamage())) * scale) / 2.0f) / scale, ((float)(2 + Minecraft.fontRendererObj.FONT_HEIGHT + 1 - 1 + 22) - (float)Minecraft.fontRendererObj.FONT_HEIGHT * scale - 2.0f) / scale, -1);
                    GlStateManager.popMatrix();
                    armorX += 17;
                }
                rectX += 17;
            }
            GlStateManager.popMatrix();
        }
        if (modes.isCurrentMode("AstolfoNew") && target != null) {
            int colors = new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue(), 255).getRGB();
            int colors1 = new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue(), 150).getRGB();
            int colors2 = new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue(), 50).getRGB();
            float health = TargetHUD.getHealthes(target);
            float n18 = 125.0f * (health / target.getMaxHealth());
            this.animation = AnimationUtil.getAnimationState(this.animation, n18, 135.0f);
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)((double)(ScaledResolution.getScaledWidth() / 2) + (Double)xe.get() + 15.0), (float)((double)ScaledResolution.getScaledHeight() - (Double)ye.get() - 55.0), 0.0f);
            GlStateManager.color(1.0f, 1.0f, 1.0f);
            if (target instanceof EntityPlayer) {
                GuiInventory.drawEntityOnScreen(-18, 47, 30, -180.0f, 0.0f, target);
            } else {
                GuiInventory.drawEntityOnScreen(-20, 50, 30, -180.0f, 0.0f, target);
            }
            RenderUtil.MdrawRect(-38.0, -14.0, 133.0, 52.0, Colors.getColor(0, 0, 0, 180));
            Minecraft.fontRendererObj.drawStringWithShadow(target.getName(), 0.0f, -8.0f, new Color(255, 255, 255).getRGB());
            RenderUtil.MdrawRect(0.0, 48.0, 130.0, 40.0, colors2);
            if ((double)(target.getHealth() / 2.0f + target.getAbsorptionAmount() / 2.0f) > 1.0) {
                RenderUtil.MdrawRect(0.0, 48.0, this.animation + 5.0f, 40.0, colors1);
            }
            RenderUtil.MdrawRect(0.0, 48.0, this.animation, 40.0, colors);
            GlStateManager.scale(3.0f, 3.0f, 3.0f);
            Minecraft.fontRendererObj.drawStringWithShadow(TargetHUD.getHealthes(target) + " \u2764", 0.0f, 2.5f, colors);
            GlStateManager.popMatrix();
        }
        if (modes.isCurrentMode("OldPowerX")) {
            int x = (int)((double)(ScaledResolution.getScaledWidth() / 2) + (Double)xe.getValue());
            int y = (int)((double)ScaledResolution.getScaledHeight() - (Double)ye.getValue());
            EntityLivingBase player = target;
            if (player != null) {
                float f = 0.0f;
                GlStateManager.pushMatrix();
                float xLeng = 144.0f;
                float yLeng = 52.0f;
                RenderUtil.rectangleBordered(x, y, (float)x + xLeng, (float)y + yLeng, 0.5, Colors.getColor(90), Colors.getColor(0));
                RenderUtil.rectangleBordered((float)x + 1.0f, (float)y + 1.0f, (float)x + xLeng - 1.0f, (float)y + yLeng - 1.0f, 1.0, Colors.getColor(90), Colors.getColor(61));
                RenderUtil.rectangleBordered((double)x + 2.5, (double)y + 2.5, (double)((float)x + xLeng) - 2.5, (double)((float)y + yLeng) - 2.5, 0.5, Colors.getColor(61), Colors.getColor(0));
                RenderUtil.rectangleBordered((float)x + 3.0f, (float)y + 3.0f, (float)x + xLeng - 3.0f, (float)y + yLeng - 3.0f, 0.5, Colors.getColor(27), Colors.getColor(61));
                Minecraft.fontRendererObj.drawStringWithShadow(player.getName(), x + 40, y + 6, -1);
                if (player instanceof EntityPlayer) {
                    GlStateManager.pushMatrix();
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    GlStateManager.enableAlpha();
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                    List var5 = GuiPlayerTabOverlay.field_175252_a.sortedCopy(Minecraft.thePlayer.sendQueue.getPlayerInfoMap());
                    for (NetworkPlayerInfo aVar5 : var5) {
                        if (Minecraft.theWorld.getPlayerEntityByUUID(aVar5.getGameProfile().getId()) != player) continue;
                        float size = 30.0f;
                        mc.getTextureManager().bindTexture(aVar5.getLocationSkin());
                        RenderUtil.drawScaledCustomSizeModalRect(x + 6, y + 6, 8.0f, 8.0f, 8, 8, size, size, 64.0f, 64.0f);
                        if (((EntityPlayer)player).isWearing(EnumPlayerModelParts.HAT)) {
                            RenderUtil.drawScaledCustomSizeModalRect(x + 6, y + 6, 40.0f, 8.0f, 8, 8, size, size, 64.0f, 64.0f);
                        }
                        GlStateManager.bindTexture(0);
                        break;
                    }
                    GlStateManager.popMatrix();
                }
                if (player instanceof EntityPlayer) {
                    EntityPlayer entityPlayer = (EntityPlayer)target;
                    GlStateManager.pushMatrix();
                    ArrayList<Object> stuff = new ArrayList<Object>();
                    int split = x + 19;
                    int armorY = y + 16;
                    for (int index = 3; index >= 0; --index) {
                        ItemStack armer = entityPlayer.inventory.armorInventory[index];
                        if (armer == null) continue;
                        stuff.add(armer);
                    }
                    if (entityPlayer.getCurrentEquippedItem() != null) {
                        stuff.add(entityPlayer.getCurrentEquippedItem());
                    }
                    for (ItemStack itemStack : stuff) {
                        if (Minecraft.theWorld != null) {
                            RenderHelper.enableGUIStandardItemLighting();
                            split += 20;
                        }
                        RenderUtil.rectangleBordered(split, armorY, (double)split + 18.0, (double)armorY + 18.0, 1.0, new Color(52, 52, 52, 150).getRGB(), Colors.BLACK.c);
                        GlStateManager.disableAlpha();
                        GlStateManager.clear(256);
                        TargetHUD.mc.getRenderItem().zLevel = -150.0f;
                        mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, split + 1, armorY + 1);
                        mc.getRenderItem().renderItemOverlays(Minecraft.fontRendererObj, itemStack, split + 1, armorY + 1);
                        TargetHUD.mc.getRenderItem().zLevel = 0.0f;
                        GlStateManager.disableBlend();
                        GlStateManager.scale(0.5, 0.5, 0.5);
                        GlStateManager.disableDepth();
                        GlStateManager.disableLighting();
                        GlStateManager.enableDepth();
                        GlStateManager.scale(2.0f, 2.0f, 2.0f);
                        GlStateManager.enableAlpha();
                    }
                    GlStateManager.popMatrix();
                }
                BigDecimal bigDecimal = new BigDecimal((double)player.getHealth());
                float health = bigDecimal.setScale(1, 4).floatValue();
                float[] fractions = new float[]{0.0f, 0.2f, 0.7f};
                Color[] colors = new Color[]{Color.RED, Color.YELLOW, Color.GREEN};
                float progress = health / player.getMaxHealth();
                Color customColor = health >= 0.0f ? this.blendColors(fractions, colors, progress).brighter() : Color.RED;
                double d = 98.0;
                float health2 = player.getHealth() / player.getMaxHealth();
                if ((double)this.powerxHealthAnim < d * (double)f) {
                    if (d * (double)health2 - (double)this.powerxHealthAnim < 1.0) {
                        this.powerxHealthAnim = (float)(d * (double)health2);
                    }
                    this.powerxHealthAnim = (float)((double)this.powerxHealthAnim + 2.0);
                }
                if (d * (double)health2 - (double)this.powerxHealthAnim > 1.0) {
                    this.powerxHealthAnim = (float)(d * (double)health2);
                }
                this.powerxHealthAnim = (float)((double)this.powerxHealthAnim - 2.0);
                if (this.powerxHealthAnim < 0.0f) {
                    this.powerxHealthAnim = 0.0f;
                }
                RenderUtil.rectangleBordered(x + 39, y + Minecraft.fontRendererObj.FONT_HEIGHT + 26, x + 137, y + Minecraft.fontRendererObj.FONT_HEIGHT + 38, 1.0, 0, Colors.BLACK.c);
                Gui.drawRect((double)(x + 40), (double)(y + Minecraft.fontRendererObj.FONT_HEIGHT + 27), (float)(x + 40) + this.powerxHealthAnim, (double)(y + Minecraft.fontRendererObj.FONT_HEIGHT + 37), customColor.darker().getRGB());
                Minecraft.fontRendererObj.drawStringWithShadow((Object)((Object)EnumChatFormatting.RED) + "\u2764" + (Object)((Object)EnumChatFormatting.RESET) + health, (float)x + 7.5f, (float)y + 52.0f - (float)Minecraft.fontRendererObj.FONT_HEIGHT - 5.0f, customColor.darker().getRGB());
                GlStateManager.popMatrix();
            }
        }
        if (modes.isCurrentMode("Exhibition") && target != null) {
            int hudX = ScaledResolution.getScaledWidth() / 2 + 30;
            int hudY = ScaledResolution.getScaledHeight() / 2 + 30;
            RenderUtil.drawRect(hudX, hudY + 20, hudX + 110, hudY + 60, Color.BLACK.getRGB());
            RenderUtil.drawRect(hudX + 1, hudY + 20 + 1, hudX + 110 - 1, hudY + 60 - 1, new Color(55, 55, 55).getRGB());
            RenderUtil.drawRect((double)(hudX + 1), (double)hudY + 21.5, (double)(hudX + 108), (double)hudY + 58.5, new Color(30, 30, 30).getRGB());
            RenderUtil.drawRect(hudX + 3, hudY + 20 + 3, hudX + 110 - 3, hudY + 60 - 3, new Color(14, 14, 14).getRGB());
            int x4 = hudX + 3;
            int y4 = hudY + 20 + 3;
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GuiInventory.drawEntityOnScreen(x4 + 15, y4 + 33, 15, TargetHUD.target.rotationYaw, TargetHUD.target.rotationPitch, target);
            GlStateManager.resetColor();
            Client.instance.FontLoaders.Comfortaa14.drawStringWithShadow("\ufffd\ufffdl" + target.getName(), (x4 += 30) + 1, y4 + 5, -1);
            RenderUtil.drawBorderedRect(x4, y4 + 12, x4 + 55, y4 + 16, 0.5f, new Color(40, 40, 40).getRGB(), Color.BLACK.getRGB());
            double hpWidth3 = 55.0 * MathHelper.clamp_double(target.getHealth() / target.getMaxHealth(), 0.0, 1.0);
            RenderUtil.drawRect((double)((float)x4 + 0.5f), (double)((float)(y4 + 11) + 0.5f), (double)x4 + hpWidth3 - 0.5, (double)((float)(y4 + 15) - 0.5f), ColorUtils.getBlendColor(target.getHealth(), target.getMaxHealth()).getRGB());
            int xOff = 6;
            for (int k = 0; k < 8; ++k) {
                RenderUtil.drawRect(x4 + xOff, y4 + 11, (float)(x4 + xOff) + 0.5f, y4 + 15, Color.BLACK.getRGB());
                xOff += 6;
            }
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5, 0.5, 0.5);
            FontRenderer fontRendererObj4 = Minecraft.fontRendererObj;
            StringBuilder append = new StringBuilder("HP:").append((int)target.getHealth()).append(" | Dist:");
            fontRendererObj4.drawStringWithShadow(append.append((int)Minecraft.thePlayer.getDistanceToEntity(target)).toString(), x4 + 1 << 1, y4 + 17 << 1, -1);
            GlStateManager.popMatrix();
            if (target instanceof EntityPlayer) {
                EntityPlayer player3 = (EntityPlayer)target;
                int xOffset3 = 3;
                for (int slot = 3; slot >= 0; --slot) {
                    ItemStack itemStack = player3.inventory.armorItemInSlot(slot);
                    if (itemStack == null) continue;
                    mc.getRenderItem().renderItemIntoGUI(itemStack, x4 - xOffset3, y4 + 20);
                    xOffset3 -= 15;
                }
                ItemStack stack2 = player3.inventory.getCurrentItem();
                if (stack2 != null) {
                    RenderHelper.enableGUIStandardItemLighting();
                    mc.getRenderItem().renderItemIntoGUI(stack2, x4 - xOffset3, y4 + 18);
                    RenderHelper.disableStandardItemLighting();
                }
            }
        }
        if (modes.isCurrentMode("NewPowerX")) {
            double d;
            int x = (int)((double)(ScaledResolution.getScaledWidth() / 2) + (Double)xe.getValue());
            int y = (int)((double)ScaledResolution.getScaledHeight() - (Double)ye.getValue());
            if (!(target instanceof EntityPlayer)) {
                this.animWidth = 0;
                this.r2 = 0.0;
                return;
            }
            int modelWidth = 22;
            int height = 30;
            int width = 2 + Math.max(Minecraft.fontRendererObj.getStringWidth(target.getName()) + 2, 98);
            String healthStr = String.valueOf((float)((int)target.getHealth()) / 2.0f);
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, 0.0f);
            RenderUtil.drawRect(0.0, 0.0, (double)(modelWidth + width), (double)height, Colors.getColor(0, 155));
            GlStateManager.color(1.0f, 1.0f, 1.0f);
            String name = target.getDisplayName().getFormattedText();
            Client.instance.FontLoaders.Comfortaa18.drawStringWithShadow(name, modelWidth + 11, 5.0, -1);
            Client.instance.FontLoaders.Comfortaa12.drawStringWithShadow(healthStr, modelWidth + 81, 23.6, new Color(150, 150, 150).getRGB());
            Client.instance.FontLoaders.Logo10.drawStringWithShadow("s", modelWidth + 74, 25.0, new Color(87, 160, 250).getRGB());
            Client.instance.FontLoaders.Logo10.drawStringWithShadow("s", modelWidth + 11, 18.5, new Color(255, 85, 85).getRGB());
            Client.instance.FontLoaders.Logo10.drawStringWithShadow("r", modelWidth + 11, 25.5, new Color(153, 153, 153).getRGB());
            double healthLocation = (float)(modelWidth + width - 2) * target.getHealth() / target.getMaxHealth();
            if ((double)this.animWidth > healthLocation) {
                this.animWidth = MathUtil.getNextPostion(this.animWidth, (int)healthLocation, 2.0);
            }
            if ((double)this.animWidth < healthLocation) {
                this.animWidth = MathUtil.getNextPostion(this.animWidth, (int)healthLocation, 2.0);
            }
            double armorlecotion = (modelWidth + width - 2) * target.getTotalArmorValue();
            if (this.r2 > d) {
                this.r2 = MathUtil.getNextPostion((int)this.r2, (int)armorlecotion, 2.0);
            }
            if (this.r2 < armorlecotion) {
                this.r2 = MathUtil.getNextPostion((int)this.r2, (int)armorlecotion, 2.0);
            }
            RenderUtil.drawRect((double)(modelWidth + 18), 17.0, (double)(modelWidth + 18) + (double)(modelWidth + width - 2) / 1.6, 19.0, new Color(0, 0, 0, 180).getRGB());
            RenderUtil.drawRect((double)(modelWidth + 18), 17.0, (double)(modelWidth + 18) + (double)this.animWidth / 1.6, 19.0, ColorUtils.getHealthColor(target.getHealth(), target.getMaxHealth()));
            RenderUtil.drawRect((double)(modelWidth + 18), 24.0, (double)(modelWidth + 18) + (double)(modelWidth + width - 2) / 2.4, 26.0, new Color(0, 0, 0, 180).getRGB());
            RenderUtil.drawRect((double)(modelWidth + 18), 24.0, (double)(modelWidth + 18) + this.r2 / 48.0, 26.0, new Color(87, 160, 250).getRGB());
            TargetHUD.drawNormalFace(target, 1.0f, 1.0f, 28.0f);
            GlStateManager.popMatrix();
        }
        if (modes.isCurrentMode("Zenith")) {
            int x = (int)((double)(ScaledResolution.getScaledWidth() / 2) + (Double)xe.getValue());
            int y = (int)((double)ScaledResolution.getScaledHeight() - (Double)ye.getValue());
            if (target != null) {
                if (Minecraft.thePlayer.getDistanceToEntity(target) < 8.0f) {
                    GlStateManager.pushMatrix();
                    float xLeng = 100.0f;
                    float yLeng = 50.0f;
                    RenderUtil.rectangleBordered(x, y, (float)x + 100.0f, (float)y + 50.0f, 0.8, Colors.getColor(255), Colors.getColor(25));
                    RenderUtil.rectangleBordered(x + 1, y + 1, (float)x + 100.0f - 1.0f, (float)y + 50.0f - 1.0f, 1.0, Colors.getColor(35), Colors.getColor(130));
                    RenderUtil.rectangleBordered((float)x + 1.5f, (float)y + 1.5f, (float)x + 100.0f - 1.5f, (float)y + 50.0f - 1.5f, 1.1, Colors.getColor(35), Colors.getColor(90));
                    String name = target.getDisplayName().getFormattedText();
                    Client.instance.FontLoaders.Comfortaa20.drawStringWithShadow(name, (double)x + 8.5, y + 10, 0xFFFFFF);
                    double hpWidth = 70.0 * MathHelper.clamp_double(target.getHealth() / target.getMaxHealth(), 0.0, 1.0);
                    RenderUtil.drawRect((double)((float)x + 8.5f), (double)(y + 31), (double)x + hpWidth + 8.5, (double)((float)(y + 21) - 0.5f), this.getHealthColor(target.getHealth(), target.getMaxHealth()).getRGB());
                    CFontRenderer cfr_ignored_0 = Client.instance.FontLoaders.Comfortaa20;
                    Client.instance.FontLoaders.Comfortaa20.drawStringWithShadow((int)target.getHealth() + " HP", (float)x + 24.5f, (float)(y + 56 - 0) - 22.2f - 11.5f, -1);
                    if (target instanceof EntityPlayer) {
                        CFontRenderer cfr_ignored_1 = Client.instance.FontLoaders.Comfortaa15;
                        Client.instance.FontLoaders.Comfortaa15.drawStringWithShadow("Blocking: " + ((EntityPlayer)target).isBlocking(), (float)x + 8.5f, (float)(y + 65 - 0) - 22.2f - 5.0f, -1);
                    }
                    GlStateManager.popMatrix();
                }
            }
        }
        if (modes.isCurrentMode("Lune")) {
            int targetx = (int)((double)(ScaledResolution.getScaledWidth() / 2) + (Double)xe.getValue());
            int targety = (int)((double)ScaledResolution.getScaledHeight() - (Double)ye.getValue());
            float anim = 150.0f;
            if (target != null) {
                RenderUtil.drawRect(targetx + 35, targety + 40, targetx + 195, targety + 72, new Color(33, 36, 41, 255).getRGB());
                Client.instance.FontLoaders.Comfortaa18.drawCenteredStringWithShadow(target.getName(), targetx + 40 + 75, targety + 45, -1);
                if (anim < 150.0f * (target.getHealth() / target.getMaxHealth())) {
                    anim = (int)(150.0f * (target.getHealth() / target.getMaxHealth()));
                } else if (anim > 150.0f * (target.getHealth() / target.getMaxHealth())) {
                    anim -= 120.0f / (float)Minecraft.debugFPS;
                }
                RenderUtil.drawRect(targetx + 40, targety + 60, targetx + 40 + 150, targety + 65, new Color(TargetHUD.target.hurtTime * 20, 0, 0).getRGB());
                RenderUtil.drawRect((float)(targetx + 40) + 150.0f * (target.getHealth() / target.getMaxHealth()), targety + 60, (float)(targetx + 40) + anim, targety + 65, new Color(255, 100, 152).getRGB());
                RenderUtil.drawGradientSideways(targetx + 40, targety + 60, (float)(targetx + 40) + 150.0f * (target.getHealth() / target.getMaxHealth()), targety + 65, new Color(73, 148, 248).getRGB(), new Color(73, 200, 248).getRGB());
            }
        }
        if (modes.isCurrentMode("GodLike") && target != null) {
            float x = (int)((double)(ScaledResolution.getScaledWidth() / 2) + (Double)xe.getValue());
            float y = (int)((double)ScaledResolution.getScaledHeight() - (Double)ye.getValue());
            float health = target.getHealth();
            float hpPercentage = TargetHUD.target.isDead ? 0.0f : health / target.getMaxHealth();
            float hpWidth = 176.0f * hpPercentage;
            int healthColor = new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()).getRGB();
            if (TimerUtils.hasReached(15L)) {
                this.healthBarWidth = AnimationUtil.animate((double)hpWidth, this.healthBarWidth, (double)SimpleRender.processFPS(1000.0f, 0.013f));
                TimerUtils.reset();
                TimerUtils.reset();
            }
            RenderUtil.drawGradientSideways(x + 40.0f, y - 23.0f, x + 216.0f, y + 27.0f, new Color(0, 0, 0, 180).getRGB(), new Color(0, 0, 0, 180).getRGB());
            RenderUtil.drawRect(0.0f, 0.0f, 0.0f, 0.0f, 0);
            RenderUtil.drawGradientSideways(x + 40.0f, y + 26.0f, (double)(x + 40.0f) + this.healthBarWidth, y + 27.0f, healthColor, healthColor);
            int fontX = -44;
            Client.instance.FontLoaders.Comfortaa22.drawStringWithShadow(String.format("%.1f", Float.valueOf(hpPercentage * 20.0f)) + " HP", x + 112.0f + -44.0f - (float)(Client.instance.FontLoaders.Comfortaa22.getStringWidth(String.format("%.1f", Float.valueOf(hpPercentage * 20.0f)) + " HP") / 2), y + 6.0f, healthColor);
            if (target instanceof EntityPlayer) {
                Client.instance.FontLoaders.Comfortaa18.drawString(target.getName(), x + 47.0f, y - 12.0f, -1);
            } else {
                Client.instance.FontLoaders.Comfortaa18.drawString("Entity", x + 47.0f, y - 12.0f, -1);
            }
        }
        if (modes.isCurrentMode("Novoline2") && target != null) {
            RenderUtil.renderTHUD((EntityPlayer)target);
        }
        if (modes.isCurrentMode("Novoline") && target != null) {
            float healthPercentage = target.getHealth() / target.getMaxHealth();
            float startX = 20.0f;
            float renderX = (float)(ScaledResolution.getScaledWidth() / 2) + startX;
            float renderY = ScaledResolution.getScaledHeight() / 2 + 10;
            int maxX2 = 30;
            if (target instanceof EntityOtherPlayerMP) {
                if (target.getCurrentArmor(3) != null) {
                    maxX2 += 15;
                }
                if (target.getCurrentArmor(2) != null) {
                    maxX2 += 15;
                }
                if (target.getCurrentArmor(1) != null) {
                    maxX2 += 15;
                }
                if (target.getCurrentArmor(0) != null) {
                    maxX2 += 15;
                }
                if (target.getHeldItem() != null) {
                    maxX2 += 15;
                }
            }
            int healthColor = this.getHealthColor(target.getHealth(), target.getMaxHealth()).getRGB();
            float maxX = Math.max(maxX2, Minecraft.fontRendererObj.getStringWidth(target.getName()) + 30);
            Gui.drawRect(renderX, renderY, renderX + maxX, renderY + 40.0f, new Color(0.0f, 0.0f, 0.0f, 0.3f).getRGB());
            Gui.drawRect(renderX, renderY + 38.0f, renderX + maxX * healthPercentage, renderY + 40.0f, healthColor);
            Minecraft.fontRendererObj.drawStringWithShadow(target.getName(), renderX + 25.0f, renderY + 7.0f, -1);
            int xAdd = 0;
            double multiplier = 0.85;
            GlStateManager.pushMatrix();
            GlStateManager.scale(multiplier, multiplier, multiplier);
            if (target instanceof EntityOtherPlayerMP) {
                if (target.getCurrentArmor(3) != null) {
                    mc.getRenderItem().renderItemAndEffectIntoGUI(target.getCurrentArmor(3), (int)((double)((float)(ScaledResolution.getScaledWidth() / 2) + startX + 23.0f + (float)xAdd) / multiplier), (int)((double)(ScaledResolution.getScaledHeight() / 2 + 28) / multiplier));
                    xAdd += 15;
                }
                if (target.getCurrentArmor(2) != null) {
                    mc.getRenderItem().renderItemAndEffectIntoGUI(target.getCurrentArmor(2), (int)((double)((float)(ScaledResolution.getScaledWidth() / 2) + startX + 23.0f + (float)xAdd) / multiplier), (int)((double)(ScaledResolution.getScaledHeight() / 2 + 28) / multiplier));
                    xAdd += 15;
                }
                if (target.getCurrentArmor(1) != null) {
                    mc.getRenderItem().renderItemAndEffectIntoGUI(target.getCurrentArmor(1), (int)((double)((float)(ScaledResolution.getScaledWidth() / 2) + startX + 23.0f + (float)xAdd) / multiplier), (int)((double)(ScaledResolution.getScaledHeight() / 2 + 28) / multiplier));
                    xAdd += 15;
                }
                if (target.getCurrentArmor(0) != null) {
                    mc.getRenderItem().renderItemAndEffectIntoGUI(target.getCurrentArmor(0), (int)((double)((float)(ScaledResolution.getScaledWidth() / 2) + startX + 23.0f + (float)xAdd) / multiplier), (int)((double)(ScaledResolution.getScaledHeight() / 2 + 28) / multiplier));
                    xAdd += 15;
                }
                if (target.getHeldItem() != null) {
                    mc.getRenderItem().renderItemAndEffectIntoGUI(target.getHeldItem(), (int)((double)((float)(ScaledResolution.getScaledWidth() / 2) + startX + 23.0f + (float)xAdd) / multiplier), (int)((double)(ScaledResolution.getScaledHeight() / 2 + 28) / multiplier));
                }
            }
            GlStateManager.popMatrix();
            GuiInventory.drawEntityOnScreen((int)renderX + 12, (int)renderY + 33, 15, TargetHUD.target.rotationYaw, TargetHUD.target.rotationPitch, target);
        }
        if (modes.isCurrentMode("Flux")) {
            int x = ScaledResolution.getScaledWidth() / 2 + 30;
            int y = ScaledResolution.getScaledHeight() / 2 - 5;
            if (target != null) {
                if (!(target instanceof EntityPlayer)) {
                    return;
                }
                this.Background(target);
                this.onName(target);
                TargetHUD.EvelinaonHead();
                RenderUtil.drawRect((double)x - 0.3, (double)(y - 10), (double)(x + 20) + 0.3, (double)y - 9.3, new Color(149, 255, 147).getRGB());
                RenderUtil.drawRect((double)x - 0.3, (double)(y - 10), (double)x, (double)(y + 10), new Color(149, 255, 147).getRGB());
                RenderUtil.drawRect((double)x - 0.3, (double)(y + 10), (double)(x + 20) + 0.3, (double)y + 10.3, new Color(149, 255, 147).getRGB());
                RenderUtil.drawRect((double)(x + 20), (double)(y - 10), (double)(x + 20) + 0.6, (double)(y + 10), new Color(149, 255, 147).getRGB());
                EntityLivingBase target1 = target;
                if (target1 != this.lastEnt && target1 != null) {
                    this.lastEnt = target1;
                }
                if (this.startAnim) {
                    this.stopAnim = false;
                }
                if (this.animAlpha == 255 && target == null) {
                    this.stopAnim = true;
                }
                boolean bl = this.startAnim = target != null;
                if (this.startAnim && this.animAlpha < 255) {
                    this.animAlpha += 15;
                }
                if (this.stopAnim && this.animAlpha > 0) {
                    this.animAlpha -= 15;
                }
                if (target == null && this.animAlpha < 255) {
                    this.stopAnim = true;
                }
                EntityLivingBase player = null;
                if (this.lastEnt != null) {
                    player = this.lastEnt;
                }
                if (player != null && this.animAlpha >= 135) {
                    assert (target != null);
                    double healthLocation = target.getHealth() > 20.0f ? 16.0 + this.rect : (16.0 + this.rect) / 20.0 * (double)((int)target.getHealth());
                    if (this.anim2 < healthLocation) {
                        this.anim2 = healthLocation;
                    }
                    if (this.anim2 > healthLocation) {
                        this.anim2 -= 0.5;
                    }
                    this.r2 = (16.0 + this.rect) / 20.0 * (double)target.getTotalArmorValue();
                    Gui.drawRect((double)(x + 7), (double)(y + 13), (double)(x + 7) + this.anim2, (double)(y + 15), new Color(255, 213, 0, 201).getRGB());
                    Gui.drawRect((double)(x + 7), (double)(y + 13), (double)(x + 7) + healthLocation, (double)(y + 15), new Color(47, 190, 130).getRGB());
                    Gui.drawRect((double)(x + 7), (double)(y + 18), (double)(x + 23) + this.rect, (double)(y + 20), new Color(50, 50, 50).getRGB());
                    Gui.drawRect((double)(x + 7), (double)(y + 18), (double)(x + 7) + this.r2, (double)(y + 20), new Color(87, 130, 189).getRGB());
                }
            }
        }
    }

    @Override
    public void onEnable() {
        this.animAlpha = 0;
        this.startAnim = false;
        this.stopAnim = false;
        this.astolfoHelathAnim = 0.0f;
    }

    private void Flat() {
        int blackcolor = new Color(0, 0, 0, 180).getRGB();
        int blackcolor2 = new Color(200, 200, 200).getRGB();
        if (target != null) {
            int x = (int)((double)(ScaledResolution.getScaledWidth() / 2) + (Double)xe.getValue());
            int y = (int)((double)ScaledResolution.getScaledHeight() - (Double)ye.getValue());
            float health = target.getHealth();
            double hpPercentage = health / target.getMaxHealth();
            Color hurt = Color.getHSBColor(1.0f, TargetHUD.target.hurtTime / 10, 1.0f);
            hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0, 1.0);
            double hpWidth = 92.0 * hpPercentage;
            int healthColor = this.getHealthColor(target.getHealth(), target.getMaxHealth()).getRGB();
            String healthStr = String.valueOf((int)target.getHealth() / 2);
            if (TimerUtils.hasReached(15L)) {
                this.healthBarWidth2 = AnimationUtil.animate(hpWidth, this.healthBarWidth2, (double)SimpleRender.processFPS(1000.0f, 0.003f));
                this.healthBarWidth = AnimationUtil.animate(hpWidth, this.healthBarWidth, (double)SimpleRender.processFPS(1000.0f, 0.013f));
                this.hudHeight = AnimationUtil.animate(50.0, this.hudHeight, (double)SimpleRender.processFPS(1000.0f, 0.01f));
                TimerUtils.reset();
            }
            GL11.glEnable((int)3089);
            RenderUtil.prepareScissorBox(x, y, x + 140, (float)((double)y + this.hudHeight));
            Gui.drawRect(x, y, x + 140, y + 40, blackcolor);
            RenderUtil.drawRect(x + 40, y + 15, x + 40 + (int)this.healthBarWidth2, y + 25, Color.YELLOW.getRGB());
            RenderUtil.drawGradientSideways(x + 40, y + 15, (double)(x + 40) + this.healthBarWidth, y + 25, Colors.RED.c, healthColor);
            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(healthStr, (float)(x + 40 + 46 - Client.instance.FontLoaders.Comfortaa16.getStringWidth(healthStr) / 2) + (float)Minecraft.fontRendererObj.getStringWidth("\u2764") / 1.9f, y + 18, blackcolor2);
            Minecraft.fontRendererObj.drawStringWithShadow("\u2764", (float)(x + 40 + 46 - Client.instance.FontLoaders.Comfortaa16.getStringWidth(healthStr) / 2) - (float)Minecraft.fontRendererObj.getStringWidth("\u2764") / 1.9f, (float)y + 15.5f, hurt.getRGB());
            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(target.getName(), x + 40, y + 5, blackcolor2);
            Client.instance.FontLoaders.Comfortaa13.drawStringWithShadow("XYZ:" + (int)TargetHUD.target.posX + " " + (int)TargetHUD.target.posY + " " + (int)TargetHUD.target.posZ + "   |   Hurt:" + (TargetHUD.target.hurtTime > 0), x + 41, y + 32, blackcolor2);
            GuiInventory.drawEntityOnScreen((int)((float)x + 13.333333f), y + 40, 20, TargetHUD.target.rotationYaw, TargetHUD.target.rotationPitch, target);
            GL11.glDisable((int)3089);
        } else {
            this.healthBarWidth2 = 92.0;
            this.healthBarWidth = 92.0;
            this.hudHeight = 0.0;
        }
    }

    public Color blendColors(float[] fractions, Color[] colors, float progress) {
        if (fractions == null) {
            throw new IllegalArgumentException("Fractions can't be null");
        }
        if (colors == null) {
            throw new IllegalArgumentException("Colours can't be null");
        }
        if (fractions.length == colors.length) {
            int[] indicies = this.getFractionIndicies(fractions, progress);
            float[] range = new float[]{fractions[indicies[0]], fractions[indicies[1]]};
            Color[] colorRange = new Color[]{colors[indicies[0]], colors[indicies[1]]};
            float max = range[1] - range[0];
            float value = progress - range[0];
            float weight = value / max;
            Color color = this.blend(colorRange[0], colorRange[1], 1.0f - weight);
            return color;
        }
        throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
    }

    public Color getHealthColor(float health, float maxHealth) {
        float[] fractions = new float[]{0.0f, 0.5f, 1.0f};
        Color[] colors = new Color[]{new Color(108, 20, 20), new Color(255, 0, 60), Color.GREEN};
        float progress = health / maxHealth;
        return this.blendColors(fractions, colors, progress).brighter();
    }

    public Color getHealthColor2(float f, float f2) {
        float[] fArray = new float[]{0.0f, 0.0f, 1.0f};
        Color[] colorArray = new Color[]{new Color(0, 81, 179), new Color(0, 153, 255), new Color(47, 154, 241)};
        float f3 = f / f2;
        return this.blendColors(fArray, colorArray, f3).brighter();
    }

    private void HanabiFlat() {
        int n;
        String string;
        Color color;
        double d;
        int n2 = new Color(0, 0, 0, 180).getRGB();
        int n3 = new Color(200, 200, 200).getRGB();
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        float f = scaledResolution.getScaledWidth();
        float f2 = scaledResolution.getScaledHeight();
        CFontRenderer font1 = Client.instance.FontLoaders.Comfortaa16;
        float f3 = f / 2.0f - 50.0f;
        float f4 = f2 / 2.0f + 32.0f;
        if (target == null) {
            float f5 = 0.0f;
            d = f5 / 20.0f;
            color = Color.getHSBColor(0.0f, 0.0f, 1.0f);
            string = String.valueOf(0.0f);
            n = this.getHealthColor2(0.0f, 20.0f).getRGB();
        } else {
            float f6 = target.getHealth();
            d = f6 / target.getMaxHealth();
            color = Color.getHSBColor(0.0f, (float)TargetHUD.target.hurtTime / 10.0f, 1.0f);
            string = String.valueOf((float)((int)target.getHealth()) / 2.0f);
            n = this.getHealthColor2(target.getHealth(), target.getMaxHealth()).getRGB();
        }
        d = MathHelper.clamp_double(d, 0.0, 1.0);
        double d2 = 140.0 * d;
        if (target == null) {
            this.healthBarWidth2 = AnimationUtil.animate(0.0, this.healthBarWidth2, (double)SimpleRender.processFPS(1000.0f, 0.007f));
            this.healthBarWidth = AnimationUtil.animate(0.0, this.healthBarWidth, (double)SimpleRender.processFPS(1000.0f, 0.013f));
            this.hudHeight = AnimationUtil.animate(0.0, this.hudHeight, (double)SimpleRender.processFPS(1000.0f, 0.013f));
        } else {
            this.healthBarWidth2 = AnimationUtil.moveUD((float)this.healthBarWidth2, (float)d2, SimpleRender.processFPS(1000.0f, 0.0045f), SimpleRender.processFPS(1000.0f, 0.004f));
            this.healthBarWidth = AnimationUtil.animate(d2, this.healthBarWidth, (double)SimpleRender.processFPS(1000.0f, 0.013f));
            this.hudHeight = AnimationUtil.animate(40.0, this.hudHeight, (double)SimpleRender.processFPS(1000.0f, 0.013f));
        }
        if (this.hudHeight == 0.0) {
            this.healthBarWidth2 = 140.0;
            this.healthBarWidth = 140.0;
        }
        GL11.glEnable((int)3089);
        RenderUtil.prepareScissorBox(f3, (float)((double)f4 + 40.0 - this.hudHeight), f3 + 140.0f, (float)((double)f4 + 40.0));
        RenderUtil.drawRect(f3, f4, f3 + 140.0f, f4 + 40.0f, n2);
        RenderUtil.drawRect(f3, f4 + 37.0f, f3 + 140.0f, f4 + 40.0f, new Color(0, 0, 0, 49).getRGB());
        RenderUtil.drawRect(f3, f4 + 37.0f, (float)((double)f3 + this.healthBarWidth2), f4 + 40.0f, new Color(255, 0, 213, 220).getRGB());
        RenderUtil.drawGradientSideways(f3, f4 + 37.0f, (double)f3 + this.healthBarWidth, f4 + 40.0f, new Color(0, 81, 179).getRGB(), n);
        RenderUtil.drawRect(0.0f, 0.0f, 0.0f, 0.0f, 0);
        font1.drawStringWithShadow(string, f3 - 5.0f + 40.0f + 85.0f - (float)font1.getStringWidth(string) / 2.0f + (float)Minecraft.fontRendererObj.getStringWidth("\u2764"), (double)(f4 + 26.0f + 2.0f), (double)n3, 160);
        Minecraft.fontRendererObj.drawStringWithShadow("\u2764", f3 + 3.0f + 40.0f + 85.0f - (float)font1.getStringWidth(string) / 2.0f - (float)Minecraft.fontRendererObj.getStringWidth("\u2764"), f4 + 26.5f - 0.5f, color.getRGB());
        CFontRenderer class322 = Client.instance.FontLoaders.Comfortaa13;
        if (target == null) {
            class322.drawStringWithShadow("XYZ:0 0 0   |   Hurt:false", f3 + 41.0f, (double)(f4 + 15.0f), (double)n3, 160);
            font1.drawStringWithShadow("(\u65e0\u76ee\u6807)", f3 + 40.0f, (double)(f4 + 5.0f), (double)n3, 160);
        } else {
            class322.drawStringWithShadow("XYZ:" + (int)TargetHUD.target.posX + " " + (int)TargetHUD.target.posY + " " + (int)TargetHUD.target.posZ + "   |   Hurt:" + (TargetHUD.target.hurtTime > 0), f3 + 41.0f, (double)(f4 + 15.0f), (double)n3, 160);
            font1.drawStringWithShadow(target.getName(), f3 + 40.0f, (double)(f4 + 4.0f), (double)n3, 160);
            if (target instanceof EntityPlayer) {
                mc.getTextureManager().bindTexture(((AbstractClientPlayer)target).getLocationSkin());
                Gui.drawScaledCustomSizeModalRect((int)f3 + 3, (int)f4 + 3, 8.0f, 8.0f, 8, 8, 32, 32, 64.0f, 64.0f);
            }
        }
        GL11.glDisable((int)3089);
    }

    public Color blend(Color color1, Color color2, double ratio) {
        float r = (float)ratio;
        float ir = 1.0f - r;
        float[] rgb1 = new float[3];
        float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        float red = rgb1[0] * r + rgb2[0] * ir;
        float green = rgb1[1] * r + rgb2[1] * ir;
        float blue = rgb1[2] * r + rgb2[2] * ir;
        if (red < 0.0f) {
            red = 0.0f;
        } else if (red > 255.0f) {
            red = 255.0f;
        }
        if (green < 0.0f) {
            green = 0.0f;
        } else if (green > 255.0f) {
            green = 255.0f;
        }
        if (blue < 0.0f) {
            blue = 0.0f;
        } else if (blue > 255.0f) {
            blue = 255.0f;
        }
        Color color3 = null;
        try {
            color3 = new Color(red, green, blue);
        }
        catch (IllegalArgumentException exp) {
            NumberFormat nf = NumberFormat.getNumberInstance();
            System.out.println(nf.format((double)red) + "; " + nf.format((double)green) + "; " + nf.format((double)blue));
            exp.printStackTrace();
        }
        return color3;
    }

    private static void drawNormalFace(EntityLivingBase e, float x, float y, float scale) {
        if (e instanceof EntityPlayer) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            List var5 = GuiPlayerTabOverlay.field_175252_a.sortedCopy(Minecraft.thePlayer.sendQueue.getPlayerInfoMap());
            for (Object aVar5 : var5) {
                NetworkPlayerInfo var6 = (NetworkPlayerInfo)aVar5;
                if (Minecraft.theWorld.getPlayerEntityByUUID(var6.getGameProfile().getId()) != e) continue;
                mc.getTextureManager().bindTexture(var6.getLocationSkin());
                Gui.drawScaledCustomSizeModalRect((int)x, (int)y, 8.0f, 8.0f, 8, 8, (int)scale, (int)scale, 64.0f, 64.0f);
                if (((EntityPlayer)e).isWearing(EnumPlayerModelParts.HAT)) {
                    Gui.drawScaledCustomSizeModalRect((int)x, (int)y, 40.0f, 8.0f, 8, 8, (int)scale, (int)scale, 64.0f, 64.0f);
                }
                GlStateManager.bindTexture(0);
                break;
            }
        }
    }

    public int[] getFractionIndicies(float[] fractions, float progress) {
        int startPoint;
        int[] range = new int[2];
        for (startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {
        }
        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }
        range[0] = startPoint - 1;
        range[1] = startPoint;
        return range;
    }

    private void Background(EntityLivingBase target) {
        int x = ScaledResolution.getScaledWidth() / 2 + 30;
        int y = ScaledResolution.getScaledHeight() / 2 - 5;
        double hea = target.getHealth();
        double f1 = new BigDecimal(hea).setScale(1, 4).doubleValue();
        if (Client.instance.FontLoaders.Comfortaa18.getStringWidth(target.getName()) > Client.instance.FontLoaders.Comfortaa14.getStringWidth("Health:" + f1)) {
            this.rect = Client.instance.FontLoaders.Comfortaa18.getStringWidth(target.getName());
        }
        if (Client.instance.FontLoaders.Comfortaa18.getStringWidth(target.getName()) == Client.instance.FontLoaders.Comfortaa14.getStringWidth("Health:" + f1)) {
            this.rect = Client.instance.FontLoaders.Comfortaa14.getStringWidth(target.getName());
        }
        if (Client.instance.FontLoaders.Comfortaa18.getStringWidth(target.getName()) < Client.instance.FontLoaders.Comfortaa14.getStringWidth("Health:" + f1)) {
            this.rect = Client.instance.FontLoaders.Comfortaa14.getStringWidth("Health:" + f1);
        }
        RenderUtil.drawFastRoundedRect(x - 2, (float)(y - 12), (int)((double)(x + 25) + this.rect), (float)(y - 20 + 28 + 15), 1.0f, new Color(40, 40, 40, 200).getRGB());
    }

    private static float getHealthes(EntityLivingBase entityLivingBase) {
        return (int)((float)((int)Math.ceil(entityLivingBase.getHealth())) + 0.5f);
    }

    private static void EvelinaonHead() {
        int x = ScaledResolution.getScaledWidth() / 2 + 30;
        int y = ScaledResolution.getScaledHeight() / 2 - 5;
        mc.getTextureManager().bindTexture(((AbstractClientPlayer)target).getLocationSkin());
        Gui.drawScaledCustomSizeModalRect(x, y - 10, 8.0f, 8.0f, 8, 8, 20, 20, 64.0f, 64.0f);
    }

    private void onName(EntityLivingBase target) {
        int x = ScaledResolution.getScaledWidth() / 2 + 30;
        int y = ScaledResolution.getScaledHeight() / 2 + 30;
        CFontRenderer font2 = Client.instance.FontLoaders.Comfortaa14;
        CFontRenderer font4 = Client.instance.FontLoaders.Comfortaa18;
        CFontRenderer font3 = Client.instance.FontLoaders.NovICON10;
        font4.drawStringWithShadow(target.getName(), x + 20 + 2, y - 45 + 3, -1);
        double hea = target.getHealth();
        double f1 = new BigDecimal(hea).setScale(1, 4).doubleValue();
        font2.drawStringWithShadow("", x - 20 + 50 + 2 + 2 + font2.getStringWidth("Health: " + f1), y - 25 + 20 + 6, -1);
        font2.drawStringWithShadow("Health:" + f1, x + 20 + 2, y - 40 + this.font.getStringHeight() + 2, -1);
        font3.drawString("s", x, y - 30 + this.font.getStringHeight() + 2, -1);
        font3.drawString("r", x, y - 30 + this.font.getStringHeight() + 8, -1);
    }

    static {
        modes = new Mode("Mode", new String[]{"Flux", "AstolfoNew", "Exhibition", "Novoline", "Novoline2", "Zeroday", "Hanabi", "OldPowerX", "NewPowerX", "Lnk", "Chocolate", "Remix", "Zenith", "Distance", "Lune", "GodLike"}, "AstolfoNew");
        xe = new Numbers<Double>("X", 0.0, 0.0, 1000.0, 0.5);
        ye = new Numbers<Double>("Y", 0.0, 0.0, 1000.0, 0.5);
    }
}

