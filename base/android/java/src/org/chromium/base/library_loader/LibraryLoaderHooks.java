package org.chromium.base.library_loader;

import android.content.Context;

import org.chromium.base.config.VanadiumConfigBridge;

class LibraryLoaderHooks {

    static void onSetLibraryProcessType(Context appContext, @LibraryProcessType int type) {
        VanadiumConfigBridge.initConfigs(appContext, type);
    }

    static void onBeforeCommandLineSwitchLocked(Context appContext, @LibraryProcessType int type) {
        VanadiumConfigBridge.applyFlagFromConfigs(appContext, type);
    }

    static void onSubsequentCommandLineSwitchLockedCheck(Context appContext, @LibraryProcessType int type) {
        VanadiumConfigBridge.applyFlagFromConfigs(appContext, type);
    }
}
