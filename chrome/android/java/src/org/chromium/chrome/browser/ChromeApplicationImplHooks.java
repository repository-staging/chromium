package org.chromium.chrome.browser;

import android.content.res.Configuration;

import org.chromium.base.LocaleUtils;
import org.chromium.base.Log;
import org.chromium.chrome.browser.base.SplitCompatApplication;
import org.chromium.chrome.browser.language.settings.LanguageItem;
import org.chromium.chrome.browser.language.settings.LanguagesManager;
import org.chromium.chrome.browser.profiles.ProfileManager;
import org.chromium.chrome.browser.subresource_filter.RulesetUpdater;

import java.util.List;
import java.util.stream.Collectors;

import app.vanadium.config.VanadiumConfConditionals;
import app.vanadium.config.VanadiumConfConditionals.ConditionSupplier;
import app.vanadium.config.proto.VanadiumConfigProto.Component.ComponentCondition;

final class ChromeApplicationImplHooks {

    private static final String TAG = "ChromeApplicationImplHooks";

    public static void postAttachBaseContext() {
        if (!SplitCompatApplication.isBrowserProcess()) {
            return;
        }

        VanadiumConfConditionals.setBrowserConditionals(new ConditionSupplier() {
            private List<String> getUserAcceptLanguageForSites()  {
                return LanguagesManager.getForProfile(
                            ProfileManager.getLastUsedRegularProfile())
                        .getUserAcceptLanguageItems().stream()
                        .map(LanguageItem::getCode)
                        .collect(Collectors.toList());
            }

            @Override
            public boolean isUserAcceptLanguageIncluded(ComponentCondition componentCondition) {
                String conditionalLanguageTag = componentCondition.getLanguage();
                String conditionalBaseLanguageTag =
                        LocaleUtils.toBaseLanguage(conditionalLanguageTag);
                boolean isConditionBaseLanguageCode =
                        conditionalBaseLanguageTag.equals(conditionalLanguageTag);
                return getUserAcceptLanguageForSites().stream().anyMatch(curLanguageTag -> {
                    if (!isConditionBaseLanguageCode) {
                        return conditionalLanguageTag.equals(curLanguageTag);
                    }
                    return LocaleUtils.isBaseLanguageEqual(curLanguageTag, conditionalLanguageTag);
                });
            }
        });
    }

    public static void onCreate() {
    }

    public static void onConfigurationChanged(Configuration newConfig) {
        if (!SplitCompatApplication.isBrowserProcess()) {
            Log.i(TAG, "Update only runs on browser process");
            return;
        }

        if (!ProfileManager.isInitialized()) {
            Log.i(TAG, "ProfileManager's native code is not yet loaded");
            return;
        }

        RulesetUpdater.update(/* ignoreVersionCheck */ true);
    }
}
