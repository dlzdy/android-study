//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import io.dcloud.common.DHInterface.IActivityHandler;
import io.dcloud.common.DHInterface.IOnCreateSplashView;
import io.dcloud.common.DHInterface.IMgr.MgrType;
import io.dcloud.common.DHInterface.ISysEventListener.SysEventType;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.InvokeExecutorHelper;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.UEH;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.TestUtil;
import io.dcloud.feature.internal.sdk.SDK.IntegratedMode;

abstract class a extends b implements IOnCreateSplashView, d {
    String aa = null;
    String appid = "Main_App";
    EntryProxy c = null;

    a() {
    }

    public void onCreate(final Bundle var1) {
        super.onCreate(var1);
        DeviceInfo.initPath(this.that);
        UEH.catchUncaughtException(this.that);
        Log.d("download_manager", "BaseActivity onCreate");
        TestUtil.print(TestUtil.START_STREAM_APP, "BaseActivity onCreate");
        this.onRuntimePreCreate(var1);
        this.onCreateSplash(this.that);
        Runnable var2 = new Runnable() {
            public void run() {
                a.this.a(a.this.getIntent());
                a.this.aa = "Main_Path_" + a.this.appid;
                io.dcloud.feature.internal.splash.a.a("Main_App");
                Logger.d(a.this.aa, "onCreate appid=" + a.this.appid);
                a.this.onRuntimeCreate(var1);
            }
        };
        (new Handler()).postDelayed(var2, 200L);
    }

    public void setViewAsContentView(View var1) {
        this.setContentView(var1);
    }

    public void closeAppStreamSplash() {
        InvokeExecutorHelper.StreamAppListActivity.invoke("closeSplashPage", new Class[]{Boolean.class}, new Object[]{Boolean.valueOf(true)});
        Logger.d("Main_Path", "BaseActivity.closeAppStreamSplash");
    }

    private void a(Intent var1) {
        Bundle var2 = var1.getExtras();
        if(var2 != null && var2.containsKey("appid")) {
            this.appid = var2.getString("appid");
        }

    }

    protected void onRuntimePreCreate(Bundle var1) {
        Log.d(this.aa, "onRuntimePreCreate appid=" + this.appid);
        this.that.getWindow().setFormat(-3);
        boolean var2 = false;
        if(var2) {
            DeviceInfo.openHardwareAccelerated(this.that, DeviceInfo.HARDWAREACCELERATED_WINDOW, this.that.getWindow());
        }

        io.dcloud.feature.internal.splash.a.a("main", this.that);
        this.a();
    }

    private void a() {
        if(AndroidResources.checkImmersedStatusBar(this.that)) {
            this.a(true);
        }

    }

    @TargetApi(19)
    private void a(boolean var1) {
        Window var2 = this.that.getWindow();
        LayoutParams var3 = var2.getAttributes();
        int var4 = 67108864;
        if(var1) {
            var3.flags |= 67108864;
        } else {
            var3.flags &= -67108865;
        }

        var2.setAttributes(var3);
    }

    public Object onCreateSplash(Context var1) {
        return "splash";
    }

    public void onCloseSplash() {
    }

    protected void onRuntimeCreate(Bundle var1) {
        Logger.d(this.aa, "onRuntimeCreate appid=" + this.appid);
        this.c = EntryProxy.init(this.that);
        this.c.onCreate(this.that, var1, (IntegratedMode)null, (IOnCreateSplashView)null);
    }

    public boolean onCreateOptionsMenu(Menu var1) {
        Logger.d(this.aa, "onCreateOptionsMenu appid=" + this.appid);
        return this.c != null?this.c.onActivityExecute(this.that, SysEventType.onCreateOptionMenu, var1):super.onCreateOptionsMenu(var1);
    }

    public void onPause() {
        super.onPause();
        Logger.d(this.aa, "onPause appid=" + this.appid);
        if(this.c != null) {
            this.c.onPause(this.that);
        }

    }

    public void onResume() {
        super.onResume();
        this.a(this.getIntent());
        Logger.d(this.aa, "onResume appid=" + this.appid);
        if(this.c != null) {
            this.c.onResume(this.that);
        }

    }

    public void updateParam(String var1, Object var2) {
        if("tab_change".equals(var1)) {
            Logger.d("BaseActivity updateParam newintent value(appid)=" + var2);
            Intent var3 = (Intent)BaseInfo.sAppStateMap.get((String)var2);
            if(var3 != null) {
                this.handleNewIntent(var3);
            } else {
                this.c.getCoreHandler().dispatchEvent(MgrType.AppMgr, 21, var2);
            }
        } else if("closewebapp".equals(var1)) {
            Activity var6 = (Activity)var2;
            String var4 = null;
            Bundle var5 = var6.getIntent().getExtras();
            if(var5 != null && var5.containsKey("appid")) {
                var4 = var5.getString("appid");
            }

            if(var6 instanceof IActivityHandler) {
                ((IActivityHandler)var6).closeAppStreamSplash(var4);
            }

            this.c.getCoreHandler().dispatchEvent((MgrType)null, 0, new Object[]{var6, var6.getIntent(), var4});
        }

    }

    public void onNewIntentImpl(Intent var1) {
        super.onNewIntentImpl(var1);
        Logger.d("syncStartApp", "BaseActivity onNewIntent appid=" + this.appid);
        this.handleNewIntent(var1);
    }

    protected void handleNewIntent(Intent var1) {
        this.setIntent(var1);
        this.a(var1);
        Logger.d("syncStartApp", "BaseActivity handleNewIntent =" + this.appid + ";" + (var1.getFlags() != 274726912));
        if(var1.getFlags() != 274726912 && this.c != null) {
            this.c.onNewIntent(this.that, var1);
        }

    }

