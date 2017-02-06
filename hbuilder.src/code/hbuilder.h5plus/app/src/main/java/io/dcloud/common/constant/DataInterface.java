//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.common.constant;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.MobilePhoneModel;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.NetworkTypeUtil;
import io.dcloud.common.util.TelephonyUtil;

public class DataInterface {
    public DataInterface() {
    }

    public static String getBaseUrl() {
        return "http://120.55.82.240/";
    }

    public static String getUrlBaseData(Context var0, String var1, String var2, String var3) {
        String var4 = TelephonyUtil.getIMEI(var0);
        int var5 = NetworkTypeUtil.getNetworkType(var0);
        String var6 = null;

        try {
            var6 = URLEncoder.encode(Build.MODEL, "utf-8");
        } catch (UnsupportedEncodingException var9) {
            var9.printStackTrace();
        }

        int var7 = VERSION.SDK_INT;
        String var8 = BaseInfo.sBaseVersion;
        return String.format(StringConst.T_URL_BASE_DATA, new Object[]{var1, var4, Integer.valueOf(var5), var6, Integer.valueOf(var7), var8, Integer.valueOf(StringConst.getIntSF(var2)), Long.valueOf(System.currentTimeMillis()), var3, Build.MANUFACTURER}) + getTestParam(var1) + "&mc=" + BaseInfo.sChannel;
    }

    public static String getTestParam(String var0) {
        String var1 = "r";
        if(!TextUtils.isEmpty(var0) && BaseInfo.isTest(var0)) {
            var1 = "t";
        }

        return "&__am=" + var1;
    }

    public static String getStatisticsUrl(String var0, String var1, int var2, String var3, String var4) {
        return String.format("http://120.55.82.240/collect/startup?appid=%s&imei=%s&s=%d&vb=%s&md=%s&os=%d&vd=%s", new Object[]{var0, var1, Integer.valueOf(var2), var3, var4, Integer.valueOf(VERSION.SDK_INT), Build.MANUFACTURER}) + "&p=a" + getTestParam(var0) + "&mc=" + BaseInfo.sChannel;
    }

    public static String getIconImageUrl(String var0, String var1) {
        return "http://120.55.82.240/resource/icon?appid=" + var0 + "&width=" + var1 + getTestParam(var0) + (DeviceInfo.sApplicationContext == null?"":"&imei=" + TelephonyUtil.getIMEI(DeviceInfo.sApplicationContext));
    }

    public static String getWGTUrl(Context var0, String var1, String var2, String var3) {
        return "http://120.55.82.240/resource/wgt?" + getUrlBaseData(var0, var1, var2, var3);
    }

    public static String getCheckUpdateUrl(String var0, String var1, String var2) {
        return "http://120.55.82.240/check/update?appid=" + var0 + "&version=" + var1 + "&type=native" + "&imei=" + var2 + getTestParam(var0);
    }

    public static String getDatailUrl(String var0) {
        return "http://120.55.82.240/apps/detail?appid=" + var0 + getTestParam(var0);
    }

    public static String getSplashUrl(String var0, String var1, String var2) {
        return "http://120.55.82.240/resource/splash?appid=" + var0 + "&width=" + var1 + "&height=" + var2 + getTestParam(var0);
    }

    public static String getAppStreamUpdateUrl(Context var0, String var1, String var2, String var3, String var4, String var5) {
        StringBuilder var6 = new StringBuilder();
        var6.append(var1).append("check/update?").append(getUrlBaseData(var0, var2, var4, var5)).append("&version=").append(var3).append("&plus_version=").append(BaseInfo.sBaseVersion).append("&width=").append(var0.getResources().getDisplayMetrics().widthPixels).append("&height=").append(var0.getResources().getDisplayMetrics().heightPixels);
        return var6.toString();
    }

    public static String getJsonUrl(String var0, String var1, String var2, Context var3, String var4) {
        return var0 + "resource/stream?" + getUrlBaseData(var3, var1, var2, var4);
    }

    public static String getAppListUrl(String var0) {
        return "http://120.55.82.240/apps/list?t=array" + getTestParam(var0);
    }

    public static String getRomVersion() {
        if(Build.MANUFACTURER.equals(MobilePhoneModel.XIAOMI)) {
            String var0 = DeviceInfo.getBuildValue("ro.miui.ui.version.name");
            return VERSION.INCREMENTAL + (TextUtils.isEmpty(var0)?"":"-" + var0);
        } else {
            return VERSION.INCREMENTAL;
        }
    }

    public static String getStreamappFrom(Intent var0) {
        String var1 = null;
        if(var0 == null) {
            return var1;
        } else {
            if(var0.hasExtra("plus.runtime.launcher")) {
                String var2 = var0.getStringExtra("plus.runtime.launcher");
                if(var2.indexOf("third:") == 0) {
                    var1 = var2.substring(6, var2.length());
                }
            } else if(var0.hasExtra("from")) {
                var1 = var0.getStringExtra("from");
            }

            return var1;
        }
    }
}
