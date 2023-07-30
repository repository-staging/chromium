/* Copyright (c) 2021 The Brave Authors. All rights reserved.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.chromium.chrome.browser.referrer.settings;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

import org.chromium.chrome.R;
import org.chromium.components.browser_ui.settings.SettingsUtils;
import org.chromium.ui.UiUtils;

public class CrossOriginReferrerPolicySettings extends PreferenceFragmentCompat {
    static final String PREF_CROSS_ORIGIN_REFERRER_POLICY = "cross_origin_referrer_policy";

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, String rootKey) {
        SettingsUtils.addPreferencesFromResource(this,
                R.xml.cross_origin_referrer_policy_preferences);
        getActivity().setTitle(R.string.cross_origin_referrer_policy_title);

        CrossOriginReferrerPolicyPreference crossOriginReferrerPolicyPreference =
                (CrossOriginReferrerPolicyPreference) findPreference(
                        PREF_CROSS_ORIGIN_REFERRER_POLICY);
        crossOriginReferrerPolicyPreference.initialize(CrossOriginReferrerPolicyUtils.getPolicy());

        crossOriginReferrerPolicyPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            CrossOriginReferrerPolicyUtils.setPolicy((int) newValue);
            return true;
        });
    }

    public static String getCrossOriginReferrerPolicySummaryString(Context context) {
        return CrossOriginReferrerPolicyUtils.getSummary(context);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O_MR1) {
            UiUtils.setNavigationBarIconColor(getActivity().getWindow().getDecorView(),
                    getResources().getBoolean(R.bool.window_light_navigation_bar));
        }

        setDivider(null);
    }
}
