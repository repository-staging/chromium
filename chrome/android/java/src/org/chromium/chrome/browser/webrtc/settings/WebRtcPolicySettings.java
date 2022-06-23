/* Copyright (c) 2021 The Brave Authors. All rights reserved.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.chromium.chrome.browser.webrtc.settings;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Browser;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

import org.chromium.chrome.R;
import org.chromium.components.browser_ui.settings.SettingsUtils;
import org.chromium.ui.UiUtils;

public class WebRtcPolicySettings extends PreferenceFragmentCompat {
    static final String PREF_WEBRTC_POLICY = "webrtc_policy";

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, String rootKey) {
        SettingsUtils.addPreferencesFromResource(this, R.xml.webrtc_policy_preferences);
        getActivity().setTitle(R.string.webrtc_policy_title);

        WebRtcPolicyPreference webRtcPolicyPreference =
                (WebRtcPolicyPreference) findPreference(PREF_WEBRTC_POLICY);
        webRtcPolicyPreference.initialize(WebRtcPolicyUtils.getPolicy());

        webRtcPolicyPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            WebRtcPolicyUtils.setPolicy((int) newValue);
            return true;
        });
    }

    public static String getWebRtcPolicySummaryString(Context context) {
        return WebRtcPolicyUtils.getSummary(context);
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
