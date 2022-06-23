/* Copyright (c) 2021 The Brave Authors. All rights reserved.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.chromium.chrome.browser.webrtc.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Browser;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import org.chromium.chrome.R;
import org.chromium.components.browser_ui.widget.RadioButtonWithDescription;
import org.chromium.components.browser_ui.widget.RadioButtonWithDescriptionLayout;

import java.util.ArrayList;
import java.util.Collections;

public class WebRtcPolicyPreference
        extends Preference implements RadioGroup.OnCheckedChangeListener {
    @IntDef({WebRtcPolicy.DEFAULT, WebRtcPolicy.DEFAULT_PUBLIC_AND_PRIVATE_INTERFACES,
            WebRtcPolicy.DEFAULT_PUBLIC_INTERFACE_ONLY, WebRtcPolicy.DISABLE_NON_PROXIED_UDP})
    public @interface WebRtcPolicy {
        int DEFAULT = 0;
        int DEFAULT_PUBLIC_AND_PRIVATE_INTERFACES = 1;
        int DEFAULT_PUBLIC_INTERFACE_ONLY = 2;
        int DISABLE_NON_PROXIED_UDP = 3;

        int NUM_ENTRIES = 4;
    }

    private @WebRtcPolicy int mSetting;
    private RadioButtonWithDescription mSettingRadioButton;
    private RadioButtonWithDescriptionLayout mGroup;
    private ArrayList<RadioButtonWithDescription> mButtons;

    @SuppressLint("WrongConstant")
    public WebRtcPolicyPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setLayoutResource(R.layout.webrtc_policy_preference);

        mButtons = new ArrayList<>(Collections.nCopies(WebRtcPolicy.NUM_ENTRIES, null));
    }

    public void initialize(@WebRtcPolicy int policy) {
        mSetting = policy;
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        mGroup = (RadioButtonWithDescriptionLayout) holder.findViewById(R.id.radio_button_layout);
        mGroup.setOnCheckedChangeListener(this);

        mButtons.set(WebRtcPolicy.DEFAULT,
                (RadioButtonWithDescription) holder.findViewById(R.id.webrtc_policy_default));
        mButtons.set(WebRtcPolicy.DEFAULT_PUBLIC_AND_PRIVATE_INTERFACES,
                (RadioButtonWithDescription) holder.findViewById(
                        R.id.webrtc_policy_default_public_and_private_interfaces));
        mButtons.set(WebRtcPolicy.DEFAULT_PUBLIC_INTERFACE_ONLY,
                (RadioButtonWithDescription) holder.findViewById(
                        R.id.webrtc_policy_default_public_interface_only));
        mButtons.set(WebRtcPolicy.DISABLE_NON_PROXIED_UDP,
                (RadioButtonWithDescription) holder.findViewById(
                        R.id.webrtc_policy_disable_non_proxied_udp));

        mSettingRadioButton = mButtons.get(mSetting);
        mSettingRadioButton.setChecked(true);
    }

    @Override
    @SuppressLint("WrongConstant")
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0; i < WebRtcPolicy.NUM_ENTRIES; i++) {
            if (mButtons.get(i).isChecked()) {
                mSetting = i;
                mSettingRadioButton = mButtons.get(i);
                break;
            }
        }
        assert mSetting >= 0 && mSetting < WebRtcPolicy.NUM_ENTRIES : "No matching setting found.";

        callChangeListener(mSetting);
    }
}
