//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import io.dcloud.common.DHInterface.SplashView;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.ui.FrameSwitchView;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.InvokeExecutorHelper;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.constant.DataInterface;
import io.dcloud.common.constant.StringConst;
import io.dcloud.common.util.AppStatus;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.DialogUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.TestUtil;
import io.dcloud.common.util.TestUtil.PointTime;
import io.dcloud.feature.internal.splash.SplashView4StreamApp;
import io.src.dcloud.adapter.DCloudAdapterUtil;

public class WebAppActivity extends io.dcloud.a {
    public static final long ONE_SECOND = 1000L;
    public static final long SPLASH_SECOND = 5000L;
    private long y;
    private boolean z;
    private boolean A;
    private Handler B = new Handler();
    private final String C = "remove-app_action";
    BroadcastReceiver p;
    static WebAppActivity q = null;
    Bitmap r = null;
    View s = null;
    long t = 0L;
    boolean u = true;
    FrameLayout v = null;
    LinearLayout w = null;
    FrameLayout x = null;

    public WebAppActivity() {
    }

    public void onCreate(Bundle var1) {
        Log.d("WebAppActivity", "onCreate");
        super.onCreate(var1);
        if(!this.A) {
            TestUtil.record("run_5app_time_key");
        }

        q = this;
        this.b();
        IntentFilter var2 = new IntentFilter("remove-app_action");
        var2.addAction("apk_download_end");
        var2.addAction(this.getPackageName() + ".streamdownload.downloadfinish");
        this.p = new BroadcastReceiver() {
            public void onReceive(Context var1, Intent var2) {
                String var3 = var2.getAction();
                String var4;
                if(TextUtils.equals(var3, "remove-app_action")) {
                    var4 = var2.getStringExtra("appid");
                    WebAppActivity.this.c.getCoreHandler().removeStreamApp(var4);
                } else if(TextUtils.equals(var3, "apk_download_end")) {
                    var4 = var2.getStringExtra("apkPath");
                    String var5 = var2.getStringExtra("msg");
                    DialogUtil.showInstallAPKDialog(WebAppActivity.q, var5, var4);
                } else if(TextUtils.equals(var3, WebAppActivity.this.getPackageName() + ".streamdownload.downloadfinish")) {
                    var4 = var2.getStringExtra("flag");
                    int var9 = var2.getIntExtra("status", 2);
                    if((var4.compareTo("wap2app_index") == 0 || var4.compareTo("appstream") == 0 || var4.compareTo("StreamAppWgt") == 0) && var9 == b.g) {
                        String var6 = var2.getStringExtra("appid");
                        int var7 = var2.getIntExtra("type", -1);
                        if(var7 == b.h || var7 == b.i || var7 == b.j) {
                            InvokeExecutorHelper.QihooInnerStatisticUtil.invoke("doEvent", new Class[]{String.class, String.class}, new Object[]{var6, "event_add_shortcut"});
                            Logger.d("syncStartApp", "download MAIN_PAGE nAppid=" + var6);
                            Intent var8 = new Intent(WebAppActivity.this.getIntent());
                            var8.putExtra("just_download", true);
                            var8.putExtra("has_stream_splash", false);
                            var8.putExtra("is_stream_app", true);
                            var8.putExtra("appid", var6);
                            WebAppActivity.this.handleNewIntent(var8);
                            InvokeExecutorHelper.QHPushHelper.invoke("registerApp", var6, false);
                        }
                    }
                }

            }
        };
        this.registerReceiver(this.p, var2);
        FrameSwitchView var3 = FrameSwitchView.getInstance(this.that);
        if(!var3.isInit()) {
            var3.initView();
        }

        this.a();
    }

