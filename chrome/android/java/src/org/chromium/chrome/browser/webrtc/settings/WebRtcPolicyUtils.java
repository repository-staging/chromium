// Copyright 2023 GrapheneOS
// Use of this source code is governed by a GPLv2 only-style license that can be
// found in the LICENSE file.

package org.chromium.chrome.browser.webrtc.settings;

import android.content.Context;

import org.chromium.chrome.R;
import org.chromium.chrome.browser.preferences.Pref;
import org.chromium.chrome.browser.profiles.ProfileManager;
import org.chromium.chrome.browser.webrtc.settings.WebRtcPolicyPreference.WebRtcPolicy;
import org.chromium.components.user_prefs.UserPrefs;

/**
 * Utility class for fetching and converting native pref value for WebRTC
 * policy to Java int equivalent and its corresponding description.
 */
class WebRtcPolicyUtils {
    // Pref values mirrored in Java, copied from
    // third_party/blink/common/peerconnection/webrtc_ip_handling_policy.cc
    private static final String WEBRTC_DEFAULT = "default";
    private static final String WEBRTC_DEFAULT_PUBLIC_AND_PRIVATE_INTERFACES =
            "default_public_and_private_interfaces";
    private static final String WEBRTC_DEFAULT_PUBLIC_INTERFACE_ONLY =
            "default_public_interface_only";
    private static final String WEBRTC_DISABLE_NON_PROXIED_UDP =
            "disable_non_proxied_udp";

    static String getSummary(Context context) {
        switch (convertToWebRtcPolicyInt(getPolicyFromNative())) {
            case WebRtcPolicy.DEFAULT:
                return context.getString(R.string.webrtc_policy_default);
            case WebRtcPolicy.DEFAULT_PUBLIC_AND_PRIVATE_INTERFACES:
                return context.getString(
                        R.string.webrtc_policy_default_public_and_private_interfaces);
            case WebRtcPolicy.DEFAULT_PUBLIC_INTERFACE_ONLY:
                return context.getString(R.string.webrtc_policy_default_public_interface_only);
            case WebRtcPolicy.DISABLE_NON_PROXIED_UDP:
                return context.getString(R.string.webrtc_policy_disable_non_proxied_udp);
            default:
                assert false;
                return "";
        }
    }

    static String convertToWebRtcPolicyString(@WebRtcPolicy int policy) {
        switch (policy) {
            case WebRtcPolicy.DEFAULT:
                return WEBRTC_DEFAULT;
            case WebRtcPolicy.DEFAULT_PUBLIC_AND_PRIVATE_INTERFACES:
                return WEBRTC_DEFAULT_PUBLIC_AND_PRIVATE_INTERFACES;
            case WebRtcPolicy.DEFAULT_PUBLIC_INTERFACE_ONLY:
                return WEBRTC_DEFAULT_PUBLIC_INTERFACE_ONLY;
            case WebRtcPolicy.DISABLE_NON_PROXIED_UDP:
                return WEBRTC_DISABLE_NON_PROXIED_UDP;
            default:
                return WEBRTC_DISABLE_NON_PROXIED_UDP;
        }
    }

    static @WebRtcPolicy int convertToWebRtcPolicyInt(String value) {
        switch (value) {
            case WEBRTC_DEFAULT:
                return WebRtcPolicy.DEFAULT;
            case WEBRTC_DEFAULT_PUBLIC_AND_PRIVATE_INTERFACES:
                return WebRtcPolicy.DEFAULT_PUBLIC_AND_PRIVATE_INTERFACES;
            case WEBRTC_DEFAULT_PUBLIC_INTERFACE_ONLY:
                return WebRtcPolicy.DEFAULT_PUBLIC_INTERFACE_ONLY;
            case WEBRTC_DISABLE_NON_PROXIED_UDP:
                return WebRtcPolicy.DISABLE_NON_PROXIED_UDP;
            default:
                return WebRtcPolicy.DISABLE_NON_PROXIED_UDP;
        }
    }

    private static String getPolicyFromNative() {
        return UserPrefs.get(ProfileManager.getLastUsedRegularProfile())
                .getString(Pref.WEB_RTCIP_HANDLING_POLICY);
    }

    static @WebRtcPolicy int getPolicy() {
        return convertToWebRtcPolicyInt(getPolicyFromNative());
    }

    static void setPolicy(@WebRtcPolicy int policy) {
        UserPrefs.get(ProfileManager.getLastUsedRegularProfile())
                .setString(Pref.WEB_RTCIP_HANDLING_POLICY, convertToWebRtcPolicyString(policy));
    }

    private WebRtcPolicyUtils() {}
}
