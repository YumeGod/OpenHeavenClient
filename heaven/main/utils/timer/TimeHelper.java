/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.utils.timer;

public class TimeHelper {
    public long lastMs = 0L;

    public boolean isDelayComplete(long delay) {
        return System.currentTimeMillis() - this.lastMs > delay;
    }

    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public void reset() {
        this.lastMs = System.currentTimeMillis();
    }

    public long getLastMs() {
        return this.lastMs;
    }

    public void setLastMs(int i) {
        this.lastMs = System.currentTimeMillis() + (long)i;
    }

    public boolean hasReached(long milliseconds) {
        return this.getCurrentMS() - this.lastMs >= milliseconds;
    }

    public boolean hasReached(float timeLeft) {
        return (float)(this.getCurrentMS() - this.lastMs) >= timeLeft;
    }

    public boolean delay(float nextDelay, boolean reset) {
        if ((float)(System.currentTimeMillis() - this.lastMs) >= nextDelay) {
            if (reset) {
                this.reset();
            }
            return true;
        }
        return false;
    }

    public boolean delay(double nextDelay) {
        return (double)(System.currentTimeMillis() - this.lastMs) >= nextDelay;
    }

    public boolean check(float milliseconds) {
        return (float)this.getTime() >= milliseconds;
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public long getTime() {
        return TimeHelper.getCurrentTime() - this.lastMs;
    }
}

