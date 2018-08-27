package com.hx.lib_hx.custom.image_preview;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

public class AndroidLifecycleUtils {
    public static boolean canLoadImage(Fragment fragment) {
        if (fragment == null) {
            return true;
        }

        FragmentActivity activity = fragment.getActivity();

        return canLoadImage(activity);
    }

    public static boolean canLoadImage(Context context) {
        if (context == null) {
            return true;
        }

        if (!(context instanceof AppCompatActivity)) {
            return true;
        }

        AppCompatActivity activity = (AppCompatActivity) context;
        return canLoadImage(activity);
    }

    public static boolean canLoadImage(AppCompatActivity activity) {
        if (activity == null) {
            return true;
        }

        boolean destroyed = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 &&
                activity.isDestroyed();

        if (destroyed || activity.isFinishing()) {
            return false;
        }

        return true;
    }
}
