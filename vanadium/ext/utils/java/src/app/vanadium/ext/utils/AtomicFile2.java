package app.vanadium.ext.utils;

import android.content.Context;
import android.os.Build;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.nio.file.Files;

// Class authored by muhomorr in Apps app repository, see
// https://github.com/GrapheneOS/Apps/commit/43e9d5218374e712f96154b269e554b2debd652d#diff-d95bcaf27889d24a999547d34ab4a980c248cd9f048aebf27524cdc103e58b47
// and the copied documentation below:
// android.util.AtomicFile has several issues:
//  - ignores errors from fsync(), rename(), close()
//  - doesn't do fsync of the containing directory
//  - doesn't enforce having at most one writer at a time
//  - checks for a legacy file name that was used by its older implementation
public final class AtomicFile2 {

    public static final String TAG = "cr_AtomicFile2";

    public final String name;
    public final String dirPath;
    private final File file;
    public final String filePath;
    private final String tmpFilePath;

    // Context.getFilesDir() is the only reliable place to write files: files from cache dir may
    // disappear mid-write, before rename of tmpPath. Writes to files on shared storage go through
    // the FUSE daemon, which lowers reliability and performance
    public AtomicFile2(Context ctx, String name) {
        File filesDir = ctx.getFilesDir();
        this.name = name;
        this.dirPath = filesDir.getPath();
        this.file = new File(filesDir, name);
        this.filePath = this.file.getPath();
        this.tmpFilePath = this.filePath + ".tmp";
    }

    public byte[] read() throws IOException {
        if (!file.exists()) {
            return null;
        }

        return Files.readAllBytes(file.toPath());
    }

    public boolean delete() {
        synchronized (file) {
            return file.delete();
        }
    }

    public void write(byte[] bytes) throws ErrnoException, InterruptedIOException {
        synchronized (file) {
            writeInner(bytes);
        }
    }

    private void writeInner(byte[] bytes) throws ErrnoException, InterruptedIOException {
        int flags = OsConstants.O_RDWR | OsConstants.O_CREAT |
                // in case there's a leftover file from previous failed write
                OsConstants.O_TRUNC;
        int mode = OsConstants.S_IRUSR | OsConstants.S_IWUSR; // 0600

        try (ScopedFileDescriptor sfd =
                     new ScopedFileDescriptor(Os.open(tmpFilePath, flags, mode))) {
            int written = 0;
            int len = bytes.length;
            while (written != len) {
                int chunkLen = len - written;
                int writeRes = Os.write(sfd.v, bytes, written, chunkLen);
                if (writeRes < 0 || writeRes > chunkLen) {
                    throw new IllegalStateException("unexpected write result and chunk to write");
                }
                written += writeRes;
            }

            Os.fsync(sfd.v);
        }

        Os.rename(tmpFilePath, filePath);

        try (var fd = new ScopedFileDescriptor(Os.open(dirPath, OsConstants.O_RDONLY, 0))) {
            Os.fsync(fd.v);
        }
    }
}
