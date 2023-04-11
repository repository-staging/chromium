// Copyright 2023 GrapheneOS
// Use of this source code is governed by a GPLv2-style license that can be
// found in the LICENSE file.

package org.chromium.chrome.browser.settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import org.chromium.base.ThreadUtils;

public class SettingsExtUtils {

    public static void safelyUpdatePreference(
        @Nullable Preference preference,
        @Nullable String newSummary
    ) {
        if (preference == null) return;
        safelyUpdatePreferenceCommon(preference, newSummary);
    }


    public static void safelyUpdateSwitchPreference(
        @Nullable SwitchPreferenceCompat switchPref,
        @Nullable String newSummary,
        boolean newValue
    ) {
        if (switchPref == null) return;
        safelyUpdatePreferenceCommon(switchPref, newSummary);
        safelyUpdatePreferenceChecked(switchPref, newValue);
    }

    private static void safelyUpdatePreferenceCommon(
        @NonNull Preference preference,
        @Nullable String newSummary
    ) {
        ThreadUtils.checkUiThread();
        if (newSummary != null) {
            preference.setSummary(newSummary);
        }
    }

    private static void safelyUpdatePreferenceChecked(
        @NonNull SwitchPreferenceCompat switchPref,
        boolean checked
    ) {
        switchPref.setChecked(checked);
    }

    public static void safelyRemovePreference(
        @NonNull PreferenceFragmentCompat prefFragment,
        @NonNull String key
    ) {
        Preference preference = prefFragment.findPreference(key);
        safelyRemovePreference(prefFragment, preference);
    }

    public static void safelyRemovePreference(
        @NonNull PreferenceFragmentCompat prefFragment,
        @Nullable Preference preference
    ) {
        ThreadUtils.checkUiThread();
        if (preference != null) {
            prefFragment.getPreferenceScreen().removePreference(preference);
        }
    }
}
