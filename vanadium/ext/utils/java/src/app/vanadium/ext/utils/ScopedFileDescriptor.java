package app.vanadium.ext.utils;

import android.content.Context;
import android.os.ParcelFileDescriptor;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

// Class authored by muhomorr in Apps app repository, see
// https://github.com/GrapheneOS/Apps/commit/43e9d5218374e712f96154b269e554b2debd652d#diff-d95bcaf27889d24a999547d34ab4a980c248cd9f048aebf27524cdc103e58b47
// and the copied documentation below:
public final class ScopedFileDescriptor implements AutoCloseable {

    public static final String TAG = "cr_ScopedFileDescriptor";

    public final FileDescriptor v;

    public ScopedFileDescriptor(FileDescriptor v) {
        this.v = v;
    }

    // Needed for accessing the int value of fd.
    // Strangely, access to int value of FileDescriptor is restricted behind hidden and discouraged
    // FileDescriptor.getInt$(), but is part of public API of ParcelFileDescriptor
    public ParcelFileDescriptor dupToFd() throws IOException {
        return ParcelFileDescriptor.dup(v);
    }

    @Override
    public void close() {
        try {
            Os.close(v);
        } catch (ErrnoException e) {
            Log.d(TAG, "", e);
        }
    }
}
