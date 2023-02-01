/*
 * Decompiled with CFR 0.152.
 */
package com.me.guichaguri.betterfps.math;

public class TaylorMath {
    private static final float BF_SIN_TO_COS = 1.5707964f;

    public static float sin(float rad) {
        double x = rad;
        double x2 = x * x;
        double x3 = x2 * x;
        double x5 = x2 * x3;
        double x7 = x2 * x5;
        double x9 = x2 * x7;
        double x11 = x2 * x9;
        double x13 = x2 * x11;
        double x15 = x2 * x13;
        double x17 = x2 * x15;
        double val = x;
        val -= x3 * 0.16666666666666666;
        val += x5 * 0.008333333333333333;
        val -= x7 * 1.984126984126984E-4;
        val += x9 * 2.7557319223985893E-6;
        val -= x11 * 2.505210838544172E-8;
        val += x13 * 1.6059043836821613E-10;
        val -= x15 * 7.647163731819816E-13;
        return (float)(val += x17 * 2.8114572543455206E-15);
    }

    public static float cos(float rad) {
        return TaylorMath.sin(rad + BF_SIN_TO_COS);
    }
}

