/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.GLU
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender2D;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.HUD;
import heaven.main.ui.simplecore.SimpleRender;
import heaven.main.utils.render.RenderUtils;
import heaven.main.utils.render.gl.ScaleUtils;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class ItemESP
extends Module {
    private final Option<Boolean> names = new Option<Boolean>("Names", true);
    private final Option<Boolean> neededOnly = new Option<Boolean>("NeededOnly", false);
    private final Option<Boolean> itemColor = new Option<Boolean>("ItemColor", true);
    private final Option<Boolean> hudColor = new Option<Boolean>("HUDCOlor", false, () -> (Boolean)this.itemColor.get() == false);
    public final Numbers<Double> r = new Numbers<Double>("Red", 120.0, 0.0, 255.0, 5.0, () -> (Boolean)this.itemColor.get() == false, () -> (Boolean)this.hudColor.get() == false);
    public final Numbers<Double> g = new Numbers<Double>("Green", 120.0, 0.0, 255.0, 5.0, () -> (Boolean)this.itemColor.get() == false, () -> (Boolean)this.hudColor.get() == false);
    public final Numbers<Double> b = new Numbers<Double>("Blue", 255.0, 0.0, 255.0, 5.0, () -> (Boolean)this.itemColor.get() == false, () -> (Boolean)this.hudColor.get() == false);
    private final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
    private final FloatBuffer modelView = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);

    public ItemESP() {
        super("ItemESP", ModuleType.Render);
        this.addValues(this.names, this.neededOnly, this.itemColor, this.hudColor, this.r, this.g, this.b);
    }

    @EventHandler
    public void onRender(EventRender2D er) {
        for (Entity o : Minecraft.theWorld.getLoadedEntityList()) {
            if (!(o instanceof EntityItem)) continue;
            float f1 = MathHelper.sin(((float)((EntityItem)o).getAge() + er.getPartialTicks()) / 10.0f + ((EntityItem)o).hoverStart) * 0.1f + 0.1f;
            double x = RenderUtils.interpolate(o.posX, o.lastTickPosX, er.getPartialTicks());
            double y = RenderUtils.interpolate(o.posY + (double)f1, o.lastTickPosY + (double)f1, er.getPartialTicks());
            double z = RenderUtils.interpolate(o.posZ, o.lastTickPosZ, er.getPartialTicks());
            double width = (double)o.width / 1.4;
            double height = (double)o.height + 0.2;
            AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
            List<Vector3d> vectors = Arrays.asList(new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ));
            ItemESP.mc.entityRenderer.setupCameraTransform(er.getPartialTicks(), 0);
            Vector4d position = null;
            for (Vector3d vector : vectors) {
                vector = this.project2D(er.getResolution(), vector.x - ItemESP.mc.getRenderManager().viewerPosX, vector.y - ItemESP.mc.getRenderManager().viewerPosY, vector.z - ItemESP.mc.getRenderManager().viewerPosZ);
                if (vector == null || !(vector.z >= 0.0) || !(vector.z < 1.0)) continue;
                if (position == null) {
                    position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                }
                position.x = Math.min(vector.x, position.x);
                position.y = Math.min(vector.y, position.y);
                position.z = Math.max(vector.x, position.z);
                position.w = Math.max(vector.y, position.w);
            }
            ItemESP.mc.entityRenderer.setupOverlayRendering();
            if (position == null || ((Boolean)this.neededOnly.get()).booleanValue() && !this.isItemSpecial((EntityItem)o)) continue;
            double posX = position.x;
            double posY = position.y;
            double endPosX = position.z;
            double endPosY = position.w;
            RenderUtils.drawCornerBox(posX, posY, endPosX, endPosY, this.isItemSpecial((EntityItem)o) ? 4.0 : 3.0, Color.BLACK);
            RenderUtils.drawCornerBox(posX, posY, endPosX, endPosY, this.isItemSpecial((EntityItem)o) ? 2.0 : 1.0, this.getItemColor((EntityItem)o));
            if (!((Boolean)this.names.get()).booleanValue()) continue;
            float amp = 1.0f;
            switch (ItemESP.mc.gameSettings.guiScale) {
                case 0: {
                    amp = 0.5f;
                    break;
                }
                case 1: {
                    amp = 2.0f;
                    break;
                }
                case 3: {
                    amp = 0.6666667f;
                }
            }
            double[] positions = ScaleUtils.getScaledMouseCoordinates(mc, posX, posY);
            double[] positionsEnd = ScaleUtils.getScaledMouseCoordinates(mc, endPosX, endPosY);
            double[] scaledPositions = new double[]{positions[0] * 2.0, positions[1] * 2.0, positionsEnd[0] * 2.0, positionsEnd[1] * 2.0};
            GL11.glPushMatrix();
            GL11.glScalef((float)(0.5f * amp), (float)(0.5f * amp), (float)(0.5f * amp));
            double _width = Math.abs(scaledPositions[2] - scaledPositions[0]);
            float v = (float)(ItemESP.mc.fontRendererCrack.getHeight() << 1) - (float)(ItemESP.mc.fontRendererCrack.getHeight() / 2);
            ItemESP.mc.fontRendererCrack.drawStringWithShadow(((EntityItem)o).getEntityItem().getDisplayName(), (float)(scaledPositions[0] + _width / 2.0 - (double)(ItemESP.mc.fontRendererCrack.getStringWidth(((EntityItem)o).getEntityItem().getDisplayName()) / 2)), (float)positionsEnd[1] * 2.0f + v, this.getItemColor((EntityItem)o).brighter().getRGB());
            GL11.glPopMatrix();
        }
    }

    private boolean isItemSpecial(EntityItem o) {
        boolean special;
        boolean bl = special = o.getEntityItem().getItem() instanceof ItemArmor || o.getEntityItem().getItem() == Items.skull && !o.getEntityItem().getDisplayName().equalsIgnoreCase("Zombie Head") && !o.getEntityItem().getDisplayName().equalsIgnoreCase("Creeper Head") && !o.getEntityItem().getDisplayName().equalsIgnoreCase("Skeleton Skull") && !o.getEntityItem().getDisplayName().equalsIgnoreCase("Wither Skeleton Skull") && !o.getEntityItem().getDisplayName().equalsIgnoreCase((Object)((Object)EnumChatFormatting.GREEN) + "Frog's Hat") || o.getEntityItem().getItem() instanceof ItemAppleGold;
        if (o.getEntityItem().getItem() instanceof ItemArmor) {
            for (int type = 1; type < 5; ++type) {
                String strType = "";
                switch (type) {
                    case 1: {
                        strType = "helmet";
                        break;
                    }
                    case 2: {
                        strType = "chestplate";
                        break;
                    }
                    case 3: {
                        strType = "leggings";
                        break;
                    }
                    case 4: {
                        strType = "boots";
                    }
                }
                if (!Minecraft.thePlayer.getSlotFromPlayerContainer(4 + type).getHasStack()) continue;
                ItemStack is = Minecraft.thePlayer.getSlotFromPlayerContainer(4 + type).getStack();
                if (!is.getItem().getUnlocalizedName().contains(strType) || !o.getEntityItem().getItem().getUnlocalizedName().contains(strType)) continue;
                return this.getProtection(o.getEntityItem()) > this.getProtection(Minecraft.thePlayer.getSlotFromPlayerContainer(4 + type).getStack());
            }
            return !this.hasItem(o.getEntityItem());
        }
        if (o.getEntityItem().getItem() instanceof ItemSword) {
            for (int i = 9; i < 45; ++i) {
                if (!Minecraft.thePlayer.getSlotFromPlayerContainer(i).getHasStack()) continue;
                if (!(Minecraft.thePlayer.getSlotFromPlayerContainer(i).getStack().getItem() instanceof ItemSword)) continue;
                return this.getDamage(o.getEntityItem()) > this.getDamage(Minecraft.thePlayer.getSlotFromPlayerContainer(i).getStack());
            }
            return !this.hasItem(o.getEntityItem());
        }
        if (o.getEntityItem().getItem() instanceof ItemPickaxe) {
            for (int i = 9; i < 45; ++i) {
                if (!Minecraft.thePlayer.getSlotFromPlayerContainer(i).getHasStack()) continue;
                if (!(Minecraft.thePlayer.getSlotFromPlayerContainer(i).getStack().getItem() instanceof ItemPickaxe)) continue;
                return this.getToolEffect(o.getEntityItem()) > this.getToolEffect(Minecraft.thePlayer.getSlotFromPlayerContainer(i).getStack());
            }
            return !this.hasItem(o.getEntityItem());
        }
        if (o.getEntityItem().getItem() instanceof ItemSpade) {
            for (int i = 9; i < 45; ++i) {
                if (!Minecraft.thePlayer.getSlotFromPlayerContainer(i).getHasStack()) continue;
                if (!(Minecraft.thePlayer.getSlotFromPlayerContainer(i).getStack().getItem() instanceof ItemSpade)) continue;
                return this.getToolEffect(o.getEntityItem()) > this.getToolEffect(Minecraft.thePlayer.getSlotFromPlayerContainer(i).getStack());
            }
            return !this.hasItem(o.getEntityItem());
        }
        if (o.getEntityItem().getItem() instanceof ItemAxe) {
            for (int i = 9; i < 45; ++i) {
                if (!Minecraft.thePlayer.getSlotFromPlayerContainer(i).getHasStack()) continue;
                if (!(Minecraft.thePlayer.getSlotFromPlayerContainer(i).getStack().getItem() instanceof ItemAxe)) continue;
                return this.getToolEffect(o.getEntityItem()) > this.getToolEffect(Minecraft.thePlayer.getSlotFromPlayerContainer(i).getStack());
            }
            return !this.hasItem(o.getEntityItem());
        }
        return special;
    }

    private float getProtection(ItemStack stack) {
        float protection = 0.0f;
        if (stack.getItem() instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor)stack.getItem();
            protection = (float)((double)protection + ((double)armor.damageReduceAmount + (double)((100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack)) * 0.0075));
            protection = (float)((double)protection + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack) / 100.0);
            protection = (float)((double)protection + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack) / 100.0);
            protection = (float)((double)protection + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack) / 100.0);
            protection = (float)((double)protection + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 50.0);
            protection = (float)((double)protection + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack) / 100.0);
        }
        return protection;
    }

    private float getDamage(ItemStack stack) {
        float damage = 0.0f;
        Item item = stack.getItem();
        if (item instanceof ItemTool) {
            damage += ((ItemTool)item).getDamage();
        } else if (item instanceof ItemSword) {
            damage += ((ItemSword)item).getAttackDamage();
        }
        return damage += (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f + (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
    }

    private float getToolEffect(ItemStack stack) {
        float value;
        Item item = stack.getItem();
        if (!(item instanceof ItemTool)) {
            return 0.0f;
        }
        String name = item.getUnlocalizedName();
        ItemTool tool = (ItemTool)item;
        if (item instanceof ItemPickaxe) {
            value = tool.getStrVsBlock(stack, Blocks.stone);
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        } else if (item instanceof ItemSpade) {
            value = tool.getStrVsBlock(stack, Blocks.dirt);
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        } else if (item instanceof ItemAxe) {
            value = tool.getStrVsBlock(stack, Blocks.log);
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        } else {
            return 1.0f;
        }
        value = (float)((double)value + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075);
        value = (float)((double)value + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100.0);
        return value;
    }

    private boolean hasItem(ItemStack is) {
        int i;
        for (i = 0; i < 3; ++i) {
            if (Minecraft.thePlayer.inventory.armorInventory[i] == null) continue;
            if (Minecraft.thePlayer.inventory.armorInventory[i].getItem() != is.getItem()) continue;
            return true;
        }
        for (i = 9; i < 45; ++i) {
            if (!Minecraft.thePlayer.getSlotFromPlayerContainer(i).getHasStack()) continue;
            ItemStack is1 = Minecraft.thePlayer.getSlotFromPlayerContainer(i).getStack();
            if (is.getItem() != is1.getItem()) continue;
            return true;
        }
        return false;
    }

    private Color getItemColor(EntityItem o) {
        if (((Boolean)this.itemColor.get()).booleanValue()) {
            String displayName = o.getEntityItem().getDisplayName();
            if (displayName.equalsIgnoreCase((Object)((Object)EnumChatFormatting.GOLD) + "Excalibur") || displayName.equalsIgnoreCase("aDragon Sword") || displayName.equalsIgnoreCase((Object)((Object)EnumChatFormatting.GREEN) + "Cornucopia") || displayName.equalsIgnoreCase((Object)((Object)EnumChatFormatting.RED) + "Bloodlust") || displayName.equalsIgnoreCase((Object)((Object)EnumChatFormatting.RED) + "Artemis' Bow") || displayName.equalsIgnoreCase((Object)((Object)EnumChatFormatting.GREEN) + "Miner's Blessing") || displayName.equalsIgnoreCase((Object)((Object)EnumChatFormatting.GOLD) + "Axe of Perun") || displayName.equalsIgnoreCase((Object)((Object)EnumChatFormatting.GOLD) + "Cornucopia")) {
                return new Color(SimpleRender.getArrayRainbow((int)(System.currentTimeMillis() / 100000L), 255));
            }
            if (!this.isItemSpecial(o)) {
                return new Color(255, 255, 255);
            }
            if (o.getEntityItem().getItem() instanceof ItemArmor) {
                return new Color(75, 189, 193);
            }
            if (o.getEntityItem().getItem() instanceof ItemAppleGold) {
                return new Color(255, 199, 71);
            }
            if (o.getEntityItem().getItem() instanceof ItemSkull && this.isItemSpecial(o)) {
                return new Color(255, 199, 71);
            }
            if (o.getEntityItem().getItem() instanceof ItemSword) {
                return new Color(255, 117, 117);
            }
            if (o.getEntityItem().getItem() instanceof ItemPickaxe) {
                return new Color(130, 219, 82);
            }
            if (o.getEntityItem().getItem() instanceof ItemSpade) {
                return new Color(130, 219, 82);
            }
            if (o.getEntityItem().getItem() instanceof ItemAxe) {
                return new Color(130, 219, 82);
            }
            return new Color(255, 255, 255);
        }
        return (Boolean)this.hudColor.get() != false ? new Color(((Double)HUD.r.get()).intValue(), ((Double)HUD.g.get()).intValue(), ((Double)HUD.b.get()).intValue()) : new Color(((Double)this.r.get()).intValue(), ((Double)this.g.get()).intValue(), ((Double)this.b.get()).intValue());
    }

    private Vector3d project2D(ScaledResolution scaledResolution, double x, double y, double z) {
        GL11.glGetFloat((int)2982, (FloatBuffer)this.modelView);
        GL11.glGetFloat((int)2983, (FloatBuffer)this.projection);
        GL11.glGetInteger((int)2978, (IntBuffer)this.viewport);
        if (GLU.gluProject((float)((float)x), (float)((float)y), (float)((float)z), (FloatBuffer)this.modelView, (FloatBuffer)this.projection, (IntBuffer)this.viewport, (FloatBuffer)this.vector)) {
            return new Vector3d(this.vector.get(0) / (float)scaledResolution.getScaleFactor(), ((float)Display.getHeight() - this.vector.get(1)) / (float)scaledResolution.getScaleFactor(), this.vector.get(2));
        }
        return null;
    }
}