    public void onRequestPermissionsResult(int var1, String[] var2, int[] var3) {
        if(this.c != null) {
            this.c.onActivityExecute(this.that, SysEventType.onRequestPermissionsResult, new Object[]{Integer.valueOf(var1), var2, var3});
        }

    }

    public void onDestroy() {
        super.onDestroy();
        io.dcloud.feature.internal.splash.a.b("Main_App");
        Logger.d(this.aa, "onDestroy appid=" + this.appid);
        if(this.c != null) {
            this.c.onStop(this.that);
        }

        if(BaseInfo.mLaunchers != null) {
            BaseInfo.mLaunchers.clear();
        }

        MessageHandler.removeCallbacksAndMessages();
    }

    public boolean onKeyEventExecute(SysEventType var1, int var2, KeyEvent var3) {
        return this.c != null?this.c.onActivityExecute(this.that, var1, new Object[]{Integer.valueOf(var2), var3}):false;
    }

    public boolean onKeyDown(int var1, KeyEvent var2) {
        if(!BaseInfo.USE_ACTIVITY_HANDLE_KEYEVENT) {
            return super.onKeyDown(var1, var2);
        } else {
            boolean var3 = false;
            if(var2.getRepeatCount() == 0) {
                var3 = this.onKeyEventExecute(SysEventType.onKeyDown, var1, var2);
            } else {
                var3 = this.onKeyEventExecute(SysEventType.onKeyLongPress, var1, var2);
            }

            return var3?var3:super.onKeyDown(var1, var2);
        }
    }

    public void onBackPressed() {
        if(!BaseInfo.USE_ACTIVITY_HANDLE_KEYEVENT) {
            super.onBackPressed();
        } else {
            boolean var1 = this.onKeyEventExecute(SysEventType.onKeyUp, 4, (KeyEvent)null);
            if(!var1 && this.c != null) {
                this.c.destroy(this.that);
                super.onBackPressed();
            }

        }
    }

    public boolean onKeyUp(int var1, KeyEvent var2) {
        if(!BaseInfo.USE_ACTIVITY_HANDLE_KEYEVENT) {
            return super.onKeyUp(var1, var2);
        } else {
            Logger.d(this.aa, "onKeyUp");
            boolean var3 = false;
            if(var1 != 4 && this.c != null) {
                var3 = this.c.onActivityExecute(this.that, SysEventType.onKeyUp, new Object[]{Integer.valueOf(var1), var2});
            }

            return var3?var3:super.onKeyUp(var1, var2);
        }
    }

    public boolean onKeyLongPress(int var1, KeyEvent var2) {
        if(!BaseInfo.USE_ACTIVITY_HANDLE_KEYEVENT) {
            return super.onKeyLongPress(var1, var2);
        } else {
            boolean var3 = this.c != null?this.c.onActivityExecute(this.that, SysEventType.onKeyLongPress, new Object[]{Integer.valueOf(var1), var2}):false;
            return var3?var3:super.onKeyLongPress(var1, var2);
        }
    }

    public void onConfigurationChanged(Configuration var1) {
        try {
            Logger.d(this.aa, "onConfigurationChanged");
            int var2 = this.getResources().getConfiguration().orientation;
            if(this.c != null) {
                this.c.onConfigurationChanged(this.that, var2);
            }

            super.onConfigurationChanged(var1);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public void onActivityResult(int var1, int var2, Intent var3) {
        Logger.d(this.aa, "onActivityResult");
        if(this.c != null) {
            this.c.onActivityExecute(this.that, SysEventType.onActivityResult, new Object[]{Integer.valueOf(var1), Integer.valueOf(var2), var3});
        }

    }

    public void onSaveInstanceState(Bundle var1) {
        if(var1 != null && this.getIntent() != null && this.getIntent().getExtras() != null) {
            var1.putAll(this.getIntent().getExtras());
        }

        Logger.d(this.aa, "onSaveInstanceState");
        if(this.c != null) {
            this.c.onActivityExecute(this.that, SysEventType.onSaveInstanceState, new Object[]{var1});
        }

        super.onSaveInstanceState(var1);
    }

    public void onLowMemory() {
        super.onLowMemory();
        Logger.d(this.aa, "onLowMemory");
        this.displayBriefMemory();
    }

    protected void displayBriefMemory() {
        ActivityManager var1 = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo var2 = new MemoryInfo();
        var1.getMemoryInfo(var2);
        Logger.i("mabo", "===============================");
        Logger.i("mabo", "程序最高可用内存:" + Runtime.getRuntime().maxMemory() / 1048576L + "M");
        Logger.i("mabo", "程序总共占用内存:" + Runtime.getRuntime().totalMemory() / 1048576L + "M");
        Logger.i("mabo", "程序所占剩余内存:" + Runtime.getRuntime().freeMemory() / 1048576L + "M");
        Logger.i("mabo", "系统剩余内存:" + var2.availMem / 1048576L + "M");
        Logger.i("mabo", "系统是否处于低内存运行：" + var2.lowMemory);
        Logger.i("mabo", "当系统剩余内存低于" + var2.threshold / 1048576L + "M" + "时就看成低内存运行");
    }

    public Resources getResources() {
        Resources var1 = super.getResources();
        Configuration var2 = var1.getConfiguration();
        float var3 = 1.0F;

        try {
            var3 = Float.parseFloat(BaseInfo.sFontScale);
            if(var2.fontScale != var3) {
                var2.fontScale = var3;
            }
        } catch (NumberFormatException var5) {
            if("none".equals(BaseInfo.sFontScale) && var2.fontScale != 1.0F) {
                var2.fontScale = 1.0F;
            }
        }

        return var1;
    }
}
