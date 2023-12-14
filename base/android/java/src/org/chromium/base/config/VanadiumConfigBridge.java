package org.chromium.base.config;

import static org.chromium.base.command_line.VanadiumCommandLineUtils.getCommaDelimitedSwitchValue;
import static org.chromium.base.command_line.VanadiumCommandLineUtils.setCommaDelimitedSwitchValue;

import android.content.Context;

import app.vanadium.config.VanadiumConfParser;
import app.vanadium.config.proto.VanadiumConfigProto.Flag.FlagType;

import org.chromium.base.CommandLine;
import org.chromium.base.Log;
import org.chromium.base.library_loader.LibraryProcessType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VanadiumConfigBridge {
    private static final String TAG = "VanadiumConfigBridge";

    public static void initConfigs(Context appCtx, @LibraryProcessType int libraryProcessType) {
        switch (libraryProcessType) {
            case LibraryProcessType.PROCESS_BROWSER:
                VanadiumConfParser.initForBrowser(appCtx);
                break;
            case LibraryProcessType.PROCESS_WEBVIEW:
                VanadiumConfParser.initForWebView(appCtx);
                break;
            case LibraryProcessType.PROCESS_CHILD:
            case LibraryProcessType.PROCESS_WEBVIEW_NONEMBEDDED:
            case LibraryProcessType.PROCESS_WEBVIEW_CHILD:
                break;
            default:
                throw new IllegalStateException();
        }
    }

    public static void applyFlagFromConfigs(Context appCtx, @LibraryProcessType int libraryProcessType) {
        if (!VanadiumConfParser.isInitialized()) {
            Log.e(TAG, "Config app not parsed nor initialized, returning early");
            return;
        }

        if (CommandLine.getInstance() == null) {
            Log.e(TAG, "No command line instance present");
            return;
        }

        Set<String> enabledFeatures = new HashSet<>(getCommaDelimitedSwitchValue("enable-features"));
        Set<String> disabledFeatures = new HashSet<>(getCommaDelimitedSwitchValue("disable-features"));

        for (FlagType flagType : FlagType.values()) {
            if (flagType == FlagType.UNRECOGNIZED) {
                continue;
            }

            if (flagType == FlagType.UNKNOWN) {
                continue;
            }

            for (Map.Entry<String, Set<String>> flag : VanadiumConfParser.getFlagsFor(flagType).entrySet()) {
                String flagName = flag.getKey();
                Set<String> flagValues = flag.getValue();
                switch (flagType) {
                    case ENABLED_FEATURE:
                        enabledFeatures.add(flagName);
                        disabledFeatures.remove(flagName);
                        break;
                    case DISABLED_FEATURE:
                        disabledFeatures.add(flagName);
                        enabledFeatures.remove(flagName);
                        break;
                    case ENABLED_SWITCH:
                        if (flagValues.isEmpty()) {
                            CommandLine.getInstance().appendSwitch(flagName);
                        } else {
                            Set<String> switchValue = new HashSet<>(getCommaDelimitedSwitchValue(flagName));
                            switchValue.addAll(flagValues);
                            setCommaDelimitedSwitchValue(flagName, new ArrayList<>(switchValue));
                        }
                        break;
                    case DISABLED_SWITCH:
                        if (flagValues.isEmpty()) {
                            CommandLine.getInstance().removeSwitch(flagName);
                        } else {
                            Set<String> switchValue = new HashSet<>(getCommaDelimitedSwitchValue(flagName));
                            switchValue.removeAll(flagValues);
                            setCommaDelimitedSwitchValue(flagName, new ArrayList<>(switchValue));
                        }
                        break;
                    default:
                        throw new IllegalStateException();
                }
            }
        }

        setCommaDelimitedSwitchValue("enable-features", new ArrayList<>(enabledFeatures));
        setCommaDelimitedSwitchValue("disable-features", new ArrayList<>(disabledFeatures));
    }
}
