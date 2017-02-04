package com.baculsoft.sample.realmexporter.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * @author Budi Oktaviyan Suryanto (budioktaviyans@gmail.com)
 */
public final class Permissions {
    private static volatile Permissions INSTANCE = null;

    public static synchronized Permissions get() {
        if (INSTANCE == null) {
            INSTANCE = new Permissions();
        }

        return INSTANCE;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasSelfPermissions(Activity activity, String[] permissions) {
        if (!isMarshmallow()) {
            return true;
        }

        for (String permission : permissions) {
            if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasSelfPermission(Activity activity, String permission) {
        return !isMarshmallow() || activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}