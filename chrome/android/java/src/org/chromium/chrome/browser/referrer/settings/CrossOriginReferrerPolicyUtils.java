// Copyright 2023 GrapheneOS
// Use of this source code is governed by a GPLv2 only-style license that can be
// found in the LICENSE file.

package org.chromium.chrome.browser.referrer.settings;

import android.content.Context;

import org.chromium.chrome.R;
import org.chromium.chrome.browser.preferences.Pref;
import org.chromium.chrome.browser.profiles.ProfileManager;
import org.chromium.chrome.browser.referrer.settings.CrossOriginReferrerPolicyPreference.CrossOriginReferrerPolicy;
import org.chromium.components.user_prefs.UserPrefs;

/**
 * Utility class for fetching and setting native pref values for cross-origin referrer policy 
 * and retrieving their corresponding descriptions.
 */
class CrossOriginReferrerPolicyUtils {
    static String getSummary(Context context) {
        switch (getPolicy()) {
            case CrossOriginReferrerPolicy.DEFAULT:
                return context.getString(R.string.cross_origin_referrer_policy_default);
            case CrossOriginReferrerPolicy.REDUCE:
                return context.getString(R.string.cross_origin_referrer_policy_reduce);
            case CrossOriginReferrerPolicy.DISABLE:
                return context.getString(R.string.cross_origin_referrer_policy_disable);
            default:
                assert false;
                return "";
        }
    }

    static @CrossOriginReferrerPolicy int getPolicy() {
        return UserPrefs.get(ProfileManager.getLastUsedRegularProfile())
            .getInteger(Pref.CROSS_ORIGIN_REFERRER_POLICY);
    }

    static void setPolicy(@CrossOriginReferrerPolicy int policy) {
        UserPrefs.get(ProfileManager.getLastUsedRegularProfile())
            .setInteger(Pref.CROSS_ORIGIN_REFERRER_POLICY, policy);
    }

    private CrossOriginReferrerPolicyUtils() {}
}
