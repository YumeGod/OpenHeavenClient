/*
 * Decompiled with CFR 0.152.
 */
package com.profesorfalken.wmi4java;

import com.profesorfalken.wmi4java.WMIException;
import java.util.List;

interface WMIStub {
    public String listClasses(String var1, String var2) throws WMIException;

    public String listObject(String var1, String var2, String var3) throws WMIException;

    public String queryObject(String var1, List<String> var2, List<String> var3, String var4, String var5) throws WMIException;

    public String listProperties(String var1, String var2, String var3) throws WMIException;
}

