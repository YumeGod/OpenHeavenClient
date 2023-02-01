/*
 * Decompiled with CFR 0.152.
 */
package org.jutils.jprocesses.info;

import com.profesorfalken.wmi4java.WMI4Java;
import com.profesorfalken.wmi4java.WMIClass;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jutils.jprocesses.info.AbstractProcessesService;
import org.jutils.jprocesses.info.VBScriptHelper;
import org.jutils.jprocesses.model.JProcessesResponse;
import org.jutils.jprocesses.model.ProcessInfo;
import org.jutils.jprocesses.util.ProcessesUtils;

class WindowsProcessesService
extends AbstractProcessesService {
    private final Map<String, String> userData = new HashMap<String, String>();
    private final Map<String, String> cpuData = new HashMap<String, String>();
    private static final String LINE_BREAK_REGEX = "\\r?\\n";
    private static final Map<String, String> keyMap;
    private Map<String, String> processMap;
    private static final String NAME_PROPNAME = "Name";
    private static final String PROCESSID_PROPNAME = "ProcessId";
    private static final String USERMODETIME_PROPNAME = "UserModeTime";
    private static final String PRIORITY_PROPNAME = "Priority";
    private static final String VIRTUALSIZE_PROPNAME = "VirtualSize";
    private static final String WORKINGSETSIZE_PROPNAME = "WorkingSetSize";
    private static final String COMMANDLINE_PROPNAME = "CommandLine";
    private static final String CREATIONDATE_PROPNAME = "CreationDate";
    private static final String CAPTION_PROPNAME = "Caption";
    private final WMI4Java wmi4Java;

    public WindowsProcessesService() {
        this(null);
    }

    WindowsProcessesService(WMI4Java wmi4Java) {
        this.wmi4Java = wmi4Java;
    }

    public WMI4Java getWmi4Java() {
        if (this.wmi4Java == null) {
            return WMI4Java.get();
        }
        return this.wmi4Java;
    }

    @Override
    protected List<Map<String, String>> parseList(String rawData) {
        String[] dataStringLines;
        ArrayList<Map<String, String>> processesDataList = new ArrayList<Map<String, String>>();
        for (String dataLine : dataStringLines = rawData.split(LINE_BREAK_REGEX)) {
            if (dataLine.trim().length() <= 0) continue;
            this.processLine(dataLine, processesDataList);
        }
        return processesDataList;
    }

    private void processLine(String dataLine, List<Map<String, String>> processesDataList) {
        if (dataLine.startsWith(CAPTION_PROPNAME)) {
            this.processMap = new HashMap<String, String>();
            processesDataList.add(this.processMap);
        }
        if (this.processMap != null) {
            String[] dataStringInfo = dataLine.split(":", 2);
            this.processMap.put(WindowsProcessesService.normalizeKey(dataStringInfo[0].trim()), WindowsProcessesService.normalizeValue(dataStringInfo[0].trim(), dataStringInfo[1].trim()));
            if (PROCESSID_PROPNAME.equals(dataStringInfo[0].trim())) {
                this.processMap.put("user", this.userData.get(dataStringInfo[1].trim()));
                this.processMap.put("cpu_usage", this.cpuData.get(dataStringInfo[1].trim()));
            }
            if (CREATIONDATE_PROPNAME.equals(dataStringInfo[0].trim())) {
                this.processMap.put("start_datetime", ProcessesUtils.parseWindowsDateTimeToFullDate(dataStringInfo[1].trim()));
            }
        }
    }

    @Override
    protected String getProcessesData(String name) {
        if (!this.fastMode) {
            this.fillExtraProcessData();
        }
        if (name != null) {
            return this.getWmi4Java().VBSEngine().properties(Arrays.asList(CAPTION_PROPNAME, PROCESSID_PROPNAME, NAME_PROPNAME, USERMODETIME_PROPNAME, COMMANDLINE_PROPNAME, WORKINGSETSIZE_PROPNAME, CREATIONDATE_PROPNAME, VIRTUALSIZE_PROPNAME, PRIORITY_PROPNAME)).filters(Collections.singletonList("Name like '%" + name + "%'")).getRawWMIObjectOutput(WMIClass.WIN32_PROCESS);
        }
        return this.getWmi4Java().VBSEngine().getRawWMIObjectOutput(WMIClass.WIN32_PROCESS);
    }

    @Override
    protected JProcessesResponse kill(int pid) {
        JProcessesResponse response = new JProcessesResponse();
        if (ProcessesUtils.executeCommandAndGetCode("taskkill", "/PID", String.valueOf(pid), "/F") == 0) {
            response.setSuccess(true);
        }
        return response;
    }

    @Override
    protected JProcessesResponse killGracefully(int pid) {
        JProcessesResponse response = new JProcessesResponse();
        if (ProcessesUtils.executeCommandAndGetCode("taskkill", "/PID", String.valueOf(pid)) == 0) {
            response.setSuccess(true);
        }
        return response;
    }

    private static String normalizeKey(String origKey) {
        return keyMap.get(origKey);
    }

    private static String normalizeValue(String origKey, String origValue) {
        if (USERMODETIME_PROPNAME.equals(origKey)) {
            long seconds = Long.parseLong(origValue) * 100L / 1000000L / 1000L;
            return WindowsProcessesService.nomalizeTime(seconds);
        }
        if ((VIRTUALSIZE_PROPNAME.equals(origKey) || WORKINGSETSIZE_PROPNAME.equals(origKey)) && !origValue.isEmpty()) {
            return String.valueOf(Long.parseLong(origValue) / 1024L);
        }
        if (CREATIONDATE_PROPNAME.equals(origKey)) {
            return ProcessesUtils.parseWindowsDateTimeToSimpleTime(origValue);
        }
        return origValue;
    }

    private static String nomalizeTime(long seconds) {
        long hours = seconds / 3600L;
        long minutes = seconds % 3600L / 60L;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void fillExtraProcessData() {
        String perfData = this.getWmi4Java().VBSEngine().getRawWMIObjectOutput(WMIClass.WIN32_PERFFORMATTEDDATA_PERFPROC_PROCESS);
        String[] dataStringLines = perfData.split(LINE_BREAK_REGEX);
        String pid = null;
        String cpuUsage = null;
        for (String dataLine : dataStringLines) {
            if (dataLine.trim().length() <= 0) continue;
            if (dataLine.startsWith(CAPTION_PROPNAME)) {
                if (pid == null || cpuUsage == null) continue;
                this.cpuData.put(pid, cpuUsage);
                pid = null;
                cpuUsage = null;
                continue;
            }
            if (pid == null) {
                pid = WindowsProcessesService.checkAndGetDataInLine("IDProcess", dataLine);
            }
            if (cpuUsage != null) continue;
            cpuUsage = WindowsProcessesService.checkAndGetDataInLine("PercentProcessorTime", dataLine);
        }
        String processesData = VBScriptHelper.getProcessesOwner();
        if (processesData != null) {
            for (String dataLine : dataStringLines = processesData.split(LINE_BREAK_REGEX)) {
                String[] dataStringInfo = dataLine.split(":", 2);
                if (dataStringInfo.length != 2) continue;
                this.userData.put(dataStringInfo[0].trim(), dataStringInfo[1].trim());
            }
        }
    }

    private static String checkAndGetDataInLine(String dataName, String dataLine) {
        String[] dataStringInfo;
        if (dataLine.startsWith(dataName) && (dataStringInfo = dataLine.split(":")).length == 2) {
            return dataStringInfo[1].trim();
        }
        return null;
    }

    @Override
    public JProcessesResponse changePriority(int pid, int priority) {
        JProcessesResponse response = new JProcessesResponse();
        String message = VBScriptHelper.changePriority(pid, priority);
        if (message == null || message.length() == 0) {
            response.setSuccess(true);
        } else {
            response.setMessage(message);
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
        List<Map<String, String>> allProcesses = this.parseList(this.getProcessesData(null));
        for (Map<String, String> process : allProcesses) {
            if (!String.valueOf(pid).equals(process.get("pid"))) continue;
            ProcessInfo info = new ProcessInfo();
            info.setPid(process.get("pid"));
            info.setName(process.get("proc_name"));
            info.setTime(process.get("proc_time"));
            info.setCommand(process.get("command"));
            info.setCpuUsage(process.get("cpu_usage"));
            info.setPhysicalMemory(process.get("physical_memory"));
            info.setStartTime(process.get("start_time"));
            info.setUser(process.get("user"));
            info.setVirtualMemory(process.get("virtual_memory"));
            info.setPriority(process.get("priority"));
            return info;
        }
        return null;
    }

    static {
        HashMap<String, String> tmpMap = new HashMap<String, String>();
        tmpMap.put(NAME_PROPNAME, "proc_name");
        tmpMap.put(PROCESSID_PROPNAME, "pid");
        tmpMap.put(USERMODETIME_PROPNAME, "proc_time");
        tmpMap.put(PRIORITY_PROPNAME, "priority");
        tmpMap.put(VIRTUALSIZE_PROPNAME, "virtual_memory");
        tmpMap.put(WORKINGSETSIZE_PROPNAME, "physical_memory");
        tmpMap.put(COMMANDLINE_PROPNAME, "command");
        tmpMap.put(CREATIONDATE_PROPNAME, "start_time");
        keyMap = Collections.unmodifiableMap(tmpMap);
    }
}

