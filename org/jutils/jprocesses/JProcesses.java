/*
 * Decompiled with CFR 0.152.
 */
package org.jutils.jprocesses;

import java.util.List;
import org.jutils.jprocesses.info.ProcessesFactory;
import org.jutils.jprocesses.info.ProcessesService;
import org.jutils.jprocesses.model.JProcessesResponse;
import org.jutils.jprocesses.model.ProcessInfo;

public class JProcesses {
    private boolean fastMode = false;

    private JProcesses() {
    }

    public static JProcesses get() {
        return new JProcesses();
    }

    public JProcesses fastMode() {
        this.fastMode = true;
        return this;
    }

    public JProcesses fastMode(boolean enabled) {
        this.fastMode = enabled;
        return this;
    }

    public List<ProcessInfo> listProcesses() {
        return JProcesses.getService().getList(this.fastMode);
    }

    public List<ProcessInfo> listProcesses(String name) {
        return JProcesses.getService().getList(name, this.fastMode);
    }

    public static List<ProcessInfo> getProcessList() {
        return JProcesses.getService().getList();
    }

    public static List<ProcessInfo> getProcessList(String name) {
        return JProcesses.getService().getList(name);
    }

    public static ProcessInfo getProcess(int pid) {
        return JProcesses.getService().getProcess(pid);
    }

    public static JProcessesResponse killProcess(int pid) {
        return JProcesses.getService().killProcess(pid);
    }

    public static JProcessesResponse killProcessGracefully(int pid) {
        return JProcesses.getService().killProcessGracefully(pid);
    }

    public static JProcessesResponse changePriority(int pid, int newPriority) {
        return JProcesses.getService().changePriority(pid, newPriority);
    }

    private static ProcessesService getService() {
        return ProcessesFactory.getService();
    }
}

