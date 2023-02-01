/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.ui.gui.clickgui;

import heaven.main.ui.simplecore.SimpleRender;
import heaven.main.utils.timer.TimerUtils;
import java.awt.Color;

public class AnimationUtil {
    public static float moveUD(float current, float end, float smoothSpeed, float minSpeed) {
        float movement = (end - current) * smoothSpeed;
        if (movement > 0.0f) {
            movement = Math.max(minSpeed, movement);
            movement = Math.min(end - current, movement);
        } else if (movement < 0.0f) {
            movement = Math.min(-minSpeed, movement);
            movement = Math.max(end - current, movement);
        }
        return current + movement;
    }

    public static float getAnimationState2(float animation, float finalState, float speed) {
        float add = (float)(SimpleRender.delta * (double)(speed / 1000.0f));
        animation = animation < finalState ? (animation + add < finalState ? (animation += add) : finalState) : (animation - add > finalState ? (animation -= add) : finalState);
        return animation;
    }

    public static int moveUDl(float current, float end, float smoothSpeed, float minSpeed) {
        float movement = (end - current) * smoothSpeed;
        if (movement > 0.0f) {
            movement = Math.max(minSpeed, movement);
            movement = Math.min(end - current, movement);
        } else if (movement < 0.0f) {
            movement = Math.min(-minSpeed, movement);
            movement = Math.max(end - current, movement);
        }
        return (int)(current + movement);
    }

    public static float calculateCompensation(float target, float current, float f, float g) {
        float diff = current - target;
        if (f < 1.0f) {
            f = 1.0f;
        }
        if (diff > g) {
            double xD = (double)(g * f / 16.0f) < 0.25 ? 0.5 : (double)(g * f / 16.0f);
            if ((current -= (float)xD) < target) {
                current = target;
            }
        } else if (diff < -g) {
            double xD = (double)(g * f / 16.0f) < 0.25 ? 0.5 : (double)(g * f / 16.0f);
            if ((current += (float)xD) > target) {
                current = target;
            }
        } else {
            current = target;
        }
        return current;
    }

    public static float calculateCompensation(float target, float current, long delta, int speed) {
        float diff = current - target;
        if (delta < 1L) {
            delta = 1L;
        }
        if (diff > (float)speed) {
            double xD = (double)((long)speed * delta / 16L) < 0.25 ? 0.5 : (double)((long)speed * delta / 16L);
            if ((current -= (float)xD) < target) {
                current = target;
            }
        } else if (diff < (float)(-speed)) {
            double xD = (double)((long)speed * delta / 16L) < 0.25 ? 0.5 : (double)((long)speed * delta / 16L);
            if ((current += (float)xD) > target) {
                current = target;
            }
        } else {
            current = target;
        }
        return current;
    }

    public static double getAnimationState(double animation, double finalState, double speed) {
        float add = (float)(0.01 * speed);
        animation = animation < finalState ? Math.min(animation + (double)add, finalState) : Math.max(animation - (double)add, finalState);
        return animation;
    }

    public static float getAnimationState(float animation, float finalState, float speed) {
        float add = (float)(0.01 * (double)speed);
        animation = animation < finalState ? Math.min(animation + add, finalState) : Math.max(animation - add, finalState);
        return animation;
    }

    public static int animatel(float target, float current, float speed) {
        if (TimerUtils.delay(4.0f)) {
            boolean larger;
            boolean bl = larger = target > current;
            if (speed < 0.0f) {
                speed = 0.0f;
            } else if ((double)speed > 1.0) {
                speed = 1.0f;
            }
            float dif = Math.max(target, current) - Math.min(target, current);
            float factor = dif * speed;
            if (factor < 0.1f) {
                factor = 0.1f;
            }
            current = larger ? current + factor : current - factor;
            TimerUtils.reset();
        }
        if ((double)Math.abs(current - target) < 0.2) {
            return (int)target;
        }
        return (int)current;
    }

    public static float animate(float target, float current, float speed) {
        if (TimerUtils.delay(4.0f)) {
            boolean larger;
            boolean bl = larger = target > current;
            if (speed < 0.0f) {
                speed = 0.0f;
            } else if ((double)speed > 1.0) {
                speed = 1.0f;
            }
            float dif = Math.max(target, current) - Math.min(target, current);
            float factor = dif * speed;
            if (factor < 0.1f) {
                factor = 0.1f;
            }
            current = larger ? current + factor : current - factor;
            TimerUtils.reset();
        }
        if ((double)Math.abs(current - target) < 0.2) {
            return target;
        }
        return current;
    }

    public static double animate(double target, double current, double speed) {
        boolean larger;
        boolean bl = larger = target > current;
        if (speed < 0.0) {
            speed = 0.0;
        } else if (speed > 1.0) {
            speed = 1.0;
        }
        if (target == current) {
            return target;
        }
        double dif = Math.max(target, current) - Math.min(target, current);
        double factor = Math.max(dif * speed, 1.0);
        if (factor < 0.1) {
            factor = 0.1;
        }
        current = larger ? (current + factor > target ? target : (current += factor)) : (current - factor < target ? target : (current -= factor));
        return current;
    }

    public static Color getColorAnimationState(Color animation, Color finalState, double speed) {
        float add = (float)(0.01 * speed);
        float animationr = animation.getRed();
        float animationg = animation.getGreen();
        float animationb = animation.getBlue();
        float finalStater = finalState.getRed();
        float finalStateg = finalState.getGreen();
        float finalStateb = finalState.getBlue();
        float finalStatea = finalState.getAlpha();
        animationr = animationr < finalStater ? (animationr + add < finalStater ? (animationr += add) : finalStater) : (animationr - add > finalStater ? (animationr -= add) : finalStater);
        animationg = animationg < finalStateg ? (animationg + add < finalStateg ? (animationg += add) : finalStateg) : (animationg - add > finalStateg ? (animationg -= add) : finalStateg);
        animationb = animationb < finalStateb ? (animationb + add < finalStateb ? (animationb += add) : finalStateb) : (animationb - add > finalStateb ? (animationb -= add) : finalStateb);
        animationr /= 255.0f;
        animationg /= 255.0f;
        animationb /= 255.0f;
        finalStatea /= 255.0f;
        if (animationr > 1.0f) {
            animationr = 1.0f;
        }
        if (animationg > 1.0f) {
            animationg = 1.0f;
        }
        if (animationb > 1.0f) {
            animationb = 1.0f;
        }
        if (finalStatea > 1.0f) {
            finalStatea = 1.0f;
        }
        return new Color(animationr, animationg, animationb, finalStatea);
    }

    public static float clamp(float number, float min, float max) {
        return number < min ? min : Math.min(number, max);
    }
}

