/*
 * Decompiled with CFR 0.152.
 */
package heaven.heavenalpha.softwarecheck;

import java.util.List;
import org.jutils.jprocesses.JProcesses;
import org.jutils.jprocesses.model.ProcessInfo;

public class BadProcessUtils {
    private static final String[] badProcesses = new String[]{"fiddler", "wireshark", "eclipse", "intelij"};

    public static boolean isBadProcessRunning() {
        try {
            List<ProcessInfo> processesList = JProcesses.getProcessList();
            for (ProcessInfo processInfo : processesList) {
                for (String str : badProcesses) {
                    if (!processInfo.getName().toLowerCase().contains(str)) continue;
                    return true;
                }
            }
        }
        catch (Exception e) {
            return true;
        }
        return false;
    }
}

