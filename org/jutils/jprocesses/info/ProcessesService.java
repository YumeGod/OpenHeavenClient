/*
 * Decompiled with CFR 0.152.
 */
package org.jutils.jprocesses.info;

import java.util.List;
import org.jutils.jprocesses.model.JProcessesResponse;
import org.jutils.jprocesses.model.ProcessInfo;

public interface ProcessesService {
    public List<ProcessInfo> getList();

    public List<ProcessInfo> getList(boolean var1);

    public List<ProcessInfo> getList(String var1);

    public List<ProcessInfo> getList(String var1, boolean var2);

    public ProcessInfo getProcess(int var1);

    public ProcessInfo getProcess(int var1, boolean var2);

    public JProcessesResponse killProcess(int var1);

    public JProcessesResponse killProcessGracefully(int var1);

    public JProcessesResponse changePriority(int var1, int var2);
}

