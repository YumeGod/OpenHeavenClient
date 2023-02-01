/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL20
 */
package heaven.main.utils.shader.shaders;

import heaven.main.utils.shader.Shader;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL20;

public final class FireShader
extends Shader {
    public static final FireShader BACKGROUND_SHADER = new FireShader();
    private float time;

    public FireShader() {
        super("firelogin.frag");
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("iResolution");
        this.setupUniform("iTime");
    }

    @Override
    public void updateUniforms() {
        int timeID;
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int resolutionID = this.getUniform("iResolution");
        if (resolutionID > -1) {
            GL20.glUniform2f((int)resolutionID, (float)((float)scaledResolution.getScaledWidth() * 2.0f), (float)((float)scaledResolution.getScaledHeight() * 2.0f));
        }
        if ((timeID = this.getUniform("iTime")) > -1) {
            GL20.glUniform1f((int)timeID, (float)this.time);
        }
        this.time = (float)((double)this.time + 0.2);
    }
}

