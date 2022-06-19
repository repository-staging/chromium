// Copyright 2023 GrapheneOS
// Use of this source code is governed by a GPLv2-style license that can be
// found in the LICENSE file.

package org.chromium.chrome.browser;

import android.content.Context;
import android.provider.Browser;
import android.content.Intent;

import androidx.annotation.NonNull;

import org.chromium.base.IntentUtils;
import org.chromium.base.ThreadUtils;
import org.chromium.base.shared_preferences.SharedPrefsUtils.SharedPrefsExt;
import org.chromium.chrome.browser.document.ChromeLauncherActivity;
import org.chromium.chrome.browser.preferences.Pref;
import org.chromium.chrome.browser.profiles.Profile;
import org.chromium.chrome.browser.tab.TabLaunchType;
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

    public static boolean shouldOpenLinksInIncognito() {
        return SharedPrefsExt.OPEN_LINKS_IN_INCOGNITO.get();
    }

    static Intent createIncognitoIntent(Context context, @NonNull Intent intent) {
        Intent newIntent = new Intent();
        newIntent.setAction(intent.getAction());
        newIntent.setData(intent.getData());
        newIntent.putExtras(intent);
        newIntent.setClass(context, ChromeLauncherActivity.class);
        newIntent.putExtra(Browser.EXTRA_CREATE_NEW_TAB, true);
        IntentHandler.setTabLaunchType(newIntent, TabLaunchType.FROM_EXTERNAL_APP);
        appendNeededIncognitoExtras(context, newIntent);
        return newIntent;
    }

    public static Intent appendNeededIncognitoExtras(Context context, @NonNull Intent intent) {
        intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
        intent.putExtra(IntentHandler.EXTRA_OPEN_NEW_INCOGNITO_TAB, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        IntentUtils.addTrustedIntentExtras(intent);
        return intent;
    }
}
