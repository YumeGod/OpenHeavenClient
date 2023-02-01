/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.utils.timer;

public class TimerUtil {
    private long lastMS;
    private static final long time = -1L;
    private final long ms = TimerUtil.getCurrentMS();
    public long lastMs;

    private static long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public boolean hasReached(double milliseconds) {
        return (double)(TimerUtil.getCurrentMS() - this.lastMS) >= milliseconds;
    }

    public long getLastMS() {
        return this.lastMS;
    }

    public void reset() {
        this.lastMS = TimerUtil.getCurrentMS();
    }

    public boolean delay(float milliSec) {
        return (float)(TimerUtil.getTime() - this.lastMS) >= milliSec;
    }

    public boolean delay(double nextDelay) {
        return (double)(System.currentTimeMillis() - this.lastMs) >= nextDelay;
    }

    public static long getTime() {
        return System.nanoTime() / 1000000L;
    }

    public boolean isDelayComplete(long delay) {
        return System.currentTimeMillis() - this.lastMS > delay;
    }

    public boolean isDelayComplete(double delay) {
        return (double)(System.currentTimeMillis() - this.lastMS) > delay;
    }

    public static boolean hasTimePassed(long MS) {
        return System.currentTimeMillis() >= -1L + MS;
    }

    public final boolean elapsed(long milliseconds) {
        return TimerUtil.getCurrentMS() - this.ms > milliseconds;
    }

    public final boolean hasPassed(long milliseconds) {
        return TimerUtil.getCurrentMS() - this.lastMS > milliseconds;
    }

    public long getLastMs() {
        return this.lastMs;
    }

    public final long getElapsedTime() {
        return TimerUtil.getCurrentMS() - this.lastMS;
    }

    public long time() {
        return System.nanoTime() / 1000000L - -1L;
    }

    public boolean sleep(long time) {
        if (this.time() >= time) {
            this.reset();
            return true;
        }
        return false;
    }
}

