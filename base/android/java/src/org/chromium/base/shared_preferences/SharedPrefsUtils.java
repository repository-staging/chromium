package org.chromium.base.shared_preferences;

import org.chromium.build.annotations.CheckDiscard;

import java.util.Arrays;

/**
 * Wrapper utility class for both (Base)ChromePreferenceKeys and SharedPreferencesManager,
 * which also serves as storage of added PreferenceKeys.
 */
public final class SharedPrefsUtils {

    public static class BoolSharedPref {
        private final String key;
        private boolean defValue;

        BoolSharedPref(String key, boolean defValue) {
            this.key = key;
            this.defValue = defValue;
        }

        public String getKey() { return key; }

        public boolean get() {
            return getSharedPrefManager().readBoolean(key, defValue);
        }

        public void put(boolean newValue) {
            getSharedPrefManager().writeBoolean(key, newValue);
        }

        public boolean putSync(boolean newValue) {
            return getSharedPrefManager().writeBooleanSync(key, newValue);
        }
    }

    public static class IntSharedPref {
        private final String key;
        private int defValue;
        private int[] validValues;

        IntSharedPref(String key, int defValue) {
            this.key = key;
            this.defValue = defValue;
        }

        IntSharedPref(String key, int defValue, int[] validValues) {
            this.key = key;
            this.defValue = defValue;
            this.validValues = validValues;
        }

        public String getKey() { return key; }

        public int get() {
            return getSharedPrefManager().readInt(key, defValue);
        }

        public boolean validateValue(int val) {
            if (validValues == null) {
                return true;
            }
            // don't do sort() + bsearch() of validValues array, it's expected to have a small number of entries
            for (int validValue : validValues) {
                if (val == validValue) {
                    return true;
                }
            }
            return false;
        }

        public void put(int newValue) {
            if (!validateValue(newValue)) {
                return;
            }
            getSharedPrefManager().writeInt(key, newValue);
        }

        public boolean putSync(int newValue) {
            if (!validateValue(newValue)) {
                return false;
            }
            return getSharedPrefManager().writeIntSync(key, newValue);
        }
    }

    public static class StringSharedPref {
        private final String key;
        private String defValue;

        StringSharedPref(String key, String defValue) {
            this.key = key;
            this.defValue = defValue;
        }

        public String getKey() { return key; }

        public String get() {
            return getSharedPrefManager().readString(key, defValue);
        }

        public boolean validateValue(String val) {
            return true;
        }

        public void put(String newValue) {
            if (!validateValue(newValue)) {
                return;
            }
            getSharedPrefManager().writeString(key, newValue);
        }

        public boolean putSync(String newValue) {
            if (!validateValue(newValue)) {
                return false;
            }
            return getSharedPrefManager().writeStringSync(key, newValue);
        }
    }

    // Stores SharedPreferences keys and its default value
    public static class SharedPrefsExt {
    }

    static SharedPreferencesManager getSharedPrefManager() {
        return SharedPreferencesManager.getInstanceForRegistry(null);
    }

    @CheckDiscard("Validation is performed in tests and in debug builds.")
    static boolean isKeyInUse(String key) {
        // clang-format off
        return Arrays.asList(
        ).contains(key);
        // clang-format on
    }
}
