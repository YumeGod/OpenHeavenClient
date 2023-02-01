/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.globals.tweaker;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class ASyncScreenShot
implements Runnable {
    private final int width;
    private final int height;
    private final int[] pixelValues;
    private final Framebuffer framebuffer;
    private final File screenshotDirectory;

    ASyncScreenShot(int width, int height, int[] pixelValues, Framebuffer framebuffer, File screenshotDirectory) {
        this.width = width;
        this.height = height;
        this.pixelValues = pixelValues;
        this.framebuffer = framebuffer;
        this.screenshotDirectory = screenshotDirectory;
    }

    @Override
    public void run() {
        ASyncScreenShot.processPixelValues(this.pixelValues, this.width, this.height);
        File screenshot = ASyncScreenShot.getTimestampedPNGFileForDirectory(this.screenshotDirectory);
        try {
            BufferedImage image;
            if (OpenGlHelper.isFramebufferEnabled()) {
                int tHeight;
                image = new BufferedImage(this.framebuffer.framebufferWidth, this.framebuffer.framebufferHeight, 1);
                int heightSize = tHeight = this.framebuffer.framebufferTextureHeight - this.framebuffer.framebufferHeight;
                while (tHeight < this.framebuffer.framebufferTextureHeight) {
                    for (int widthSize = 0; widthSize < this.framebuffer.framebufferWidth; ++widthSize) {
                        image.setRGB(widthSize, tHeight - heightSize, this.pixelValues[tHeight * this.framebuffer.framebufferTextureWidth + widthSize]);
                    }
                    ++tHeight;
                }
            } else {
                image = new BufferedImage(this.width, this.height, 1);
                image.setRGB(0, 0, this.width, this.height, this.pixelValues, 0, this.width);
            }
            ImageIO.write((RenderedImage)image, "png", screenshot);
            ChatComponentText chat = new ChatComponentText((Object)((Object)EnumChatFormatting.BLUE) + "(" + (Object)((Object)EnumChatFormatting.GRAY) + (Object)((Object)EnumChatFormatting.BOLD) + "Tweaker" + (Object)((Object)EnumChatFormatting.BLUE) + ") " + (Object)((Object)EnumChatFormatting.GRAY) + "Screenshot saved to " + (Object)((Object)EnumChatFormatting.BOLD) + screenshot.getName());
            chat.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, screenshot.getCanonicalPath()));
            Minecraft.getMinecraft();
            Minecraft.thePlayer.addChatMessage(chat);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processPixelValues(int[] pixels, int displayWidth, int displayHeight) {
        int[] xValues = new int[displayWidth];
        int yValues = displayHeight / 2;
        for (int val = 0; val < yValues; ++val) {
            System.arraycopy(pixels, val * displayWidth, xValues, 0, displayWidth);
            System.arraycopy(pixels, (displayHeight - 1 - val) * displayWidth, pixels, val * displayWidth, displayWidth);
            System.arraycopy(xValues, 0, pixels, (displayHeight - 1 - val) * displayWidth, displayWidth);
        }
    }

    private static File getTimestampedPNGFileForDirectory(File gameDirectory) {
        File screenshot;
        String dateFormatting = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date());
        int screenshotCount = 1;
        while ((screenshot = new File(gameDirectory, dateFormatting + (screenshotCount == 1 ? "" : "_" + screenshotCount) + ".png")).exists()) {
            ++screenshotCount;
        }
        return screenshot;
    }
}

