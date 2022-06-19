package org.chromium.chrome.browser;

import android.app.Activity;
import android.content.Intent;

final class LaunchIntentDispatcherHooks {
    static Intent maybeModifyActionViewIntents(Activity activity, Intent intent) {
        Intent newIntent = intent;
        if (TabPreferencesUtils.shouldOpenLinksInIncognito()) {
            newIntent = TabPreferencesUtils.createIncognitoIntent(activity, intent);
        }

        return newIntent;
    }
}
