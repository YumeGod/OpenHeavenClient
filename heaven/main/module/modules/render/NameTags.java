/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.GLU
 */
package heaven.main.module.modules.render;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender;
import heaven.main.event.events.rendering.EventRender2D;
import heaven.main.management.FriendManager;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.AntiBot;
import heaven.main.module.modules.misc.Teams;
import heaven.main.ui.font.fontRenderer.newuse.BluelunFontRenderer;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.utils.math.RotationUtil;
import heaven.main.utils.render.color.Colors;
import heaven.main.value.Option;
import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class NameTags
extends Module {
    public Option<Boolean> invis = new Option<Boolean>("Invisible", false);
    public Option<Boolean> armor = new Option<Boolean>("Armor", false);
    public static Map<EntityLivingBase, double[]> entityPositions = new HashMap<EntityLivingBase, double[]>();
    public DecimalFormat format = new DecimalFormat("0.0");

    public NameTags() {
        super("NameTags", new String[]{"NameTag"}, ModuleType.Render);
        this.addValues(this.invis, this.armor);
    }

    @EventHandler
    private void onRender(EventRender e) {
        this.updatePositions();
    }

    @EventHandler
    public void onRender2D(EventRender2D class112) {
        if (Minecraft.thePlayer == null) {
            return;
        }
        GlStateManager.pushMatrix();
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        for (Map.Entry<EntityLivingBase, double[]> entry : entityPositions.entrySet()) {
            EntityLivingBase entity = entry.getKey();
            if (!((Boolean)this.invis.getValue()).booleanValue() && entity.isInvisible()) continue;
            GlStateManager.pushMatrix();
            if (entity instanceof EntityPlayer) {
                double[] array = entry.getValue();
                if (array[3] < 0.0 || array[3] >= 1.0) {
                    GlStateManager.popMatrix();
                    continue;
                }
                BluelunFontRenderer wqy16 = Client.instance.fontManager.chinese17;
                GlStateManager.translate(array[0] / (double)scaledResolution.getScaleFactor(), array[1] / (double)scaledResolution.getScaleFactor(), 0.0);
                this.scale();
                GlStateManager.translate(0.0, -2.5, 0.0);
                String s = AntiBot.isBot(entity) ? "[Bot]" : "  ";
                String s2 = FriendManager.isFriend(entity.getName()) ? "[Friend]" : "  ";
                String s3 = Teams.isOnSameTeam(entity) ? "[Team]" : "  ";
                String string = "Health: " + this.format.format(entity.getHealth());
                String string2 = s2 + s3 + s + entity.getDisplayName().getUnformattedText();
                float n = wqy16.getStringWidth(string2);
                float n2 = Client.instance.FontLoaders.regular13.getStringWidth(string);
                float n3 = Math.max(n, n2);
                float n4 = n3 - 8.0f;
                RenderUtil.drawGradientSidewaysV(-n4 / 2.0f, -22.0, n4 / 2.0f, -8.0, Colors.getColor(0, 180), Colors.getColor(0, 100));
                Client.instance.FontLoaders.regular15.drawString(string2, -n4 / 2.0f - 10.5f, -18.0f, Colors.WHITE.c);
                float n11 = (float)Math.ceil(entity.getHealth() + entity.getAbsorptionAmount()) / (entity.getMaxHealth() + entity.getAbsorptionAmount());
                int n12 = Colors.RED.c;
                String formattedText = entity.getDisplayName().getFormattedText();
                for (int i = 0; i < formattedText.length(); ++i) {
                    int index;
                    if (formattedText.charAt(i) != '\u00a7' || i + 1 >= formattedText.length() || (index = "0123456789abcdefklmnorg".indexOf(Character.toLowerCase(formattedText.charAt(i + 1)))) >= 16) continue;
                    try {
                        Color color = new Color(Minecraft.fontRendererObj.colorCode[index]);
                        n12 = this.getColor(color.getRed(), color.getGreen(), color.getBlue(), 255);
                        continue;
                    }
                    catch (ArrayIndexOutOfBoundsException color) {
                        // empty catch block
                    }
                }
                RenderUtil.drawGradientSideways(-n4 / 2.0f, -10.0, n4 / 2.0f - n4 / 2.0f * (1.0f - n11) * 2.0f, -8.0, new Color((float)new Color(n12).getRed() / 255.0f * 0.8f, (float)new Color(n12).getGreen() / 255.0f * 0.5f, (float)new Color(n12).getBlue() / 255.0f).brighter().getRGB(), n12);
                RenderUtil.drawRect(0.0f, 0.0f, 0.0f, 0.0f, 0);
                if (((Boolean)this.armor.getValue()).booleanValue()) {
                    ArrayList<ItemStack> list = new ArrayList<ItemStack>();
                    for (int j = 0; j < 5; ++j) {
                        ItemStack equipmentInSlot = entity.getEquipmentInSlot(j);
                        if (equipmentInSlot == null) continue;
                        list.add(equipmentInSlot);
                    }
                    int p_renderItemOverlays_3_ = -(list.size() * 9);
                    for (ItemStack p_getEnchantmentLevel_1_ : list) {
                        RenderHelper.enableGUIStandardItemLighting();
                        NameTags.mc.getRenderItem().zLevel = -150.0f;
                        this.fixGlintShit();
                        mc.getRenderItem().renderItemIntoGUI(p_getEnchantmentLevel_1_, p_renderItemOverlays_3_ + 6, -42);
                        mc.getRenderItem().renderItemOverlays(Minecraft.fontRendererObj, p_getEnchantmentLevel_1_, p_renderItemOverlays_3_, -42);
                        NameTags.mc.getRenderItem().zLevel = 0.0f;
                        p_renderItemOverlays_3_ += 3;
                        RenderHelper.disableStandardItemLighting();
                        if (p_getEnchantmentLevel_1_ == null) continue;
                        int n13 = 21;
                        int enchantmentLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, p_getEnchantmentLevel_1_);
                        int enchantmentLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, p_getEnchantmentLevel_1_);
                        int enchantmentLevel3 = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, p_getEnchantmentLevel_1_);
                        if (enchantmentLevel > 0) {
                            this.drawEnchantTag("Sh" + this.getColor(enchantmentLevel) + enchantmentLevel, p_renderItemOverlays_3_, n13);
                            n13 += 6;
                        }
                        if (enchantmentLevel2 > 0) {
                            this.drawEnchantTag("Fir" + this.getColor(enchantmentLevel2) + enchantmentLevel2, p_renderItemOverlays_3_, n13);
                            n13 += 6;
                        }
                        if (enchantmentLevel3 > 0) {
                            this.drawEnchantTag("Kb" + this.getColor(enchantmentLevel3) + enchantmentLevel3, p_renderItemOverlays_3_, n13);
                        } else if (p_getEnchantmentLevel_1_.getItem() instanceof ItemArmor) {
                            int enchantmentLevel4 = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, p_getEnchantmentLevel_1_);
                            int enchantmentLevel5 = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, p_getEnchantmentLevel_1_);
                            int enchantmentLevel6 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, p_getEnchantmentLevel_1_);
                            if (enchantmentLevel4 > 0) {
                                this.drawEnchantTag("P" + this.getColor(enchantmentLevel4) + enchantmentLevel4, p_renderItemOverlays_3_, n13);
                                n13 += 6;
                            }
                            if (enchantmentLevel5 > 0) {
                                this.drawEnchantTag("Th" + this.getColor(enchantmentLevel5) + enchantmentLevel5, p_renderItemOverlays_3_, n13);
                                n13 += 6;
                            }
                            if (enchantmentLevel6 > 0) {
                                this.drawEnchantTag("Unb" + this.getColor(enchantmentLevel6) + enchantmentLevel6, p_renderItemOverlays_3_, n13);
                            }
                        } else if (p_getEnchantmentLevel_1_.getItem() instanceof ItemBow) {
                            int enchantmentLevel7 = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, p_getEnchantmentLevel_1_);
                            int enchantmentLevel8 = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, p_getEnchantmentLevel_1_);
                            int enchantmentLevel9 = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, p_getEnchantmentLevel_1_);
                            if (enchantmentLevel7 > 0) {
                                this.drawEnchantTag("Pow" + this.getColor(enchantmentLevel7) + enchantmentLevel7, p_renderItemOverlays_3_, n13);
                                n13 += 6;
                            }
                            if (enchantmentLevel8 > 0) {
                                this.drawEnchantTag("Pun" + this.getColor(enchantmentLevel8) + enchantmentLevel8, p_renderItemOverlays_3_, n13);
                                n13 += 6;
                            }
                            if (enchantmentLevel9 > 0) {
                                this.drawEnchantTag("Fir" + this.getColor(enchantmentLevel9) + enchantmentLevel9, p_renderItemOverlays_3_, n13);
                            }
                        } else if (p_getEnchantmentLevel_1_.getRarity() == EnumRarity.EPIC) {
                            this.drawEnchantTag("\u00a76\u00a7lGod", p_renderItemOverlays_3_ - 2, n13);
                        }
                        float n14 = (float)((double)p_renderItemOverlays_3_ * 1.05) - 2.0f;
                        if (p_getEnchantmentLevel_1_.getMaxDamage() - p_getEnchantmentLevel_1_.getItemDamage() > 0) {
                            GlStateManager.pushMatrix();
                            GlStateManager.disableDepth();
                            Client.instance.FontLoaders.regular14.drawString(String.valueOf(p_getEnchantmentLevel_1_.getMaxDamage() - p_getEnchantmentLevel_1_.getItemDamage()), n14 + 6.0f, -32.0f, Colors.WHITE.c);
                            GlStateManager.enableDepth();
                            GlStateManager.popMatrix();
                        }
                        p_renderItemOverlays_3_ += 12;
                    }
                }
            }
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();
    }

    private String getColor(int n) {
        if (n != 1) {
            if (n == 2) {
                return "\u00a7a";
            }
            if (n == 3) {
                return "\u00a73";
            }
            if (n == 4) {
                return "\u00a74";
            }
            if (n >= 5) {
                return "\u00a76";
            }
        }
        return "\u00a7f";
    }

    private void drawEnchantTag(String s, int n, int n2) {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        Client.instance.FontLoaders.regular10.drawStringWithColor(s, n + 9, -30 - (n2 -= 6), Colors.getColor(255), 255);
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    public int getColor(int p_clamp_int_0_, int p_clamp_int_0_2, int p_clamp_int_0_3, int p_clamp_int_0_4) {
        return MathHelper.clamp_int(p_clamp_int_0_4, 0, 255) << 24 | MathHelper.clamp_int(p_clamp_int_0_, 0, 255) << 16 | MathHelper.clamp_int(p_clamp_int_0_2, 0, 255) << 8 | MathHelper.clamp_int(p_clamp_int_0_3, 0, 255);
    }

    private void scale() {
        float n = 1.05f;
        GlStateManager.scale(1.05f, 1.05f, 1.05f);
    }

    private void fixGlintShit() {
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
    }

    private void updatePositions() {
        entityPositions.clear();
        float pTicks = NameTags.mc.timer.renderPartialTicks;
        for (Entity o : Minecraft.theWorld.loadedEntityList) {
            if (o == Minecraft.thePlayer || !(o instanceof EntityPlayer) || o.isInvisible() && ((Boolean)this.invis.getValue()).booleanValue()) continue;
            double x = o.lastTickPosX + (o.posX - o.lastTickPosX) * (double)pTicks - NameTags.mc.getRenderManager().viewerPosX;
            double y = o.lastTickPosY + (o.posY - o.lastTickPosY) * (double)pTicks - NameTags.mc.getRenderManager().viewerPosY;
            double z = o.lastTickPosZ + (o.posZ - o.lastTickPosZ) * (double)pTicks - NameTags.mc.getRenderManager().viewerPosZ;
            if (!(Objects.requireNonNull(this.convertTo2D(x, y += (double)o.height + 0.2, z))[2] >= 0.0) || !(Objects.requireNonNull(this.convertTo2D(x, y, z))[2] < 1.0)) continue;
            entityPositions.put((EntityPlayer)o, new double[]{Objects.requireNonNull(this.convertTo2D(x, y, z))[0], Objects.requireNonNull(this.convertTo2D(x, y, z))[1], Math.abs(this.convertTo2D(x, y + 1.0, z, o)[1] - this.convertTo2D(x, y, z, o)[1]), Objects.requireNonNull(this.convertTo2D(x, y, z))[2]});
        }
    }

    private double[] convertTo2D(double x, double y, double z, Entity ent) {
        float pTicks = NameTags.mc.timer.renderPartialTicks;
        float prevYaw = Minecraft.thePlayer.rotationYaw;
        float prevPrevYaw = Minecraft.thePlayer.prevRotationYaw;
        float[] rotations = RotationUtil.getRotationFromPosition(ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double)pTicks, ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double)pTicks, ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * (double)pTicks - 1.6);
        NameTags.mc.getRenderViewEntity().rotationYaw = NameTags.mc.getRenderViewEntity().prevRotationYaw = rotations[0];
        NameTags.mc.entityRenderer.setupCameraTransform(pTicks, 0);
        double[] convertedPoints = this.convertTo2D(x, y, z);
        NameTags.mc.getRenderViewEntity().rotationYaw = prevYaw;
        NameTags.mc.getRenderViewEntity().prevRotationYaw = prevPrevYaw;
        NameTags.mc.entityRenderer.setupCameraTransform(pTicks, 0);
        return convertedPoints;
    }

    private double[] convertTo2D(double x, double y, double z) {
        FloatBuffer screenCoords = BufferUtils.createFloatBuffer((int)3);
        IntBuffer viewport = BufferUtils.createIntBuffer((int)16);
        FloatBuffer modelView = BufferUtils.createFloatBuffer((int)16);
        FloatBuffer projection = BufferUtils.createFloatBuffer((int)16);
        GL11.glGetFloat((int)2982, (FloatBuffer)modelView);
        GL11.glGetFloat((int)2983, (FloatBuffer)projection);
        GL11.glGetInteger((int)2978, (IntBuffer)viewport);
        boolean result = GLU.gluProject((float)((float)x), (float)((float)y), (float)((float)z), (FloatBuffer)modelView, (FloatBuffer)projection, (IntBuffer)viewport, (FloatBuffer)screenCoords);
        if (result) {
            return new double[]{screenCoords.get(0), (float)Display.getHeight() - screenCoords.get(1), screenCoords.get(2)};
        }
        return null;
    }
}

