package org.chromium.chrome.browser.searchwidget;

import org.chromium.chrome.browser.omnibox.LocationBarCoordinator;

public class SearchActivityHooks {

    static void modifyLocationBarCoordinatorBehavior(LocationBarCoordinator locationBarCoordinator) {
        locationBarCoordinator.setShouldClearOmniboxByDefault(false);
    }
}
