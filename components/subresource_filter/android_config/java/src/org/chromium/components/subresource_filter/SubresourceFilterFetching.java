package org.chromium.components.subresource_filter;

import org.jni_zero.CalledByNative;
import org.jni_zero.JNINamespace;

import app.vanadium.config.SubresourceFilterComponentUtils;
import app.vanadium.config.VanadiumConfParser;
import app.vanadium.config.proto.VanadiumConfigProto.Component.ComponentType;

@JNINamespace("subresource_filter")
public class SubresourceFilterFetching {

    public static long getUnindexedRulesetVersion() {
        return VanadiumConfParser.getVersionCodeForComponent(ComponentType.SUBRESOURCE_FILTER_TOOLS);
    }

    public static byte[] getUnindexedRulesetData() {
        return VanadiumConfParser.getByteArrayForComponent(ComponentType.SUBRESOURCE_FILTER_TOOLS);
    }

    @CalledByNative
    public static boolean isInitialized() {
        return VanadiumConfParser.isInitialized();
    }

    @CalledByNative
    public static boolean deleteUnindexedFile() {
        return SubresourceFilterComponentUtils.deleteComponentFileForParsing();
    }
}
