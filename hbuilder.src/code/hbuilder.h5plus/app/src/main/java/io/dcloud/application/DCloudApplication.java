//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.application;

import android.app.Application;
import android.content.Context;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.UEH;

public class DCloudApplication extends Application {
    private static final String a = DCloudApplication.class.getSimpleName();
    public boolean isQihooTrafficFreeValidate = true;
    private static DCloudApplication b;
    private static Context c = null;

    public DCloudApplication() {
    }

    public static Context getInstance() {
        return c;
    }

    public static void setInstance(Context var0) {
        if(c == null) {
            c = var0;
        }

    }

    public static DCloudApplication self() {
        return b;
    }

    public void onCreate() {
        super.onCreate();
        b = this;
        setInstance(this.getApplicationContext());
        UEH.catchUncaughtException(this.getApplicationContext());
    }

    public void onLowMemory() {
        super.onLowMemory();
        Logger.e(a, "onLowMemory" + Runtime.getRuntime().maxMemory());
    }

    public void onTrimMemory(int var1) {
        super.onTrimMemory(var1);
        Logger.e(a, "onTrimMemory");
    }
}
