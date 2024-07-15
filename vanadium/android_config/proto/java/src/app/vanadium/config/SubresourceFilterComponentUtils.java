package app.vanadium.config;

import android.content.Context;
import android.system.ErrnoException;
import android.util.Log;

import java.io.InterruptedIOException;
import java.util.Objects;

import app.vanadium.config.proto.VanadiumConfigProto;
import app.vanadium.ext.utils.AtomicFile2;

public final class SubresourceFilterComponentUtils {

    private static final String UNINDEXED_RULESET_FILE_NAME = "unindexed_subresource_filter";
    private static final String TAG = "cr_SubresourceFilterComponentUtils";

    private static SubresourceFilterComponentUtils instance;
    private static volatile boolean initialized = false;

    private final AtomicFile2 atomicFileForParsing;

    SubresourceFilterComponentUtils(Context appCtx) {
        this.atomicFileForParsing = new AtomicFile2(appCtx, UNINDEXED_RULESET_FILE_NAME);
    }

    static SubresourceFilterComponentUtils initialize(Context appCtx) {
        if (initialized) {
            return instance;
        }

        instance = new SubresourceFilterComponentUtils(appCtx);
        initialized = true;
        return instance;
    }

    private static SubresourceFilterComponentUtils getInstance() {
        if (!initialized || instance == null) {
            Log.w(TAG, "not yet initialized", new Throwable());
            return null;
        }

        return instance;
    }

    public static void writeCurrentContents() {
        SubresourceFilterComponentUtils instance = getInstance();
        if (instance == null) {
            return;
        }

        try {
            instance.atomicFileForParsing.write(
                    VanadiumConfParser.getByteArrayForComponent(
                            VanadiumConfigProto.Component.ComponentType.SUBRESOURCE_FILTER_TOOLS));
        } catch (ErrnoException | InterruptedIOException e) {
            Log.e(TAG, "", e);
        }
    }

    public static boolean deleteComponentFileForParsing() {
        SubresourceFilterComponentUtils instance = getInstance();
        if (instance == null) {
            return false;
        }

        return instance.atomicFileForParsing.delete();
    }

    public static String getFilePathForParsing() {
        SubresourceFilterComponentUtils instance = getInstance();
        if (instance == null) {
            return null;
        }

        return instance.atomicFileForParsing.filePath;
    }
}
