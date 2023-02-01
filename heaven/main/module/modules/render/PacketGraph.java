/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender2D;
import heaven.main.event.events.world.EventUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.HUD;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.ui.simplecore.SimpleRender;
import heaven.main.utils.object.BlockObject;
import heaven.main.utils.render.RenderUtils;
import heaven.main.utils.render.gl.ScaleUtils;
import heaven.main.utils.timer.TimerUtil;
import heaven.main.value.Numbers;
import java.awt.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class PacketGraph
extends Module {
    private final List<BlockObject> clientBlocks = new CopyOnWriteArrayList<BlockObject>();
    private final List<BlockObject> serverBlocks = new CopyOnWriteArrayList<BlockObject>();
    private final TimerUtil timerUtil = new TimerUtil();
    private final TimerUtil secTimerUtil = new TimerUtil();
    private final Numbers<Double> X = new Numbers<Double>("X", 650.0, 1.0, 1920.0, 5.0);
    private final Numbers<Double> Y = new Numbers<Double>("Y", 2.0, 0.0, 1080.0, 5.0);
    private static int clientPackets;
    private static int serverPackets;
    private static int secClientPackets;
    private static int secServerPackets;
    private int renderSecClientPackets;
    private int renderSecServerPackets;
    float x;
    float y;

    public PacketGraph() {
        super("PacketGraph", ModuleType.Render);
        this.addValues(this.X, this.Y);
    }

    public void clear() {
        clientPackets = 0;
        serverPackets = 0;
        secClientPackets = 0;
        secServerPackets = 0;
        this.renderSecClientPackets = 0;
        this.renderSecServerPackets = 0;
        this.clientBlocks.clear();
        this.serverBlocks.clear();
        this.timerUtil.reset();
        this.secTimerUtil.reset();
    }

    @EventHandler
    public void onUpdate(EventUpdate event) {
        ++serverPackets;
        ++secServerPackets;
    }

    @Override
    public void onDisable() {
        this.y = 0.0f;
        this.x = 0.0f;
    }

    @EventHandler
    public void onRender(EventRender2D event) {
        if (PacketGraph.mc.gameSettings.showDebugInfo) {
            return;
        }
        if (this.x != ((Double)this.X.get()).floatValue()) {
            this.x = AnimationUtil.moveUD(this.x, ((Double)this.X.get()).floatValue(), SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
        }
        if (this.y != ((Double)this.Y.get()).floatValue()) {
            this.y = AnimationUtil.moveUD(this.y, ((Double)this.Y.get()).floatValue(), SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
        }
        GL11.glPushMatrix();
        ScaleUtils.scale(mc);
        if (this.timerUtil.hasReached(50.0)) {
            this.clientBlocks.forEach(blockObject -> --blockObject.x);
            this.clientBlocks.add(new BlockObject((int)(this.x + 150.0f), Math.min(clientPackets, 55)));
            clientPackets = 0;
            this.serverBlocks.forEach(blockObject -> --blockObject.x);
            this.serverBlocks.add(new BlockObject((int)(this.x + 150.0f), Math.min(serverPackets, 55)));
            serverPackets = 0;
            this.timerUtil.reset();
        }
        if (this.secTimerUtil.hasReached(1000.0)) {
            this.renderSecClientPackets = secClientPackets;
            this.renderSecServerPackets = secServerPackets;
            secClientPackets = 0;
            secServerPackets = 0;
            this.secTimerUtil.reset();
        }
        float graphY = (int)this.y;
        for (int i = 0; i < 2; ++i) {
            this.drawGraph(i, (int)this.x, graphY);
            graphY += 68.0f;
        }
        GL11.glPushMatrix();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        if (!this.clientBlocks.isEmpty()) {
            BlockObject firstBlock = this.clientBlocks.get(this.clientBlocks.size() - 1);
            Minecraft.fontRendererObj.drawString("< avg: " + firstBlock.height, (int)(this.x * 2.0f + 301.0f), (int)(this.y * 2.0f - (float)(firstBlock.height << 1) - 4.0f), -1);
        }
        if (!this.serverBlocks.isEmpty()) {
            BlockObject firstBlock = this.serverBlocks.get(this.serverBlocks.size() - 1);
            Minecraft.fontRendererObj.drawString("< avg: " + firstBlock.height, (int)(this.x * 2.0f + 301.0f), (int)((this.y + 68.0f) * 2.0f - (float)(firstBlock.height << 1) - 4.0f), -1);
        }
        GL11.glPopMatrix();
        this.clientBlocks.removeIf(block -> (float)block.x < this.x);
        this.serverBlocks.removeIf(block -> (float)block.x < this.x);
        GL11.glPopMatrix();
    }

    @EventHandler
    public void InScreenRender() {
        if (PacketGraph.mc.gameSettings.showDebugInfo) {
            return;
        }
        GL11.glPushMatrix();
        ScaleUtils.scale(mc);
        if (this.timerUtil.hasReached(50.0)) {
            this.clientBlocks.forEach(blockObject -> --blockObject.x);
            this.clientBlocks.add(new BlockObject((int)(this.x + 150.0f), Math.min(clientPackets, 55)));
            clientPackets = 0;
            this.serverBlocks.forEach(blockObject -> --blockObject.x);
            this.serverBlocks.add(new BlockObject((int)(this.x + 150.0f), Math.min(serverPackets, 55)));
            serverPackets = 0;
            this.timerUtil.reset();
        }
        if (this.secTimerUtil.hasReached(1000.0)) {
            this.renderSecClientPackets = secClientPackets;
            this.renderSecServerPackets = secServerPackets;
            secClientPackets = 0;
            secServerPackets = 0;
            this.secTimerUtil.reset();
        }
        float graphY = (int)this.y;
        for (int i = 0; i < 2; ++i) {
            this.drawGraph(i, (int)this.x, graphY);
            graphY += 68.0f;
        }
        GL11.glPushMatrix();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        if (!this.clientBlocks.isEmpty()) {
            BlockObject firstBlock = this.clientBlocks.get(this.clientBlocks.size() - 1);
            Minecraft.fontRendererObj.drawString("< avg: " + firstBlock.height, (int)(this.x * 2.0f + 301.0f), (int)(this.y * 2.0f - (float)(firstBlock.height * 2) - 4.0f), -1);
        }
        if (!this.serverBlocks.isEmpty()) {
            BlockObject firstBlock = this.serverBlocks.get(this.serverBlocks.size() - 1);
            Minecraft.fontRendererObj.drawString("< avg: " + firstBlock.height, (int)(this.x * 2.0f + 301.0f), (int)((this.y + 68.0f) * 2.0f - (float)(firstBlock.height << 1) - 4.0f), -1);
        }
        GL11.glPopMatrix();
        this.clientBlocks.removeIf(block -> (float)block.x < this.x);
        this.serverBlocks.removeIf(block -> (float)block.x < this.x);
        this.InScreenRender();
        GL11.glPopMatrix();
    }

    private void drawGraph(int mode, float x, float y) {
        if (PacketGraph.mc.gameSettings.showDebugInfo) {
            return;
        }
        boolean isClient = mode == 0;
        RenderUtil.drawRect(x, (float)((double)y + 0.5), x + 150.0f, y - 60.0f, new Color(0, 0, 0, 180).getRGB());
        GL11.glPushMatrix();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        String secString = isClient ? this.renderSecClientPackets + " packets/sec" : this.renderSecServerPackets + " packets/sec";
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Minecraft.fontRendererObj.drawString(isClient ? "Outgoing packets" : "Incoming packets", (int)(x * 2.0f), (int)(y * 2.0f - 129.0f), -1);
        Minecraft.fontRendererObj.drawString(secString, (int)(x * 2.0f + 300.0f - (float)Minecraft.fontRendererObj.getStringWidth(secString)), (int)(y * 2.0f - 129.0f), -1);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)2.0f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glBegin((int)1);
        List<BlockObject> list = isClient ? this.clientBlocks : this.serverBlocks;
        Color col = new Color(RenderUtils.rainbow(System.nanoTime(), 0.0f, 1.0f).getRGB());
        Color cuscolor = new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue());
        Color Ranbowe = new Color((float)cuscolor.getRed() / 255.0f, (float)cuscolor.getGreen() / 255.0f, (float)cuscolor.getBlue() / 255.0f, 1.0f - (float)col.getGreen() / 400.0f);
        for (BlockObject block : list) {
            GL11.glColor4f((float)((float)Ranbowe.getRed() / 255.0f), (float)((float)Ranbowe.getGreen() / 255.0f), (float)((float)Ranbowe.getBlue() / 255.0f), (float)((float)Ranbowe.getAlpha() / 255.0f));
            GL11.glVertex2d((double)block.x, (double)(y - (float)block.height));
            try {
                BlockObject lastBlock = list.get(list.indexOf(block) + 1);
                GL11.glVertex2d((double)(block.x + 1), (double)(y - (float)lastBlock.height));
            }
            catch (Exception exception) {}
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GlStateManager.resetColor();
        GL11.glPopMatrix();
    }
}

