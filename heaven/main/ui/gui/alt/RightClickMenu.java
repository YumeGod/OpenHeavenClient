/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.ui.gui.alt;

import heaven.main.ui.gui.alt.PressEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class RightClickMenu {
    protected int x;
    protected int y;
    protected final List<RightClickMenuButton> buttons = new CopyOnWriteArrayList<RightClickMenuButton>();

    public RightClickMenu(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void onOpen();

    public abstract void draw(int var1, int var2);

    protected static class RightClickMenuButton {
        private final String buttonText;
        private final PressEvent pressEvent;

        public RightClickMenuButton(String buttonText, PressEvent pressEvent) {
            this.buttonText = buttonText;
            this.pressEvent = pressEvent;
        }

        public void onPress() {
            this.pressEvent.pressed();
        }

        public String getButtonText() {
            return this.buttonText;
        }
    }
}

