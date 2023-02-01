/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.module.modules.globals.tweaker;

import heaven.main.module.modules.globals.tweaker.ASyncScreenShot;
import java.io.File;
import java.nio.IntBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class ScreenShotHook {
    private static IntBuffer pixelBuffer;
    private static int[] pixelValues;

    public static IChatComponent handleScreenshot(File gameDirectory, String screenshotName, int width, int height, Framebuffer buffer) {
        File screenshotDirectory = new File(Minecraft.getMinecraft().mcDataDir, "screenshots");
        if (!screenshotDirectory.exists()) {
            screenshotDirectory.mkdir();
        }
        if (OpenGlHelper.isFramebufferEnabled()) {
            width = buffer.framebufferTextureWidth;
            height = buffer.framebufferTextureHeight;
        }
        int imageScale = width * height;
        if (pixelBuffer == null || pixelBuffer.capacity() < imageScale) {
            pixelBuffer = BufferUtils.createIntBuffer((int)imageScale);
            pixelValues = new int[imageScale];
        }
        GL11.glPixelStorei((int)3333, (int)1);
        GL11.glPixelStorei((int)3317, (int)1);
        pixelBuffer.clear();
        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.bindTexture(buffer.framebufferTexture);
            GL11.glGetTexImage((int)3553, (int)0, (int)32993, (int)33639, (IntBuffer)pixelBuffer);
        } else {
            GL11.glReadPixels((int)0, (int)0, (int)width, (int)height, (int)32993, (int)33639, (IntBuffer)pixelBuffer);
        }
        pixelBuffer.get(pixelValues);
        new Thread(new ASyncScreenShot(width, height, pixelValues, Minecraft.getMinecraft().getFramebuffer(), screenshotDirectory)).start();
        return new ChatComponentText((Object)((Object)EnumChatFormatting.BLUE) + "(" + (Object)((Object)EnumChatFormatting.GRAY) + (Object)((Object)EnumChatFormatting.BOLD) + "Tweaker" + (Object)((Object)EnumChatFormatting.BLUE) + ") " + (Object)((Object)EnumChatFormatting.GRAY) + "Capturing screenshot.");
    }
}

