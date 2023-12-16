// Copyright 2023 GrapheneOS
// Use of this source code is governed by a GPLv2-style license that can be
// found in the LICENSE file.

package org.chromium.chrome.browser.privacy.settings;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import org.chromium.base.ThreadUtils;
import org.chromium.base.shared_preferences.SharedPrefsUtils.SharedPrefsExt;
import org.chromium.chrome.R;
import org.chromium.chrome.browser.flags.ChromeFeatureList;
import org.chromium.chrome.browser.preferences.Pref;
import org.chromium.chrome.browser.profiles.Profile;
import org.chromium.chrome.browser.settings.ChromeManagedPreferenceDelegate;
import org.chromium.chrome.browser.settings.SettingsExtUtils;
import org.chromium.components.browser_ui.settings.ChromeBasePreference;
import org.chromium.components.browser_ui.settings.ChromeSwitchPreference;
import org.chromium.components.browser_ui.settings.SettingsUtils;
import org.chromium.components.prefs.PrefService;
import org.chromium.components.user_prefs.UserPrefs;

final class PrivacySettingsExt {

    private static final Preference.OnPreferenceChangeListener getListener(@NonNull Profile profile) {
        return (pref, val) -> {
            PrefService prefService = UserPrefs.get(profile);
            if (pref == null) {
                return false;
            }
            String key = pref.getKey();
            return true;
        };
    }

    private static final ChromeManagedPreferenceDelegate getDelegate(@NonNull Profile profile) {
        return new ChromeManagedPreferenceDelegate(profile) {
            @Override
            public boolean isPreferenceControlledByPolicy(Preference pref) {
                PrefService prefService = UserPrefs.get(profile);
                if (pref == null) {
                    return false;
                }
                String key = pref.getKey();
                return false;
            }
        };
    }

    static void removeUnwantedPreferences(@NonNull PreferenceFragmentCompat prefFragment) {
        ThreadUtils.checkUiThread();
        SettingsExtUtils.safelyRemovePreference(
                prefFragment, PrivacySettings.PREF_SYNC_AND_SERVICES_LINK);
        SettingsExtUtils.safelyRemovePreference(
                prefFragment, PrivacySettings.PREF_PRIVACY_SANDBOX);
        SettingsExtUtils.safelyRemovePreference(
                prefFragment, PrivacySettings.PREF_PRIVACY_GUIDE);
    }

    static void initializePreferences(@NonNull PreferenceFragmentCompat prefFragment, @NonNull Profile profile) {
        ThreadUtils.checkUiThread();
        // This is such that privacy preferences are added at Privacy Section in newer UI,
        // and mostly kept on previous order at older UI.
        int PRIVACY_PREFERENCES_ORDER =
                ChromeFeatureList.isEnabled(ChromeFeatureList.PRIVACY_SANDBOX_SETTINGS_4) ? 2 : 6;
        int SECURITY_PREFERENCES_ORDER =
                ChromeFeatureList.isEnabled(ChromeFeatureList.PRIVACY_SANDBOX_SETTINGS_4) ? 2 : 9999;
        SettingsUtils.addPreferencesFromResource(prefFragment, R.xml.privacy_preferences_ext);
    }

    static void updatePreferences(@NonNull PreferenceFragmentCompat prefFragment, @NonNull Profile profile) {
        ThreadUtils.checkUiThread();
        PrefService prefService = UserPrefs.get(profile);
    }
}
