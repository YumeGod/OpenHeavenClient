/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.utils.math;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.util.MathHelper;

public class MathUtil {
    public static final Random random = new Random();

    public static double randomFloat(float min, float max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    public static double round(double in, int places) {
        places = (int)MathHelper.clamp_double(places, 0.0, 2.147483647E9);
        return Double.parseDouble(String.format("%." + places + "f", in));
    }

    public static double roundOther(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static float getRandomInRange(float min, float max) {
        Random random = new Random();
        return random.nextFloat() * (max - min) + min;
    }

    public static boolean parsable(String s, byte type) {
        try {
            switch (type) {
                case 0: {
                    Short.parseShort(s);
                    break;
                }
                case 1: {
                    Byte.parseByte(s);
                    break;
                }
                case 2: {
                    Integer.parseInt(s);
                    break;
                }
                case 3: {
                    Float.parseFloat(s);
                    break;
                }
                case 4: {
                    Double.parseDouble(s);
                    break;
                }
                case 5: {
                    Long.parseLong(s);
                }
            }
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static double square(double in) {
        return in * in;
    }

    public static double randomDouble(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    public static double randomNumber(double max, double min) {
        return Math.random() * (max - min) + min;
    }

    public static int getNextPostion(int position, int toPosition, double count) {
        int pos = position;
        if (pos < toPosition) {
            int speed = (int)((double)(toPosition - pos) / count);
            if (speed < 1) {
                speed = 1;
            }
            pos += speed;
        } else if (pos > toPosition) {
            int speed = (int)((double)(pos - toPosition) / count);
            if (speed < 1) {
                speed = 1;
            }
            pos -= speed;
        }
        return pos;
    }

    public static double random(double max, double min) {
        return Math.random() * (max - min) + min;
    }

    public static double randomMis(double min, double max) {
        Random random = new Random();
        return min + random.nextDouble() * (max - min);
    }

    public static float getRandom() {
        Random random = new Random();
        return random.nextFloat();
    }

    public static double getRandom(double min, double max) {
        double shifted;
        Random random = new Random();
        double range = max - min;
        double scaled = random.nextDouble() * range;
        if (scaled > max) {
            scaled = max;
        }
        if ((shifted = scaled + min) > max) {
            shifted = max;
        }
        return shifted;
    }
}

