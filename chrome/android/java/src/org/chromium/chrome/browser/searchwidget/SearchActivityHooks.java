package org.chromium.chrome.browser.searchwidget;

import android.app.Activity;
import android.content.Intent;

import org.chromium.base.IntentUtils;
import org.chromium.chrome.browser.IntentHandler;
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

    static boolean shouldOpenInIncognito(Intent intent) {
        return IntentUtils.safeGetBooleanExtra(intent,
                IntentHandler.EXTRA_OPEN_NEW_INCOGNITO_TAB, false);
    }

    public static final String EXTRA_SELECT_ALL_TEXT =
            "org.chromium.chrome.browser.searchwidget.SELECT_ALL_TEXT";

    static boolean getShouldSelectAllQueryTextInSearchBox(Intent intent) {
        String prepopulatedIntentQuery = SearchActivityUtils.getIntentQuery(intent);
        prepopulatedIntentQuery = prepopulatedIntentQuery != null ? prepopulatedIntentQuery : "";

        if (prepopulatedIntentQuery.isEmpty()) {
            return true;
        }

        return IntentUtils.safeGetBooleanExtra(intent, EXTRA_SELECT_ALL_TEXT, true);
    }
}
