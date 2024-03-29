// Copyright 2024 The Chromium Authors
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
package org.chromium.chrome.browser.webauthn;

import org.chromium.components.webauthn.cred_man.CredManUiRecommender;

public class CredManUiRecommenderImpl implements CredManUiRecommender {
    @Override
    public boolean recommendsCustomUi() {
        // Requires to be changed when WebAuthenticationAndroidCredMan feature default is changed,
        // along with its FeatureParam "gpm_in_cred_man", both found at //device/fido/features.h as of M124
        return true; // Use the platform CredMan APIs if available.
    }
}
