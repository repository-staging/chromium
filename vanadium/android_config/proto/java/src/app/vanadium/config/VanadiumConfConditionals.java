package app.vanadium.config;

public final class VanadiumConfConditionals {

    public interface ConditionSupplier {
    }

    private static volatile ConditionSupplier conditionSupplier;

    private static ConditionSupplier getConditionSupplier() {
        return VanadiumConfConditionals.conditionSupplier;
    }

    public static void setBrowserConditionals(ConditionSupplier conditionSupplier) {
        VanadiumConfConditionals.conditionSupplier = conditionSupplier;
    }
}
