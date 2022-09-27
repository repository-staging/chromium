// Copyright 2022-2024 GrapheneOS
// Use of this source code is governed by a GPL-2.0-style license that can be
// found in the LICENSE file.

// Initially authored by Zoraver Kang, updated by GrapheneOS
// to fit the config apk infrastructure for Vanadium.

package org.chromium.chrome.browser.subresource_filter;

import org.chromium.base.Log;
import org.chromium.components.subresource_filter.SubresourceFilterFetching;
import org.jni_zero.JNINamespace;
import org.jni_zero.NativeMethods;

import app.vanadium.config.SubresourceFilterComponentUtils;
import app.vanadium.config.VanadiumConfParser;

@JNINamespace("subresource_filter")
public class RulesetUpdater {

    private static final String TAG = RulesetUpdater.class.getSimpleName();

    public static void init() {
        if (!VanadiumConfParser.isInitialized()) {
            Log.w(TAG, "config not initialized");
            return;
        }

        update(/* ignoreVersionCheck */ false);
    }

    public static void update(boolean ignoreVersionCheck) {
        long unindexedContentVersionCode = SubresourceFilterFetching.getUnindexedRulesetVersion();
        if (ignoreVersionCheck) {
            Log.d(TAG, "Will update subresource filter from config apk regardless of previous version");
        } else {
            if (unindexedContentVersionCode < 0) {
                return;
            }

            long previousVersion;
            try {
                previousVersion = Long.parseLong(version());
            } catch (NumberFormatException e) {
                Log.d(TAG, "Previous version is not from config apk, treating as outdated.");
                previousVersion = -1;
            }

            if (previousVersion >= unindexedContentVersionCode) {
                Log.d(TAG, "Not updating the subresource filter from config apk, previous version fetched is up to date");
                return;
            }
        }

        byte[] unindexedRulesetData = SubresourceFilterFetching.getUnindexedRulesetData();
        if (unindexedRulesetData == null) {
            return;
        }

        SubresourceFilterComponentUtils.writeCurrentContents();

        String unindexedContentFilePath = SubresourceFilterComponentUtils.getFilePathForParsing();
        if (unindexedContentFilePath == null) {
            Log.w(TAG, "Unable to update ruleset with ignoreVersionCheck: " + ignoreVersionCheck);
            return;
        }

        RulesetUpdaterJni.get().update(Long.toString(unindexedContentVersionCode), unindexedContentFilePath);
    }

    public static String version() {
        return RulesetUpdaterJni.get().version();
    }

    @NativeMethods
    interface Natives {
        void update(String unindexedContentVersion, String unindexedContentPath);
        String version();
    }
}
