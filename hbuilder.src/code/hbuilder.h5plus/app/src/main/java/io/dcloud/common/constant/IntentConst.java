//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.common.constant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.URLUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.StringUtil;

public class IntentConst {
    public static final String APPID = "appid";
    public static final String INTENT_DATA = "http://update.dcloud.net.cn/apps/";
    public static final String SHORT_CUT_APPID = "short_cut_appid";
    public static final String SHORT_CUT_MODE = "short_cut_mode";
    public static final String QIHOO_360_BROWSER_URL = "360_browser_url";
    public static final String PROCESS_TYPE = "_process_type_";
    public static int PROCESS_TYPE_DEFALUT = 0;
    public static int PROCESS_TYPE_HEAD = 1;
    public static final String SHORT_CUT_SRC = "shoort_cut_src";
    public static final String APP_SPLASH_PATH = "app_splash_path";
    public static final String WEBAPP_ACTIVITY_HAS_STREAM_SPLASH = "has_stream_splash";
    public static final String WEBAPP_ACTIVITY_HIDE_STREAM_SPLASH = "hide_stream_splash";
    public static final String WEBAPP_ACTIVITY_JUST_DOWNLOAD = "just_download";
    public static final String WEBAPP_ACTIVITY_APPICON = "app_icon";
    public static final String WEBAPP_ACTIVITY_APPEXTERN = "app_extern";
    public static final String WEBAPP_ACTIVITY_APPNAME = "app_name";
    public static final String WEBAPP_ACTIVITY_SPLASH_MODE = "__splash_mode__";
    public static final String WEBAPP_ACTIVITY_LAUNCH_PATH = "__launch_path__";
    public static final String WEBAPP_SHORT_CUT_CLASS_NAME = "short_cut_class_name";
    public static final String FROM_SHORT_CUT_STRAT = "from_short_cut_start";
    public static final String FROM_BARCODE = "from_barcode";
    public static final String FROM_PUSH = "from_push";
    public static final String IS_WEBAPP_REPLY = "__webapp_reply__";
    public static final String START_FROM = "__start_from__";
    public static final int START_FROM_UNKONW = -1;
    public static final int START_FROM_STREAM_OPEN = 1;
    public static final int START_FROM_SHORT_CUT = 2;
    public static final int START_FROM_PUSH = 3;
    public static final int START_FROM_BARCODE = 4;
    public static final int START_FROM_MYAPP = 5;
    public static final int START_FROM_SECHEME = 6;
    public static final String FROM_STREAM_OPEN_FLAG = "__from_stream_open_flag__";
    public static final String FROM_STREAM_OPEN_STYLE = "__from_stream_open_style__";
    public static final String FROM_STREAM_OPEN_AUTOCLOSE = "__from_stream_open_autoclose__";
    public static final String FROM_STREAM_OPEN_TIMEOUT = "__from_stream_open_timeout__";
    public static final String NAME = "__name__";
    public static final String EXTRAS = "__extras__";
    public static final String DELETE_PUSH_BY_USER = "__by_user__";
    public static final String IS_STREAM_APP = "is_stream_app";
    public static final String STREAM_LAUNCHER = "__launcher__";
    public static final String TEST_STREAM_APP = "__am";
    public static final String PUSH_PAYLOAD = "__payload__";
    public static final String FIRST_WEB_URL = "__first_web_url__";
    public static final String IS_START_FIRST_WEB = "__start_first_web__";
    public static final String START_FORCE_SHORT = "__sc";
    public static final String QIHOO_START_PARAM_FROM = "from";
    public static final String QIHOO_START_PARAM_MODE = "mode";
    public static final String PENDING_INTENT_MODE = "__pending_intent_mode__";
    public static final String PENDING_INTENT_MODE_ACTIVITY = "__pending_intent_mode_activity__";
    public static final String PENDING_INTENT_MODE_SERVICE = "__pending_intent_mode_service__";
    public static final String RUNING_STREAPP_LAUNCHER = "plus.runtime.launcher";
    public static final String SPLASH_VIEW = "__splash_view__";
    public static final String PL_UPDATE = "__plugin_update__";
    public static final String PL_AUTO_HIDE = "__plugin_auto_hide__";
    public static final String PL_AUTO_HIDE_SHOW_PN = "__plugin_auto_hide_show_pname__";
    public static final String PL_AUTO_HIDE_SHOW_ACTIVITY = "__plugin_auto_hide_show_activity__";
    private static ArrayList<String> TO_JS_CANT_USE_KEYS = new ArrayList();

    public IntentConst() {
    }

    public static boolean allowToHtml(String var0) {
        return !TO_JS_CANT_USE_KEYS.contains(var0) && !var0.startsWith("com.morgoo.droidplugin");
    }

    public static Intent modifyStartFrom(Intent var0) {
        if(var0 != null && var0.getIntExtra("__start_from__", -1) == -1) {
            if(var0.getBooleanExtra("from_short_cut_start", false)) {
                var0.putExtra("__start_from__", 2);
            } else if(var0.getBooleanExtra("from_push", false)) {
                var0.putExtra("__start_from__", 3);
            } else if(var0.getBooleanExtra("from_barcode", false)) {
                var0.putExtra("__start_from__", 4);
            }
        }

        return var0;
    }

