//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import io.dcloud.common.DHInterface.IActivityHandler;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IReflectAble;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.InvokeExecutorHelper;
import io.dcloud.common.adapter.util.InvokeExecutorHelper.InvokeExecutor;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.constant.DataInterface;
import io.dcloud.common.util.ImageLoaderUtil;
import io.dcloud.feature.internal.reflect.BroadcastReceiver;
import io.src.dcloud.adapter.DCloudAdapterUtil;
import io.src.dcloud.adapter.DCloudBaseActivity;

abstract class b extends DCloudBaseActivity implements IActivityHandler, IReflectAble {
    private HashMap<String, c> a = new HashMap();
    InvokeExecutor d;
    private ServiceConnection bbbb = new ServiceConnection() {
        public void onServiceConnected(ComponentName var1, IBinder var2) {
            b.this.d = InvokeExecutorHelper.createIDownloadService(var2);
            Logger.d("stream_manager", "onServiceConnected ;tService =" + (b.this.d != null));
        }

        public void onServiceDisconnected(ComponentName var1) {
            b.this.d = null;
        }
    };
    static final InvokeExecutor e;
    static final int f;
    static final int g;
    static final int h;
    static final int i;
    static final int j;
    static final Class[] k;
    Class[] l;
    private static String c;
    final Class[] m;
    final Class[] n;
    final Class[] o;

    b() {
        this.l = new Class[]{String.class, Integer.TYPE, String.class, String.class};
        this.m = new Class[]{String.class, Integer.TYPE, Integer.TYPE, String.class};
        this.n = new Class[]{String.class, String.class, String.class, Integer.TYPE, String.class, String.class};
        this.o = new Class[]{String.class, String.class};
    }

    public void onCreate(Bundle var1) {
        super.onCreate(var1);
        AndroidResources.initAndroidResources(this.that);
        ImageLoaderUtil.initImageLoader(this.that);
        if(this.isStreamAppMode()) {
            this.bindDCloudServices();
        }

    }

    public boolean isStreamAppMode() {
        return true;
    }

