/* Copyright (c) 2021 The Brave Authors. All rights reserved.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.chromium.chrome.browser.referrer.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;

import androidx.annotation.IntDef;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import org.chromium.chrome.R;
import org.chromium.components.browser_ui.widget.RadioButtonWithDescription;
import org.chromium.components.browser_ui.widget.RadioButtonWithDescriptionLayout;

import java.util.ArrayList;
import java.util.Collections;

public class CrossOriginReferrerPolicyPreference
        extends Preference implements RadioGroup.OnCheckedChangeListener {
    @IntDef({CrossOriginReferrerPolicy.DEFAULT, CrossOriginReferrerPolicy.REDUCE,
            CrossOriginReferrerPolicy.DISABLE})
    public @interface CrossOriginReferrerPolicy {
        int DEFAULT = 0;
        int REDUCE = 1;
        int DISABLE = 2;

        int NUM_ENTRIES = 3;
    }

    private @CrossOriginReferrerPolicy int mSetting;
    private RadioButtonWithDescription mSettingRadioButton;
    private RadioButtonWithDescriptionLayout mGroup;
    private ArrayList<RadioButtonWithDescription> mButtons;

    @SuppressLint("WrongConstant")
    public CrossOriginReferrerPolicyPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setLayoutResource(R.layout.cross_origin_referrer_policy_preference);

        mButtons = new ArrayList<>(Collections.nCopies(CrossOriginReferrerPolicy.NUM_ENTRIES, null));
    }

    public void initialize(@CrossOriginReferrerPolicy int policy) {
        mSetting = policy;
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        mGroup = (RadioButtonWithDescriptionLayout) holder.findViewById(R.id.radio_button_layout);
        mGroup.setOnCheckedChangeListener(this);

        mButtons.set(CrossOriginReferrerPolicy.DEFAULT,
                (RadioButtonWithDescription) holder.findViewById(
                        R.id.cross_origin_referrer_policy_default));
        mButtons.set(CrossOriginReferrerPolicy.REDUCE,
                (RadioButtonWithDescription) holder.findViewById(
                        R.id.cross_origin_referrer_policy_reduce));
        mButtons.set(CrossOriginReferrerPolicy.DISABLE,
                (RadioButtonWithDescription) holder.findViewById(
                        R.id.cross_origin_referrer_policy_disable));

        mSettingRadioButton = mButtons.get(mSetting);
        mSettingRadioButton.setChecked(true);
    }

    @Override
    @SuppressLint("WrongConstant")
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0; i < CrossOriginReferrerPolicy.NUM_ENTRIES; i++) {
            if (mButtons.get(i).isChecked()) {
                mSetting = i;
                mSettingRadioButton = mButtons.get(i);
                break;
            }
        }
        assert mSetting >= 0 && mSetting < CrossOriginReferrerPolicy.NUM_ENTRIES : "No matching setting found.";

        callChangeListener(mSetting);
    }
}
