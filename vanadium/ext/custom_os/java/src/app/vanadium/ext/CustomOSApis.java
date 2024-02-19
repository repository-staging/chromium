package app.vanadium.ext;

import android.os.Environment;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CustomOSApis {

    private static final String TAG = "cr_CustomOSApis";

    public static boolean isExecmemBlocked() {
        try {
            Method isExecmemBlocked = Environment.class.getDeclaredMethod("isExecmemBlocked");
            if (isExecmemBlocked.getReturnType() != Boolean.TYPE) {
                Log.e(TAG, "Unexpected return type: isExecmemBlocked must return boolean");
                return false;
            }

            var res = isExecmemBlocked.invoke(null);
            return (boolean) res;
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

}