    public Intent registerReceiver(BroadcastReceiver var1, IntentFilter var2, String var3, Handler var4) {
        Intent var5 = null;
        c var6 = new c(var1, var2);

        try {
            var5 = this.registerReceiver(var6, var2, var3, var4);
            this.a.put(var1.toString(), var6);
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return var5;
    }

    public Intent registerReceiver(BroadcastReceiver var1, IntentFilter var2) {
        Intent var3 = null;
        c var4 = new c(var1, var2);

        try {
            var3 = this.registerReceiver(var4, var2);
            this.a.put(var1.toString(), var4);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return var3;
    }

    public void unregisterReceiver(BroadcastReceiver var1) {
        c var2 = (c)this.a.remove(var1.toString());
        if(var2 != null) {
            this.unregisterReceiver(var2);
        }

    }

    private void a() {
        Collection var1 = this.a.values();
        Iterator var2 = var1.iterator();

        while(var2.hasNext()) {
            c var3 = (c)var2.next();
            this.unregisterReceiver(var3);
        }

        this.a.clear();
    }

    public void onDestroy() {
        super.onDestroy();

        try {
            this.a();
            if(this.isStreamAppMode()) {
                this.b();
                this.unbindDCloudServices();
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    private void b() {
        Intent var1 = new Intent();
        var1.setClassName(this.getPackageName(), DCloudAdapterUtil.getDcloudDownloadService());
        var1.setAction("com.qihoo.appstore.plugin.streamapp.ACTION_PUSH_TO_PLUGIN");
        var1.putExtra("_____flag____", true);
        var1.putExtra("_____collect____", true);
        this.startService(var1);
    }

    public void unbindDCloudServices() {
        this.unbindService(this.bbbb);
    }

    public void bindDCloudServices() {
        Class var1 = null;

        try {
            var1 = Class.forName(DCloudAdapterUtil.getDcloudDownloadService());
        } catch (ClassNotFoundException var3) {
            var3.printStackTrace();
        }

        if(var1 != null) {
            Intent var2 = new Intent(this.that, var1);
            this.bindService(var2, this.bbbb, Context.BIND_AUTO_CREATE);
            Logger.d("stream_manager", "bindDCloudServices");
        }

    }

    public boolean raiseFilePriority(String var1, String var2) {
        Logger.d("stream_manager", "raiseFilePriority filePath=" + var1 + ";tService =" + (this.d != null));
        if(this.d != null) {
            try {
                return this.d.invoke("setCurrentPage", this.getUrlByFilePath(var2, var1), false);
            } catch (Exception var4) {
                Logger.d("stream_manager", new Object[]{"addAppStreamTask filePath=" + var1, var4});
            }
        }

        return false;
    }

    public void addAppStreamTask(String var1, String var2) {
        Logger.d("stream_manager", "addAppStreamTask filePath=" + var1 + ";tService =" + (this.d != null));
        if(this.d != null) {
            try {
                this.d.invoke("addSimpleFileTask", k, new Object[]{this.getUrlByFilePath(var2, var1), Integer.valueOf(f), var1, var2});
            } catch (Exception var4) {
                Logger.d("stream_manager", new Object[]{"addAppStreamTask filePath=" + var1, var4});
            }
        }

    }

    public boolean queryUrl(String var1, String var2) {
        try {
            int var3 = ((Integer)e.invoke("checkPageOrResourceStatus", new Class[]{String.class, String.class}, new Object[]{var1, var2})).intValue();
            boolean var4 = var3 == g;
            Logger.d("stream_manager", "queryUrl filePath=" + var1 + ";ret =" + var4);
            return var4;
        } catch (Exception var5) {
            var5.printStackTrace();
            return true;
        }
    }

    public void resumeAppStreamTask(String var1) {
        Logger.d("Main_Path", "resumeAppStreamTask app=" + var1 + ";tService =" + (this.d != null));

        try {
            if(this.d != null) {
                this.d.invoke("resumeDownload", var1);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public void downloadSimpleFileTask(IApp var1, String var2, String var3, String var4) {
        if(this.d != null && !(new File(var3)).exists()) {
            this.d.invoke("addSimpleFileTask", this.l, new Object[]{var2, Integer.valueOf(f), var3, var4});
        }

    }

    public String getUrlByFilePath(String var1, String var2) {
        String var3 = DataInterface.getBaseUrl();
        String var4;
        if(this.d != null) {
            var3 = this.d.invoke("getAppRootUrl", var1);
            if(TextUtils.isEmpty(var3)) {
                var4 = (String)InvokeExecutorHelper.AppStreamUtils.invoke("getJsonFilePath", new Class[]{String.class}, new Object[]{var1});
                String var5 = "";

                try {
                    byte[] var6 = DHFile.readAll(var4);
                    if(var6 != null) {
                        var5 = new String(var6);
                    }

                    JSONObject var7 = new JSONObject(var5);
                    var3 = var7.getString("rooturl");
                } catch (Exception var8) {
                    ;
                }
            }
        }

        var4 = var2.substring(var2.indexOf(c) + c.length());
        return var3 + var4;
    }

    public Context getContext() {
        return this.that;
    }

    public void commitPointData0(String var1, int var2, int var3, String var4) {
        if(this.d != null) {
            this.d.invoke("commitPointData0", this.m, new Object[]{var1, Integer.valueOf(var2), Integer.valueOf(var3), var4});
        }

    }

    public void commitPointData(String var1, String var2, String var3, int var4, String var5, String var6) {
        if(this.d != null) {
            this.d.invoke("commitPointData", this.n, new Object[]{var1, var2, var3, Integer.valueOf(var4), var5, var6});
        }

    }

    public void commitActivateData(String var1, String var2) {
        if(this.d != null) {
            this.d.invoke("commitActivateData", this.o, new Object[]{var1, var2});
        }

    }

    static {
        e = InvokeExecutorHelper.AppStreamUtils;
        f = e != null?e.getInt("PRIORITY_MIN"):0;
        g = e != null?e.getInt("CONTRACT_STATUS_SUCCESS"):0;
        h = e != null?e.getInt("CONTRACT_TYPE_WAP2APP_INDEX_ZIP"):0;
        i = e != null?e.getInt("CONTRACT_TYPE_STREAM_MAIN_PAGE"):0;
        j = e != null?e.getInt("CONTRACT_TYPE_STREAM_APP"):0;
        k = new Class[]{String.class, Integer.TYPE, String.class, String.class};
        c = "www/";
    }
}
