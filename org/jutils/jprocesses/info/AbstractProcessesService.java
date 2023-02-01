/*
 * Decompiled with CFR 0.152.
 */
package org.jutils.jprocesses.info;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jutils.jprocesses.info.ProcessesService;
import org.jutils.jprocesses.model.JProcessesResponse;
import org.jutils.jprocesses.model.ProcessInfo;

abstract class AbstractProcessesService
implements ProcessesService {
    protected boolean fastMode = false;

    AbstractProcessesService() {
    }

    @Override
    public List<ProcessInfo> getList() {
        return this.getList(null);
    }

    @Override
    public List<ProcessInfo> getList(boolean fastMode) {
        return this.getList(null, fastMode);
    }

    @Override
    public List<ProcessInfo> getList(String name) {
        return this.getList(name, false);
    }

    @Override
    public List<ProcessInfo> getList(String name, boolean fastMode) {
        this.fastMode = fastMode;
        String rawData = this.getProcessesData(name);
        List<Map<String, String>> mapList = this.parseList(rawData);
        return this.buildInfoFromMap(mapList);
    }

    @Override
    public JProcessesResponse killProcess(int pid) {
        return this.kill(pid);
    }

    @Override
    public JProcessesResponse killProcessGracefully(int pid) {
        return this.killGracefully(pid);
    }

    protected abstract List<Map<String, String>> parseList(String var1);

    protected abstract String getProcessesData(String var1);

    protected abstract JProcessesResponse kill(int var1);

    protected abstract JProcessesResponse killGracefully(int var1);

    private List<ProcessInfo> buildInfoFromMap(List<Map<String, String>> mapList) {
        ArrayList<ProcessInfo> infoList = new ArrayList<ProcessInfo>();
        for (Map<String, String> map : mapList) {
            ProcessInfo info = new ProcessInfo();
            info.setPid(map.get("pid"));
            info.setName(map.get("proc_name"));
            info.setTime(map.get("proc_time"));
            info.setCommand(map.get("command") != null ? map.get("command") : "");
            info.setCpuUsage(map.get("cpu_usage"));
            info.setPhysicalMemory(map.get("physical_memory"));
            info.setStartTime(map.get("start_time"));
            info.setUser(map.get("user"));
            info.setVirtualMemory(map.get("virtual_memory"));
            info.setPriority(map.get("priority"));
            info.setExtraData(map);
            infoList.add(info);
        }
        return infoList;
    }
}

