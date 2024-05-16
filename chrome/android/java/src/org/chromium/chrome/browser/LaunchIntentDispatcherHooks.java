package org.chromium.chrome.browser;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;

import org.chromium.chrome.browser.searchwidget.SearchActivityHooks;

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

    static Intent maybeModifySearchIntents(Activity activity, Intent intent,
            Intent originalIntent) {
        Intent newIntent = maybeCreateIncognitoTabIntentFor(activity, intent);
        if (originalIntent != null) {
            String originalAction = originalIntent.getAction();
            boolean shoudlSelectAllText = !Intent.ACTION_WEB_SEARCH.equals(originalAction)
                    && !SearchManager.INTENT_ACTION_GLOBAL_SEARCH.equals(originalAction);
            newIntent.putExtra(SearchActivityHooks.EXTRA_SELECT_ALL_TEXT, shoudlSelectAllText);
        }

        return newIntent;
    }

    static Intent maybeModifyActionSendIntents(Activity activity, Intent intent) {
        Intent newIntent = maybeCreateIncognitoTabIntentFor(activity, intent);

        return newIntent;
    }
}
