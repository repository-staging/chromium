package org.chromium.base.library_loader;

import android.content.Context;

import org.chromium.base.CommandLine;
import org.chromium.base.command_line.VanadiumCommandLineUtils;
import org.chromium.base.config.VanadiumConfigBridge;

import java.util.ArrayList;
import java.util.HashSet;

import app.vanadium.ext.CustomOSApis;

class LibraryLoaderHooks {

    static void onSetLibraryProcessType(Context appContext, @LibraryProcessType int type) {
        VanadiumConfigBridge.initConfigs(appContext, type);
    }

    static void onBeforeCommandLineSwitchLocked(Context appContext, @LibraryProcessType int type) {
        VanadiumConfigBridge.applyFlagFromConfigs(appContext, type);
        switch (type) {
            case LibraryProcessType.PROCESS_BROWSER:
            case LibraryProcessType.PROCESS_WEBVIEW:
            case LibraryProcessType.PROCESS_WEBVIEW_NONEMBEDDED:
                return;
            case LibraryProcessType.PROCESS_CHILD:
            case LibraryProcessType.PROCESS_WEBVIEW_CHILD:
                if (CommandLine.getInstance() != null) {
                    boolean checkForOSConfigForJitless =
                            !VanadiumCommandLineUtils.getCommaDelimitedSwitchValue(
                                    "disable-features").contains("check-os-config-for-jitless");
                    if (CustomOSApis.isExecmemBlocked() && checkForOSConfigForJitless) {
                        String jsFlagsName = "js-flags";
                        HashSet<String> jsFlags = new HashSet<>(
                                VanadiumCommandLineUtils.getCommaDelimitedSwitchValue(jsFlagsName));
                        jsFlags.add("--jitless");
                        VanadiumCommandLineUtils.setCommaDelimitedSwitchValue(
                                jsFlagsName, new ArrayList<>(jsFlags));
                    }
                }
                break;
            default:
                throw new IllegalStateException();
        }
    }

    static void onSubsequentCommandLineSwitchLockedCheck(Context appContext, @LibraryProcessType int type) {
        VanadiumConfigBridge.applyFlagFromConfigs(appContext, type);
    }
}
