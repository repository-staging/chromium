package org.chromium.base.library_loader;

import android.content.Context;

class LibraryLoaderHooks {

    static void onSetLibraryProcessType(Context appContext, @LibraryProcessType int type) {
    }

    static void onBeforeCommandLineSwitchLocked(Context appContext, @LibraryProcessType int type) {
    }

    static void onSubsequentCommandLineSwitchLockedCheck(Context appContext, @LibraryProcessType int type) {
    }
}
