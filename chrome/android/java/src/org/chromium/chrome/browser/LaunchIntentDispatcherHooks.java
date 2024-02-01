package org.chromium.chrome.browser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

final class LaunchIntentDispatcherHooks {

    private static Intent maybeCreateIncognitoTabIntentFor(Context context, Intent intent) {
        Intent newIntent = intent;
        if (TabPreferencesUtils.shouldOpenLinksInIncognito()) {
            newIntent = TabPreferencesUtils.appendNeededIncognitoExtras(context, newIntent);
        }

        return newIntent;
    }

    static Intent maybeModifyActionViewIntents(Activity activity, Intent intent) {
        Intent newIntent = intent;
        if (TabPreferencesUtils.shouldOpenLinksInIncognito()) {
            newIntent = TabPreferencesUtils.createIncognitoIntent(activity, intent);
        }

        return newIntent;
    }

    static Intent maybeModifyCustomTabIntents(Context context, Intent intent) {
        Intent newIntent = maybeCreateIncognitoTabIntentFor(context, intent);

        return newIntent;
    }
}
