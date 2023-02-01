/*
 * Decompiled with CFR 0.152.
 */
package com.profesorfalken.jpowershell;

import com.profesorfalken.jpowershell.PowerShellResponse;

@FunctionalInterface
interface PowerShellResponseHandler {
    public void handle(PowerShellResponse var1);
}

