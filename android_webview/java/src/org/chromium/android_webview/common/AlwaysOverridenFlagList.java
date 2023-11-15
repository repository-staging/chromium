package org.chromium.android_webview.common;

import org.chromium.blink_public.common.BlinkFeatures;

import java.util.HashMap;
import java.util.Map;

public class AlwaysOverridenFlagList {
    private static final Flag[] sFlags = {
        Flag.baseFeature(BlinkFeatures.CLIENT_HINTS_LOW_ENTROPY_ONLY),
        Flag.baseFeature(BlinkFeatures.CLIENT_HINTS_FROM_REDUCED_UA)
    };

    private static final Map<String, Boolean> sOverrides = new HashMap<>();

    static {
        sOverrides.put(BlinkFeatures.CLIENT_HINTS_LOW_ENTROPY_ONLY, false);
        sOverrides.put(BlinkFeatures.CLIENT_HINTS_FROM_REDUCED_UA, false);
    }

    public static void applyFlagOverrides(Map<String, Boolean> existingOverrideMap) {
        Map<String, Boolean> mergedOverrideMap;
        Flag[] mergedFlags;
        if (existingOverrideMap != null) {
            mergedOverrideMap = new HashMap<>(existingOverrideMap);
            mergedOverrideMap.putAll(sOverrides);
            mergedFlags = new Flag[ProductionSupportedFlagList.sFlagList.length + sFlags.length];
            System.arraycopy(ProductionSupportedFlagList.sFlagList, 0, mergedFlags, 0, ProductionSupportedFlagList.sFlagList.length);
            System.arraycopy(sFlags, 0, mergedFlags, ProductionSupportedFlagList.sFlagList.length, sFlags.length);
        } else {
            mergedOverrideMap = sOverrides;
            mergedFlags = sFlags;
        }
        var helper = new FlagOverrideHelper(mergedFlags);
        helper.applyFlagOverrides(mergedOverrideMap);
    }
}
