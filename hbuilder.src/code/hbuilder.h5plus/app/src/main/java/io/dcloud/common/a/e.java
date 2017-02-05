//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.common.a;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import io.dcloud.common.DHInterface.IAppInfo;
import io.dcloud.common.DHInterface.IOnCreateSplashView;
import io.dcloud.common.DHInterface.IWebAppRootView;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.MessageHandler.IMessages;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.adapter.util.ViewRect;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.PdrUtil;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;

public class e implements IAppInfo {
    Activity V = null;
    protected IWebAppRootView W = null;
    private IOnCreateSplashView a = null;
    public int X = 0;
    public int Y = 0;
    public int Z = 0;
    public int aa = 0;
    public int ab = 0;
    protected boolean ac = false;
    private boolean b = false;
    private int c = 0;
    ViewRect ad = new ViewRect();

    public e() {
    }

    void a(Activity var1) {
        DisplayMetrics var2 = var1.getResources().getDisplayMetrics();
        this.Y = var2.heightPixels;
        this.V = var1;
        this.ad.mJsonViewOption = JSONUtil.createJSONObject("{}");
        this.Z = PdrUtil.parseInt(PlatformUtil.getBundleData(BaseInfo.PDR, "StatusBarHeight"), 0);
        this.X = var2.widthPixels;
        this.ab = var2.heightPixels - this.Z;
        Logger.i("WebAppInfo", "init() get sStatusBarHeight=" + this.Z);
    }

    public int checkSelfPermission(String var1) {
        return VERSION.SDK_INT >= 23 && this.V != null && var1 != null?((Integer)PlatformUtil.invokeMethod(this.V.getClass().getName(), "checkSelfPermission", this.V, new Class[]{var1.getClass()}, new Object[]{var1})).intValue():-100;
    }

    public void requestPermissions(String[] var1, int var2) {
        if(VERSION.SDK_INT >= 23 && this.V != null && var1 != null) {
            PlatformUtil.invokeMethod(this.V.getClass().getName(), "requestPermissions", this.V, new Class[]{var1.getClass(), Integer.TYPE}, new Object[]{var1, Integer.valueOf(var2)});
        }

    }

    public Activity getActivity() {
        return this.V;
    }

    public ViewRect getAppViewRect() {
        return this.ad;
    }

    public IWebAppRootView obtainWebAppRootView() {
        return this.W;
    }

    public void setWebAppRootView(IWebAppRootView var1) {
        this.W = var1;
    }

    public void setFullScreen(boolean var1) {
        if(BaseInfo.sGlobalFullScreen != var1) {
            this.ac = var1;
            Window var2 = this.getActivity().getWindow();
            LayoutParams var3;
            if(var1) {
                var3 = var2.getAttributes();
                var3.flags |= 1024;
                var2.setAttributes(var3);
                var2.addFlags(512);
            } else {
                var3 = var2.getAttributes();
                var3.flags &= -1025;
                var2.setAttributes(var3);
                var2.clearFlags(512);
            }

            this.updateScreenInfo(this.ac?2:3);
        }

        BaseInfo.sGlobalFullScreen = var1;
    }

    public boolean isFullScreen() {
        return this.ac;
    }

    public void updateScreenInfo(int var1) {
        boolean var2 = this.ac;
        if(!var2 && this.Z == 0) {
            Rect var3 = new Rect();
            this.V.getWindow().getDecorView().getWindowVisibleDisplayFrame(var3);
            this.Z = var3.top;
            if(this.Z != 0) {
                PlatformUtil.setBundleData(BaseInfo.PDR, "StatusBarHeight", String.valueOf(this.Z));
            }
        }

        if(var1 == 2) {
            DisplayMetrics var4 = this.V.getResources().getDisplayMetrics();
            this.X = var4.widthPixels;
            this.ab = var4.heightPixels;
        } else {
            this.X = this.W.obtainMainView().getWidth();
            this.ab = this.W.obtainMainView().getHeight();
        }

        this.ad.onScreenChanged(this.X, this.ab);
    }

    public void setRequestedOrientation(final String var1) {
        try {
            MessageHandler.sendMessage(new IMessages() {
                public void execute(Object var1x) {
                    if("landscape".equals(var1)) {
                        e.this.V.setRequestedOrientation(SCREEN_ORIENTATION_SENSOR_LANDSCAPE);//6
                    } else if("landscape-primary".equals(var1)) {
                        e.this.V.setRequestedOrientation(SCREEN_ORIENTATION_LANDSCAPE);//0
                    } else if("landscape-secondary".equals(var1)) {
                        e.this.V.setRequestedOrientation(SCREEN_ORIENTATION_REVERSE_LANDSCAPE);//8
                    } else if("portrait".equals(var1)) {
                        e.this.V.setRequestedOrientation(SCREEN_ORIENTATION_SENSOR_PORTRAIT);//7
                    } else if("portrait-primary".equals(var1)) {
                        e.this.V.setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);//1
                    } else if("portrait-secondary".equals(var1)) {
                        e.this.V.setRequestedOrientation(SCREEN_ORIENTATION_REVERSE_PORTRAIT);//9
                    } else {
                        e.this.V.setRequestedOrientation(SCREEN_ORIENTATION_SENSOR);//4
                    }

                }
            }, 48L, var1);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public int getRequestedOrientation() {
        return this.V.getRequestedOrientation();
    }

    public boolean isVerticalScreen() {
        return this.V.getResources().getConfiguration().orientation == 1;
    }

    public void setRequestedOrientation(int var1) {
        this.V.setRequestedOrientation(var1);
    }

    public int getInt(int var1) {
        int var2 = -1;
        switch(var1) {
            case 0:
                var2 = this.X;
                break;
            case 1:
                var2 = this.ab;
                break;
            case 2:
                var2 = this.Y;
        }

        return var2;
    }

    public void setMaskLayer(boolean var1) {
        this.b = var1;
        if(var1) {
            ++this.c;
        } else {
            --this.c;
            if(this.c < 0) {
                this.c = 0;
            }
        }

    }

    public int getMaskLayerCount() {
        return this.c;
    }

    public void clearMaskLayerCount() {
        this.c = 0;
    }

    public void setOnCreateSplashView(IOnCreateSplashView var1) {
        this.a = var1;
    }

    public IOnCreateSplashView getOnCreateSplashView() {
        return this.a;
    }
}