    private void a() {
        Intent var1 = this.getIntent();
        boolean var2 = var1 != null?var1.getBooleanExtra("__plugin_auto_hide__", false):false;
        Log.d("WebAppActivity", "checkAutoHide " + var2);
        if(var2) {
            Intent var3 = new Intent();
            String var4 = var1.getStringExtra("__plugin_auto_hide_show_pname__");
            String var5 = var1.getStringExtra("__plugin_auto_hide_show_activity__");
            var3.putExtra("__plugin_auto_hide_show_pname__", true);
            var3.setClassName(var4, var5);
            this.that.startActivity(var3);
            this.that.overridePendingTransition(0, 0);
            Log.d("WebAppActivity", "checkAutoHide return mini package " + var5);
        }

    }

    private void b() {
        Intent var1 = this.getIntent();
        if(var1 != null) {
            this.A = var1.getBooleanExtra("is_stream_app", false);
            if(!this.A) {
                var1.removeExtra("appid");
            }
        }

    }

    public void onDestroy() {
        Log.d("WebAppActivity", "onDestroy");
        super.onDestroy();
        this.unregisterReceiver(this.p);
        q = null;
        io.dcloud.common.bb.bb.a.a();
        if(this.B != null) {
            this.B.removeCallbacksAndMessages((Object)null);
        }

        FrameSwitchView.getInstance(this.that).clearData();
        PlatformUtil.invokeMethod("io.src.dcloud.adapter.DCloudAdapterUtil", "WebAppDestroy");
    }

