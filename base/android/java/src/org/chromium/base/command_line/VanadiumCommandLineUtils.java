package org.chromium.base.command_line;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import org.chromium.base.CommandLine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class VanadiumCommandLineUtils {
    public static List<String> getCommaDelimitedSwitchValue(String name) {
        return CommandLine.getInstance().hasSwitch(name)
                && CommandLine.getInstance().getSwitchValue(name) != null
                ? Arrays.asList(CommandLine.getInstance().getSwitchValue(name).split(","))
                : new ArrayList<>();
    }

    public static void setCommaDelimitedSwitchValue(String name, @NonNull List<String> value) {
        if (value.isEmpty()) {
            CommandLine.getInstance().removeSwitch(name);
        } else {
            CommandLine.getInstance().appendSwitchWithValue(name, TextUtils.join(",", value));
        }
    }

    public static void removeValueOnSwitch(String name, @NonNull String value) {
        removeValuesOnSwitch(name, List.of(value));
    }

    public static void removeValuesOnSwitch(String name, @NonNull List<String> values) {
        addOrRemoveValuesOnSwitch(name, values, false);
    }

    public static void appendValueOnSwitch(String name, @NonNull String value) {
        appendValuesOnSwitch(name, List.of(value));
    }

    public static void appendValuesOnSwitch(String name, @NonNull List<String> values) {
        addOrRemoveValuesOnSwitch(name, values, true);
    }

    private static void addOrRemoveValuesOnSwitch(String name, @NonNull List<String> values, boolean add) {
        if (CommandLine.getInstance() != null) {
            var newValues = new HashSet<>(getCommaDelimitedSwitchValue(name));
            var valuesSet = new HashSet<>(values);
            if (add) {
                newValues.addAll(valuesSet);
            } else {
                newValues.removeAll(valuesSet);
            }
            VanadiumCommandLineUtils.setCommaDelimitedSwitchValue(name, new ArrayList<>(newValues));
        }
    }
}
