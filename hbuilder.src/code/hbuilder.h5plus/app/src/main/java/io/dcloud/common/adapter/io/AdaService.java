//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.common.adapter.io;

import android.content.Context;
import android.content.Intent;

import java.util.HashMap;

import io.dcloud.common.adapter.util.Logger;

public abstract class AdaService {
    static final String TAG = "AdaService";
    protected Context mContextWrapper = null;
    private String mServiceName = null;
    static HashMap<String, AdaService> mServicesHandler = new HashMap(2);

    protected AdaService(Context var1, String var2) {
        this.mContextWrapper = var1;
        this.mServiceName = var2;
    }

    public final void startMiniServer() {
        mServicesHandler.put(this.mServiceName, this);
        Intent var1 = new Intent(this.mContextWrapper, MiniServerService.class);
        var1.putExtra("mini_server", this.mServiceName);
        this.mContextWrapper.startService(var1);
        Logger.d("AdaService", "pname=" + this.mContextWrapper.getPackageName() + " startMiniServer");
    }

    public final void stopMiniServer() {
        Intent var1 = new Intent(this.mContextWrapper, MiniServerService.class);
        var1.putExtra("mini_server", this.mServiceName);
        this.mContextWrapper.stopService(var1);
        Logger.d("AdaService", "pname=" + this.mContextWrapper.getPackageName() + " stopMiniServer");
    }

    public abstract void onExecute();

    public abstract void onDestroy();

    public static final AdaService getServiceListener(String var0) {
        return (AdaService)mServicesHandler.get(var0);
    }

    public static final void removeServiceListener(String var0) {
        mServicesHandler.remove(var0);
    }
}