    public static Intent removeArgs(Intent var0, String var1) {
        if(var0 == null) {
            return var0;
        } else {
            Uri var2 = var0.getData();
            if(var2 != null && !URLUtil.isNetworkUrl(var2.toString())) {
                int var3 = var2.toString().indexOf("://");
                var0.setData(Uri.parse(var2.toString().substring(0, var3 + "://".length())));
            }

            if(var0.getExtras() != null) {
                Bundle var9 = var0.getExtras();
                if(var9 != null) {
                    Set var4 = var9.keySet();
                    if(var4 != null) {
                        int var5 = var4.size();
                        String[] var6 = new String[var5];
                        var4.toArray(var6);

                        for(int var7 = var5 - 1; var7 >= 0; --var7) {
                            String var8 = var6[var7];
                            if(allowToHtml(var8)) {
                                var9.remove(var8);
                            }
                        }
                    }
                }
            }

            return var0;
        }
    }

    public static String obtainArgs(Intent var0, String var1) {
        if(var0 == null) {
            return "";
        } else {
            Uri var2 = var0.getData();
            String var3;
            if(var2 != null && !URLUtil.isNetworkUrl(var2.toString())) {
                var3 = BaseInfo.getLaunchType(var0);
                BaseInfo.putLauncherData(var1, var3);
                saveType(var1, var3);
                return var0.hasExtra("__extras__")?var0.getStringExtra("__extras__"):var2.toString() + "";
            } else if(var0.getExtras() != null) {
                var3 = BaseInfo.getLaunchType(var0);
                BaseInfo.putLauncherData(var1, var3);
                saveType(var1, var3);
                if(var0 != null && var0.hasExtra("__extras__")) {
                    return var0.getStringExtra("__extras__");
                } else {
                    JSONObject var4 = new JSONObject();
                    Bundle var5 = var0.getExtras();
                    if(var5 != null) {
                        Set var6 = var5.keySet();
                        if(var6 != null) {
                            int var7 = var6.size();
                            String[] var8 = new String[var7];
                            var6.toArray(var8);

                            for(int var9 = 0; var9 < var7; ++var9) {
                                String var10 = var8[var9];
                                if(allowToHtml(var10)) {
                                    try {
                                        var4.put(var10, var5.get(var10).toString());
                                    } catch (Exception var12) {
                                        var12.printStackTrace();
                                    }
                                }
                            }
                        }
                    }

                    if(TextUtils.equals(var3, "push")) {
                        String var13 = var0.getStringExtra("__payload__");
                        String var14 = StringUtil.getSCString(var13, "__sc");
                        if(!TextUtils.isEmpty(var14)) {
                            var0.putExtra("__sc", var14);
                        }

                        return TextUtils.isEmpty(var13)?"":var13;
                    } else {
                        return var4.length() > 0?var4.toString():"";
                    }
                }
            } else {
                return "";
            }
        }
    }

    private static void saveType(String var0, String var1) {
        String var2 = SP.getBundleData("pdr", var0 + "LAUNCHTYPE");
        if(TextUtils.isEmpty(var2)) {
            SP.setBundleData("pdr", var0 + "LAUNCHTYPE", var1);
        }

    }

    static {
        TO_JS_CANT_USE_KEYS.add("__name__");
        TO_JS_CANT_USE_KEYS.add("__from_stream_open_autoclose__");
        TO_JS_CANT_USE_KEYS.add("__from_stream_open_timeout__");
        TO_JS_CANT_USE_KEYS.add("__start_from__");
        TO_JS_CANT_USE_KEYS.add("__plugin_auto_hide__");
        TO_JS_CANT_USE_KEYS.add("__plugin_auto_hide_show_pname__");
        TO_JS_CANT_USE_KEYS.add("__plugin_auto_hide_show_activity__");
        TO_JS_CANT_USE_KEYS.add("__splash_view__");
        TO_JS_CANT_USE_KEYS.add("__from_stream_open_style__");
        TO_JS_CANT_USE_KEYS.add("__from_stream_open_flag__");
        TO_JS_CANT_USE_KEYS.add("__launcher__");
        TO_JS_CANT_USE_KEYS.add("short_cut_appid");
        TO_JS_CANT_USE_KEYS.add("appid");
        TO_JS_CANT_USE_KEYS.add("mode");
        TO_JS_CANT_USE_KEYS.add("short_cut_mode");
        TO_JS_CANT_USE_KEYS.add("shoort_cut_src");
        TO_JS_CANT_USE_KEYS.add("__am");
        TO_JS_CANT_USE_KEYS.add("from");
        TO_JS_CANT_USE_KEYS.add("__sc");
        TO_JS_CANT_USE_KEYS.add("app_splash_path");
        TO_JS_CANT_USE_KEYS.add("has_stream_splash");
        TO_JS_CANT_USE_KEYS.add("app_icon");
        TO_JS_CANT_USE_KEYS.add("app_extern");
        TO_JS_CANT_USE_KEYS.add("app_name");
        TO_JS_CANT_USE_KEYS.add("from_short_cut_start");
        TO_JS_CANT_USE_KEYS.add("from_barcode");
        TO_JS_CANT_USE_KEYS.add("from_push");
        TO_JS_CANT_USE_KEYS.add("__by_user__");
        TO_JS_CANT_USE_KEYS.add("is_stream_app");
        TO_JS_CANT_USE_KEYS.add("short_cut_class_name");
        TO_JS_CANT_USE_KEYS.add("just_download");
        TO_JS_CANT_USE_KEYS.add("hide_stream_splash");
        TO_JS_CANT_USE_KEYS.add("has_stream_splash");
        TO_JS_CANT_USE_KEYS.add("__first_web_url__");
        TO_JS_CANT_USE_KEYS.add("__start_first_web__");
        TO_JS_CANT_USE_KEYS.add("plus.runtime.launcher");
        TO_JS_CANT_USE_KEYS.add("__splash_mode__");
        TO_JS_CANT_USE_KEYS.add("__launch_path__");
        TO_JS_CANT_USE_KEYS.add("_process_type_");
        TO_JS_CANT_USE_KEYS.add("__webapp_reply__");
    }
}