    private Bitmap a(String var1, String var2, String var3) {
        Bitmap var4 = null;

        try {
            if(!TextUtils.isEmpty(var1) && (new File(var1)).exists()) {
                Logger.d("Main_Path", "use splashPath=" + var1);
                var4 = BitmapFactory.decodeFile(var1);
                if(var4 != null) {
                    this.u = false;

                    try {
                        DHFile.deleteFile(var1);
                    } catch (IOException var6) {
                        var6.printStackTrace();
                    }

                    Logger.d("Main_Path", "use splashPath=" + var1);
                }
            }

            if(var4 == null && !TextUtils.isEmpty(var2) && (new File(var2)).exists()) {
                Logger.d("Main_Path", "use splashPath=" + var2);
                var4 = BitmapFactory.decodeFile(var2);
            }

            if(var4 == null && !TextUtils.isEmpty(var3) && (new File(var3)).exists()) {
                Logger.d("Main_Path", "use splashPath=" + var3);
                var4 = BitmapFactory.decodeFile(var3);
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return var4;
    }

    public Object onCreateSplash(Context var1) {
        Intent var2 = this.getIntent();
        String var3 = var2.getStringExtra("appid");
        if(this.s != null && this.s.getTag() != null) {
            if(this.s.getTag().equals(var3)) {
                return null;
            }

            this.closeAppStreamSplash(this.s.getTag().toString());
        }

        this.u = true;
        Logger.d("WebAppActivity", "onCreateSplash;intent=" + var2);
        boolean var4 = var2.getBooleanExtra("__start_first_web__", false);
        if(var4) {
            return null;
        } else {
            boolean var5 = var2.getBooleanExtra("__splash_view__", true);
            if(!var5) {
                return null;
            } else {
                Logger.d("WebAppActivity", "onCreateSplash hasSplash=" + var5);
                boolean var6 = var2.getBooleanExtra("__plugin_auto_hide__", false);
                if(var6) {
                    return null;
                } else {
                    String var7 = var2.getStringExtra("__splash_mode__");
                    var2.removeExtra("__splash_mode__");
                    if(null == var7 || "".equals(var7.trim()) || !"auto".equals(var7) && !"default".equals(var7)) {
                        var7 = "auto";
                    }

                    Logger.d("WebAppActivity", "onCreateSplash __splash_mode__=" + var7);
                    if(var2 != null && var2.getBooleanExtra("hide_stream_splash", false)) {
                        this.setViewAsContentView(new View(var1));
                        this.t = System.currentTimeMillis();
                        this.z = true;
                        return null;
                    } else {
                        String var8;
                        String var9;
                        String var10;
                        if(var2 != null && var2.getBooleanExtra("has_stream_splash", false)) {
                            if(this.s == null) {
                                if("auto".equals(var7)) {
                                    var8 = StringConst.STREAMAPP_KEY_ROOTPATH + "splash_temp/" + var3 + ".png";
                                    var9 = var2.getStringExtra("app_splash_path");
                                    var10 = StringConst.STREAMAPP_KEY_ROOTPATH + "splash/" + var3 + ".png";
                                    this.r = this.a(var8, var9, var10);
                                    if(this.r != null) {
                                        this.s = new SplashView(this.that, this.r);
                                        if(!this.u) {
                                            ((SplashView)this.s).showWaiting(SplashView.STYLE_BLACK);
                                        }
                                    }
                                }

                                if(this.s == null) {
                                    if(BaseInfo.isShowTitleBar(var1) && !BaseInfo.isStreamSDK) {
                                        this.s = new WebAppActivity.a(this.that);
                                    } else {
                                        var8 = var2.getStringExtra("app_icon");
                                        if(!TextUtils.isEmpty(var8) && (new File(var8)).exists()) {
                                            this.r = BitmapFactory.decodeFile(var8);
                                        }

                                        if(this.r == null) {
                                            this.a(var3);
                                        }

                                        this.s = new SplashView4StreamApp(var1, this.r, var2.getStringExtra("__name__"));
                                    }
                                }
                            }

                            this.s.setTag(var3);
                            this.setViewAsContentView(this.s);
                            this.t = System.currentTimeMillis();
                            this.z = true;
                            return null;
                        } else if(this.s == null) {
                            try {
                                if("auto".equals(var7)) {
                                    var8 = StringConst.STREAMAPP_KEY_ROOTPATH + "splash_temp/" + var3 + ".png";
                                    var9 = var2.getStringExtra("app_splash_path");
                                    var10 = StringConst.STREAMAPP_KEY_ROOTPATH + "splash/" + var3 + ".png";
                                    this.r = this.a(var8, var9, var10);
                                    if(this.r == null && !BaseInfo.isForQihooHelper(var1) && !BaseInfo.isStreamApp(var1) && !BaseInfo.isForQihooBrowser(var1)) {
                                        SharedPreferences var11 = PlatformUtil.getOrCreateBundle("pdr");
                                        String var12 = var11.getString("update_splash_img_path", "");
                                        if(!TextUtils.isEmpty(var12)) {
                                            try {
                                                if(PdrUtil.isDeviceRootDir(var12)) {
                                                    this.r = BitmapFactory.decodeFile(var12);
                                                } else {
                                                    InputStream var13 = this.getResources().getAssets().open(var12);
                                                    this.r = BitmapFactory.decodeStream(var13);
                                                    var13.close();
                                                }
                                            } catch (Exception var14) {
                                                this.r = null;
                                            }
                                        }

                                        if(this.r == null) {
                                            this.r = BitmapFactory.decodeResource(this.getResources(), RInformation.DRAWABLE_SPLASH);
                                        }
                                    }

                                    if(this.r != null) {
                                        this.s = new SplashView(this.that, this.r);
                                        if(!this.u) {
                                            ((SplashView)this.s).showWaiting(SplashView.STYLE_BLACK);
                                        }
                                    }
                                }

                                if(this.s == null) {
                                    if(BaseInfo.isShowTitleBar(var1) && !BaseInfo.isStreamSDK) {
                                        this.s = new WebAppActivity.a(this.that);
                                    } else {
                                        var8 = var2.getStringExtra("app_icon");
                                        if(!TextUtils.isEmpty(var8) && (new File(var8)).exists()) {
                                            this.r = BitmapFactory.decodeFile(var8);
                                        }

                                        if(this.r == null) {
                                            this.a(var3);
                                        }

                                        Log.d("Main_Path", "use defaultSplash");
                                        this.s = new SplashView4StreamApp(var1, this.r, var2.getStringExtra("__name__"), "正在启动流应用");
                                    }
                                }

                                this.s.setTag(var3);
                                this.setViewAsContentView(this.s);
                                this.t = System.currentTimeMillis();
                                this.z = true;
                            } catch (Exception var15) {
                                var15.printStackTrace();
                            }

                            return null;
                        } else {
                            return null;
                        }
                    }
                }
            }
        }
    }

    public void updateParam(String var1, Object var2) {
        if("progress".equals(var1)) {
            if(this.s instanceof WebAppActivity.a) {
                ((WebAppActivity.a)this.s).a(((Integer)var2).intValue());
            }
        } else if("setProgressView".equals(var1)) {
            this.setProgressView();
        } else {
            super.updateParam(var1, var2);
        }

    }

    public void updateSplash(String var1) {
        if(this.s != null && this.s instanceof SplashView4StreamApp) {
            ((SplashView4StreamApp)this.s).a(var1);
        }

    }

    private void a(String var1) {
        String var2 = DataInterface.getIconImageUrl(var1, this.getResources().getDisplayMetrics().widthPixels + "");
        ImageLoader.getInstance().loadImage(var2, new ImageLoadingListener() {
            public void onLoadingStarted(String var1, View var2) {
            }

            public void onLoadingFailed(String var1, View var2, FailReason var3) {
            }

            public void onLoadingComplete(String var1, View var2, Bitmap var3) {
                if(WebAppActivity.this.s != null && WebAppActivity.this.s instanceof SplashView4StreamApp) {
                    ((SplashView4StreamApp)WebAppActivity.this.s).a(var3);
                }

            }

            public void onLoadingCancelled(String var1, View var2) {
            }
        });
    }

    public void showSplashWaiting() {
        if(this.u && this.s instanceof SplashView) {
            ((SplashView)this.s).showWaiting();
        }

    }

    public void setViewAsContentView(View var1) {
        if(this.v == null) {
            this.v = new FrameLayout(this.that);
            this.w = DCloudAdapterUtil.getWebAppRootView(this.that);
            if(this.w != null) {
                LayoutParams var2 = new LayoutParams(-1, -1);
                this.v.setLayoutParams(var2);
                this.w.addView(this.v);
                this.setContentView(this.w);
            } else {
                this.setContentView(this.v);
            }
        }

        String var7 = this.getIntent().getStringExtra("appid");
        PlatformUtil.invokeMethod("io.src.dcloud.adapter.DCloudAdapterUtil", "checkNeedTitleView", (Object)null, new Class[]{Activity.class, String.class}, new Object[]{this.that, var7});
        if(AndroidResources.checkImmersedStatusBar(this.that) && VERSION.SDK_INT >= 23) {
            android.widget.FrameLayout.LayoutParams var3 = new android.widget.FrameLayout.LayoutParams(-1, -1);
            var3.topMargin = -1;
            this.v.setLayoutParams(var3);
        }

        int var8 = this.v.indexOfChild(this.s);
        int var4 = this.v.getChildCount();
        if(var4 > 0) {
            for(int var5 = var4 - 1; var5 >= 0; --var5) {
                View var6 = this.v.getChildAt(var5);
                if(var6 != var1) {
                    if("AppRootView".equals(var6.getTag())) {
                        this.v.addView(var1, var5);
                        this.v.removeView(var6);
                        break;
                    }

                    if(var5 == 0) {
                        if(var6 == this.s) {
                            this.v.addView(var1, 0);
                        } else if(var8 > 0) {
                            this.v.addView(var1, var8 - 1);
                        } else {
                            this.v.addView(var1);
                        }
                    }
                }
            }
        } else {
            this.v.addView(var1);
        }

        TestUtil.print(TestUtil.START_APP_SET_ROOTVIEW, "启动" + var1);
        if(AndroidResources.checkImmersedStatusBar(this.that)) {
            io.dcloud.common.bb.bb.a.a(this.v);
        }

    }

    public void setProgressView() {
        if(BaseInfo.isShowTitleBar(this.that) && !BaseInfo.isStreamSDK) {
            View var1 = null;

            for(int var2 = 0; var2 < this.v.getChildCount(); ++var2) {
                var1 = this.v.getChildAt(var2);
                if(null != var1 && var1 == this.s) {
                    this.v.removeViewAt(var2);
                    break;
                }
            }

            this.s = new WebAppActivity.a(this.that);
            this.v.addView(this.s);
        }

    }

    public boolean isCanRefresh() {
        if(!BaseInfo.isShowTitleBar(this.that)) {
            return false;
        } else if(null == this.v) {
            return false;
        } else {
            View var1 = null;

            for(int var2 = 0; var2 < this.v.getChildCount(); ++var2) {
                var1 = this.v.getChildAt(var2);
                if(null != var1 && var1 instanceof SplashView) {
                    return false;
                }
            }

            return true;
        }
    }

    public void setWebViewIntoPreloadView(View var1) {
        if(this.x == null) {
            this.x = new FrameLayout(this.that);
            this.v.addView(this.x, 0);
        }

        this.x.addView(var1);
    }

    public void closeAppStreamSplash(String var1) {
        Logger.d("webappActivity closeAppStreamSplash");
        DCloudAdapterUtil.Plugin2Host_closeAppStreamSplash(var1);
        if(this.r != null && !this.r.isRecycled()) {
            try {
                this.r.recycle();
                this.r = null;
            } catch (Exception var3) {
                var3.printStackTrace();
            }
        }

        if(this.s != null) {
            Logger.d("webappActivity removeView mSplashView");
            if(this.s instanceof WebAppActivity.a) {
                ((WebAppActivity.a)this.s).a();
            } else {
                this.v.removeView(this.s);
            }

            this.s = null;
        }

        this.z = false;
        this.t = 0L;
    }

    public static AlertDialog showDownloadDialog(String var0, final OnClickListener var1) {
        if(q != null && q.z) {
            AlertDialog var2 = (new Builder(q.that)).create();
            var2.setTitle("提示");
            if(PointTime.mEc == 20) {
                var2.setMessage("检测到设备无网络，请检查系统网络设置！");
            } else if(var0 != null) {
                var2.setMessage("进入流应用" + var0 + "失败" + getErrorTipMsg());
            } else {
                var2.setMessage("进入流应用失败" + getErrorTipMsg());
            }

            var2.setCanceledOnTouchOutside(false);
            var2.setButton(-1, "重试", var1);
            var2.setButton(-2, "关闭", new OnClickListener() {
                public void onClick(DialogInterface var1x, int var2) {
                    WebAppActivity.q.updateParam("closewebapp", WebAppActivity.q);
                    var1.onClick(var1x, var2);
                }
            });
            var2.setOnKeyListener(new OnKeyListener() {
                public boolean onKey(DialogInterface var1x, int var2, KeyEvent var3) {
                    if(var3.getAction() == 1 && var2 == 4) {
                        WebAppActivity.q.updateParam("closewebapp", WebAppActivity.q);
                        var1.onClick(var1x, -2);
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            var2.show();
            return var2;
        } else {
            return null;
        }
    }

    public void onWindowFocusChanged(boolean var1) {
        super.onWindowFocusChanged(var1);
        PlatformUtil.SCREEN_WIDTH(this.that);
        PlatformUtil.SCREEN_HEIGHT(this.that);
        PlatformUtil.MESURE_SCREEN_STATUSBAR_HEIGHT(this.that);
    }

    private void c() {
        String var1 = "再按一次返回键关闭流应用";
        if(this.getIntent() != null && this.getIntent().hasExtra("__name__")) {
            var1 = "再按一次返回键关闭" + this.getIntent().getStringExtra("__name__");
        }

        Toast.makeText(this.that, var1, Toast.LENGTH_SHORT).show();
    }

    public void onBackPressed() {
        Log.d("WebAppActivity", "onBackPressed");
        if(this.A && this.z) {
            if("all".equalsIgnoreCase(BaseInfo.sSplashExitCondition)) {
                super.onBackPressed();
            } else if(this.t == 0L || System.currentTimeMillis() - this.t >= 5000L) {
                if(this.y == 0L) {
                    this.c();
                    this.y = System.currentTimeMillis();
                    this.B.postDelayed(new Runnable() {
                        public void run() {
                            WebAppActivity.this.y = 0L;
                        }
                    }, 1000L);
                } else if(System.currentTimeMillis() - this.y > 1000L) {
                    this.y = 0L;
                    this.c();
                } else {
                    String var1 = this.appid;
                    if(var1 == null && this.getIntent() != null && this.getIntent().hasExtra("appid")) {
                        var1 = this.getIntent().getStringExtra("appid");
                    }

                    if(var1 != null) {
                        if(PointTime.hasPointTime(TestUtil.STREAM_APP_POINT)) {
                            PointTime.getPointTime(TestUtil.STREAM_APP_POINT).point();
                        }

                        PointTime.commit(this, var1, 2, 2, "");
                        AppStatus.setAppStatus(var1, 0);
                    } else {
                        Logger.e("onBackPressed appid 不能为null的");
                    }

                    Logger.i("WebAppActivity.onBackPressed finish");
                    this.finish();
                    PlatformUtil.invokeMethod("io.dcloud.appstream.StreamAppMainActivity", "closeSplashPage", (Object)null, new Class[]{Boolean.TYPE}, new Object[]{Boolean.valueOf(false)});
                }
            }
        } else {
            super.onBackPressed();
        }
    }

    public static String getErrorTipMsg() {
        String var0 = "";
        if(PointTime.mEc == 4) {
            var0 = var0 + ", 无SD卡！";
        } else if(PointTime.mEc == 9) {
            var0 = var0 + ", SD卡空间不足！";
        } else if(PointTime.mEt == 1) {
            var0 = var0 + ", 配置文件下载失败！";
        } else if(PointTime.mEt == 3 || PointTime.mEt == 2) {
            var0 = var0 + ", 应用资源下载失败！";
        }

        return var0;
    }

    class a extends View {
        int a;
        float b;
        int c = 0;
        Paint d = new Paint();
        int e = 0;
        int f = 0;
        int g = 255;

        a(Context var2) {
            super(var2);
            this.a = var2.getResources().getDisplayMetrics().widthPixels;
            int var3 = var2.getResources().getDisplayMetrics().heightPixels;
            switch(var3) {
                case 1280:
                    this.b = 6.0F;
                    break;
                case 1920:
                    this.b = 9.0F;
                    break;
                default:
                    this.b = (float)var2.getResources().getDisplayMetrics().heightPixels * 0.0045F;
            }

        }

        protected void onMeasure(int var1, int var2) {
            super.onMeasure(var1, var2);
            this.setMeasuredDimension(this.a, this.c + (int)this.b);
        }

        void a() {
            this.a(100);
        }

        void b() {
            this.postDelayed(new Runnable() {
                public void run() {
                    a.this.g -= 5;
                    if(a.this.g > 0) {
                        a.this.postDelayed(this, 5L);
                    } else {
                        ViewGroup var1 = (ViewGroup)a.this.getParent();
                        if(var1 != null) {
                            var1.removeView(a.this);
                        }
                    }

                    a.this.invalidate();
                }
            }, 50L);
        }

        void a(int var1) {
            var1 = this.a * var1 / 100;
            if(this.e >= this.f) {
                this.postDelayed(new Runnable() {
                    public void run() {
                        int var1 = (a.this.f - a.this.e) / 10;
                        if(var1 > 10) {
                            var1 = 10;
                        } else if(var1 < 1) {
                            var1 = 1;
                        }

                        a.this.e += var1;
                        if(a.this.f > a.this.e) {
                            a.this.postDelayed(this, 5L);
                        } else if(a.this.f >= a.this.a) {
                            a.this.b();
                        }

                        a.this.invalidate();
                    }
                }, 5L);
            }

            this.f = var1;
        }

        protected void onDraw(Canvas var1) {
            super.onDraw(var1);
            this.d.setColor(Color.argb(this.g, 26, 173, 25));
            var1.drawRect(0.0F, (float)this.c, (float)this.e, (float)this.c + this.b, this.d);
        }
    }
}
