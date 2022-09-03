package org.chromium.chrome.browser.searchwidget;

import android.app.Activity;
import android.content.Intent;

import org.chromium.chrome.browser.TabPreferencesUtils;
import org.chromium.chrome.browser.omnibox.LocationBarCoordinator;

public class SearchActivityHooks {

    static void modifyLocationBarCoordinatorBehavior(LocationBarCoordinator locationBarCoordinator) {
        locationBarCoordinator.setShouldClearOmniboxByDefault(false);
    }

    static Intent modifyIntentForStartActivity(Activity activity, Intent intent) {
        Intent newIntent = intent;
        if (TabPreferencesUtils.shouldOpenLinksInIncognito()) {
            newIntent = TabPreferencesUtils.appendNeededIncognitoExtras(activity, newIntent);
        }

        return newIntent;
    }
}
