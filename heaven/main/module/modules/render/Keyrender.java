/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender2D;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.HUD;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import heaven.main.ui.gui.hud.minekey.Key;
import heaven.main.ui.gui.hud.minekey.MouseButton;
import heaven.main.ui.gui.hud.minekey.SimpleWhiteKey;
import heaven.main.ui.gui.hud.minekey.SimpleWhiteMouseButton;
import heaven.main.ui.simplecore.SimpleRender;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.awt.Color;
import java.awt.Toolkit;

public class Keyrender
extends Module {
    private final SimpleWhiteKey[] movementSimpleWhiteKeys = new SimpleWhiteKey[4];
    private final SimpleWhiteMouseButton[] simpleWhiteMouseButtons = new SimpleWhiteMouseButton[2];
    private final Key[] movementKeys = new Key[4];
    private final MouseButton[] mouseButtons = new MouseButton[2];
    private static final Mode<String> ui = new Mode("UI", new String[]{"Normal", "SimpleWhite"}, "SimpleWhite");
    private final Numbers<Double> X = new Numbers<Double>("X", 5.0, 0.0, Toolkit.getDefaultToolkit().getScreenSize().getWidth(), 10.0);
    private final Numbers<Double> Y = new Numbers<Double>("Y", 150.0, 0.0, Toolkit.getDefaultToolkit().getScreenSize().getHeight(), 10.0);
    private static final Option<Boolean> rainbow = new Option<Boolean>("Rainbow", false, () -> ui.is("Normal"));
    private static final Option<Boolean> THUDColor = new Option<Boolean>("HUDColor", true, () -> ui.is("Normal"));
    private static final Numbers<Double> r = new Numbers<Double>("Red", 120.0, 0.0, 255.0, 5.0, () -> (Boolean)rainbow.getValue() == false, () -> (Boolean)THUDColor.getValue() == false, () -> ui.is("Normal"));
    private static final Numbers<Double> g = new Numbers<Double>("Green", 120.0, 0.0, 255.0, 5.0, () -> (Boolean)rainbow.getValue() == false, () -> (Boolean)THUDColor.getValue() == false, () -> ui.is("Normal"));
    private static final Numbers<Double> b = new Numbers<Double>("Blue", 255.0, 0.0, 255.0, 5.0, () -> (Boolean)rainbow.getValue() == false, () -> (Boolean)THUDColor.getValue() == false, () -> ui.is("Normal"));
    private static final Option<Boolean> Mouses = new Option<Boolean>("MouseButton", true);
    public static final Option<Boolean> LR = new Option<Boolean>("SimpleLR", false, () -> ui.is("SimpleWhite"));
    int textColor;
    float x;
    float y;

    public Keyrender() {
        super("KeyStrokes", ModuleType.Render);
        this.addValues(ui, this.X, this.Y, Mouses, r, g, b, rainbow, THUDColor, LR);
        this.movementSimpleWhiteKeys[0] = new SimpleWhiteKey(Keyrender.mc.gameSettings.keyBindForward, 26, 2);
        this.movementSimpleWhiteKeys[1] = new SimpleWhiteKey(Keyrender.mc.gameSettings.keyBindLeft, 2, 26);
        this.movementSimpleWhiteKeys[2] = new SimpleWhiteKey(Keyrender.mc.gameSettings.keyBindBack, 26, 26);
        this.movementSimpleWhiteKeys[3] = new SimpleWhiteKey(Keyrender.mc.gameSettings.keyBindRight, 50, 26);
        this.simpleWhiteMouseButtons[0] = new SimpleWhiteMouseButton(0, 2);
        this.simpleWhiteMouseButtons[1] = new SimpleWhiteMouseButton(1, 38);
        this.movementKeys[0] = new Key(Keyrender.mc.gameSettings.keyBindForward, 26, 2);
        this.movementKeys[1] = new Key(Keyrender.mc.gameSettings.keyBindLeft, 2, 26);
        this.movementKeys[2] = new Key(Keyrender.mc.gameSettings.keyBindBack, 26, 26);
        this.movementKeys[3] = new Key(Keyrender.mc.gameSettings.keyBindRight, 50, 26);
        this.mouseButtons[0] = new MouseButton(0, 2);
        this.mouseButtons[1] = new MouseButton(1, 38);
    }

    @Override
    public void onDisable() {
        this.y = 0.0f;
        this.x = 0.0f;
    }

    @EventHandler
    public void renderKeystrokes(EventRender2D e) {
        if (this.x != ((Double)this.X.get()).floatValue()) {
            this.x = AnimationUtil.moveUD(this.x, ((Double)this.X.get()).floatValue(), SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
        }
        if (this.y != ((Double)this.Y.get()).floatValue()) {
            this.y = AnimationUtil.moveUD(this.y, ((Double)this.Y.get()).floatValue(), SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
        }
        if (ui.is("Normal")) {
            this.textColor = (Boolean)rainbow.getValue() != false ? Keyrender.getColor() : Keyrender.Color();
        }
        this.drawMovementKeys(this.x, this.y);
        if (((Boolean)Mouses.getValue()).booleanValue()) {
            this.drawMouseButtons(this.x, this.y);
        }
    }

    private static int Color() {
        if (((Boolean)THUDColor.getValue()).booleanValue()) {
            return new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()).getRGB();
        }
        return new Color(((Double)r.getValue()).intValue(), ((Double)g.getValue()).intValue(), ((Double)b.getValue()).intValue()).getRGB();
    }

    private static int getColor() {
        return Color.HSBtoRGB((float)(System.currentTimeMillis() % 1000L) / 1000.0f, 0.8f, 0.8f);
    }

    private void drawMovementKeys(float x, float y) {
        if (ui.is("SimpleWhite")) {
            for (SimpleWhiteKey simpleWhiteKey : this.movementSimpleWhiteKeys) {
                simpleWhiteKey.renderKey(x, y);
            }
        } else {
            for (Key key : this.movementKeys) {
                key.renderKey(x, y, this.textColor);
            }
        }
    }

    private void drawMouseButtons(float x, float y) {
        if (ui.is("SimpleWhite")) {
            for (SimpleWhiteMouseButton button : this.simpleWhiteMouseButtons) {
                button.renderMouseButton(x, y);
            }
        } else {
            for (MouseButton button : this.mouseButtons) {
                button.renderMouseButton(x, y, this.textColor);
            }
        }
    }
}

