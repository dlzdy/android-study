//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.common.adapter.io;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import io.dcloud.common.adapter.util.DeviceInfo;

public class MiniServerService extends Service {
    String mServiceName = null;

    public MiniServerService() {
    }

    public IBinder onBind(Intent var1) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent var1, int var2, int var3) {
        if(DeviceInfo.sDeviceSdkVer > 5 && var1 != null) {
            this.mServiceName = var1.getStringExtra("mini_server");
            this.startServer();
        }

        return super.onStartCommand(var1, var2, var3);
    }

    public void onStart(Intent var1, int var2) {
        if(DeviceInfo.sDeviceSdkVer <= 5 && var1 != null) {
            this.mServiceName = var1.getStringExtra("mini_server");
            this.startServer();
        }

        super.onStart(var1, var2);
    }

    private void startServer() {
        AdaService var1 = AdaService.getServiceListener(this.mServiceName);
        if(var1 != null) {
            var1.onExecute();
        }

    }

    public void onDestroy() {
        AdaService var1 = AdaService.getServiceListener(this.mServiceName);
        if(var1 != null) {
            var1.onDestroy();
            AdaService.getServiceListener(this.mServiceName);
        }

        super.onDestroy();
    }
}
