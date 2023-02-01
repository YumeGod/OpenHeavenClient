/*
 * Decompiled with CFR 0.152.
 */
package org.jutils.jprocesses.info;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jutils.jprocesses.info.AbstractProcessesService;
import org.jutils.jprocesses.model.JProcessesResponse;
import org.jutils.jprocesses.model.ProcessInfo;
import org.jutils.jprocesses.util.OSDetector;
import org.jutils.jprocesses.util.ProcessesUtils;

class UnixProcessesService
extends AbstractProcessesService {
    private static final String PS_COLUMNS = "pid,ruser,vsize,rss,%cpu,lstart,cputime,nice,ucomm";
    private static final String PS_FULL_COMMAND = "pid,command";
    private static final int PS_COLUMNS_SIZE = "pid,ruser,vsize,rss,%cpu,lstart,cputime,nice,ucomm".split(",").length;
    private static final int PS_FULL_COMMAND_SIZE = "pid,command".split(",").length;
    private String nameFilter = null;

    UnixProcessesService() {
    }

    @Override
    protected List<Map<String, String>> parseList(String rawData) {
        String[] dataStringLines;
        ArrayList<Map<String, String>> processesDataList = new ArrayList<Map<String, String>>();
        for (String dataLine : dataStringLines = rawData.split("\\r?\\n")) {
            String line = dataLine.trim();
            if (line.startsWith("PID")) continue;
            LinkedHashMap<String, String> element = new LinkedHashMap<String, String>();
            String[] elements = line.split("\\s+", PS_COLUMNS_SIZE + 5);
            int index = 0;
            element.put("pid", elements[index++]);
            element.put("user", elements[index++]);
            element.put("virtual_memory", elements[index++]);
            element.put("physical_memory", elements[index++]);
            element.put("cpu_usage", elements[index++]);
            int n = ++index;
            int n2 = ++index;
            int n3 = ++index;
            int n4 = ++index;
            String longDate = elements[n] + " " + elements[n2] + " " + elements[n3] + " " + elements[n4];
            element.put("start_time", elements[++index - 2]);
            try {
                element.put("start_datetime", ProcessesUtils.parseUnixLongTimeToFullDate(longDate));
            }
            catch (ParseException e) {
                element.put("start_datetime", "01/01/2000 00:00:00");
                System.err.println("Failed formatting date from ps: " + longDate + ", using \"01/01/2000 00:00:00\"");
            }
            element.put("proc_time", elements[index++]);
            element.put("priority", elements[index++]);
            element.put("proc_name", elements[index++]);
            element.put("command", elements[index - 1]);
            processesDataList.add(element);
        }
        UnixProcessesService.loadFullCommandData(processesDataList);
        if (this.nameFilter != null) {
            this.filterByName(processesDataList);
        }
        return processesDataList;
    }

    @Override
    protected String getProcessesData(String name) {
        if (name != null) {
            if (OSDetector.isLinux()) {
                return ProcessesUtils.executeCommand("ps", "-o", PS_COLUMNS, "-C", name);
            }
            this.nameFilter = name;
        }
        return ProcessesUtils.executeCommand("ps", "-e", "-o", PS_COLUMNS);
    }

    @Override
    protected JProcessesResponse kill(int pid) {
        JProcessesResponse response = new JProcessesResponse();
        if (ProcessesUtils.executeCommandAndGetCode("kill", "-9", String.valueOf(pid)) == 0) {
            response.setSuccess(true);
        }
        return response;
    }

    @Override
    protected JProcessesResponse killGracefully(int pid) {
        JProcessesResponse response = new JProcessesResponse();
        if (ProcessesUtils.executeCommandAndGetCode("kill", "-15", String.valueOf(pid)) == 0) {
            response.setSuccess(true);
        }
        return response;
    }

    @Override
    public JProcessesResponse changePriority(int pid, int priority) {
        JProcessesResponse response = new JProcessesResponse();
        if (ProcessesUtils.executeCommandAndGetCode("renice", String.valueOf(priority), "-p", String.valueOf(pid)) == 0) {
            response.setSuccess(true);
        }
        return response;
    }

    @Override
    public ProcessInfo getProcess(int pid) {
        return this.getProcess(pid, false);
    }

    @Override
    public ProcessInfo getProcess(int pid, boolean fastMode) {
        this.fastMode = fastMode;
        List<Map<String, String>> processList = this.parseList(ProcessesUtils.executeCommand("ps", "-o", PS_COLUMNS, "-p", String.valueOf(pid)));
        if (processList != null && !processList.isEmpty()) {
            Map<String, String> processData = processList.get(0);
            ProcessInfo info = new ProcessInfo();
            info.setPid(processData.get("pid"));
            info.setName(processData.get("proc_name"));
            info.setTime(processData.get("proc_time"));
            info.setCommand(processData.get("command"));
            info.setCpuUsage(processData.get("cpu_usage"));
            info.setPhysicalMemory(processData.get("physical_memory"));
            info.setStartTime(processData.get("start_time"));
            info.setUser(processData.get("user"));
            info.setVirtualMemory(processData.get("virtual_memory"));
            info.setPriority(processData.get("priority"));
            return info;
        }
        return null;
    }

    private static void loadFullCommandData(List<Map<String, String>> processesDataList) {
        HashMap<String, String> commandsMap = new HashMap<String, String>();
        String data = ProcessesUtils.executeCommand("ps", "-e", "-o", PS_FULL_COMMAND);
        String[] dataStringLines = data.split("\\r?\\n");
        for (String dataLine : dataStringLines) {
            String[] elements;
            if (dataLine.trim().startsWith("PID") || (elements = dataLine.trim().split("\\s+", PS_FULL_COMMAND_SIZE)).length != PS_FULL_COMMAND_SIZE) continue;
            commandsMap.put(elements[0], elements[1]);
        }
        for (Map map : processesDataList) {
            if (!commandsMap.containsKey(map.get("pid"))) continue;
            map.put("command", commandsMap.get(map.get("pid")));
        }
    }

    private void filterByName(List<Map<String, String>> processesDataList) {
        ArrayList<Map<String, String>> processesToRemove = new ArrayList<Map<String, String>>();
        for (Map<String, String> process : processesDataList) {
            if (this.nameFilter.equals(process.get("proc_name"))) continue;
            processesToRemove.add(process);
        }
        processesDataList.removeAll(processesToRemove);
    }
}

