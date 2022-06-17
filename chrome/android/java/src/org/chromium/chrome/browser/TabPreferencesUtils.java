// Copyright 2023 GrapheneOS
// Use of this source code is governed by a GPLv2-style license that can be
// found in the LICENSE file.

package org.chromium.chrome.browser;

import org.chromium.base.ThreadUtils;
import org.chromium.base.shared_preferences.SharedPrefsUtils.SharedPrefsExt;
import org.chromium.chrome.browser.preferences.Pref;
import org.chromium.chrome.browser.profiles.Profile;
import org.chromium.components.prefs.PrefService;
import org.chromium.components.user_prefs.UserPrefs;

/*
 * Utility class for modifying tab-related browsing behaviors
 * based on user preferences.
 */
public final class TabPreferencesUtils {

    public static boolean shouldCloseTabsOnExit() {
        return SharedPrefsExt.CLOSE_TABS_ON_EXIT.get();
    }

}
