/*
 * Decompiled with CFR 0.152.
 */
package org.jutils.jprocesses.info;

import org.jutils.jprocesses.info.ProcessesService;
import org.jutils.jprocesses.info.UnixProcessesService;
import org.jutils.jprocesses.info.WindowsProcessesService;
import org.jutils.jprocesses.util.OSDetector;

public class ProcessesFactory {
    private ProcessesFactory() {
    }

    public static ProcessesService getService() {
        if (OSDetector.isWindows()) {
            return new WindowsProcessesService();
        }
        if (OSDetector.isUnix()) {
            return new UnixProcessesService();
        }
        throw new UnsupportedOperationException("Your Operating System is not supported by this library.");
    }
}

