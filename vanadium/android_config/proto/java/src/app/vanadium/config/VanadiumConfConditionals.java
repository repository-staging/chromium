package app.vanadium.config;

import android.util.Log;

import app.vanadium.config.proto.VanadiumConfigProto.Component.ComponentCondition;

public final class VanadiumConfConditionals {

    public static final String TAG = "cr_VanadiumConfConditionals";

    public interface ConditionSupplier {
        boolean isUserAcceptLanguageIncluded(ComponentCondition componentCondition);
    }

    private static volatile ConditionSupplier conditionSupplier;

    private static ConditionSupplier getConditionSupplier() {
        return VanadiumConfConditionals.conditionSupplier;
    }

    static boolean isConditionSatisfied(ComponentCondition condition) {
        ConditionSupplier curConditionSupplier = getConditionSupplier();
        if (curConditionSupplier == null) {
            Log.d(TAG, "Conditional component not supported");
            return false;
        }

        if (condition == null || ComponentCondition.getDefaultInstance().equals(condition)) {
            return false;
        }

        if (!curConditionSupplier.isUserAcceptLanguageIncluded(condition)) {
            return false;
        }

        return true;
    }

    public static void setBrowserConditionals(ConditionSupplier conditionSupplier) {
        VanadiumConfConditionals.conditionSupplier = conditionSupplier;
    }
}
