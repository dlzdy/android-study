//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud;

import android.content.Context;
import android.text.TextUtils;

import io.dcloud.application.DCloudApplication;
import io.dcloud.common.DHInterface.IReflectAble;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.util.BaseInfo;
import io.src.dcloud.adapter.DCloudAdapterUtil;

public class PdrR implements IReflectAble {
    private static String a = null;
    public static int DRAWABLE_SPLASH = RInformation.getInt("drawable", "splash");
    public static int DRAWABLE_ICON;
    public static int LAYOUT_SNOW_WHITE_PROGRESS;
    public static int LAYOUT_SNOW_BLACK_PROGRESS;
    public static int ID_PROGRESSBAR;
    public static int FEATURE_LOSS_STYLE;
    public static int ID_ICON_SPLASH;
    public static int ID_TEXT_COPYRIGHT_SPLASH;
    public static int ID_TEXT_LOADING_SPLASH;
    public static int ID_TEXT_NAME_SPLASH;
    public static int LAYOUT_CUSTION_NOTIFICATION_DCLOUD;
    public static int ID_IMAGE_NOTIFICATION_DCLOUD;
    public static int ID_TITLE_NOTIFICATION_DCLOUD;
    public static int ID_TEXT_NOTIFICATION;
    public static int ID_TIME_NOTIFICATION_DCLOUD;
    public static int DRAWABLE_DCLOUD_DIALOG_SHAPE;
    public static int LAYOUT_DIALOG_LAYOUT_DCLOUD_DIALOG;
    public static int ID_DCLOUD_DIALOG_ROOTVIEW;
    public static int ID_DCLOUD_DIALOG_TITLE;
    public static int ID_DCLOUD_DIALOG_ICON;
    public static int ID_DCLOUD_DIALOG_MSG;
    public static int ID_DCLOUD_DIALOG_BTN1;
    public static int ID_DCLOUD_DIALOG_BTN2;
    public static int STYLE_DIALOG_DCLOUD_DEFALUT_DIALOG;
    public static int STYLE_DIALOG_STYLE_DCLOUD_ANIM_DIALOG_WINDOW_IN_OUT;
    public static int ANIM_DIALOG_ANIM_DCLOUD_SLIDE_IN_FROM_TOP;
    public static int ANIM_DIALOG_ANIM_DCLOUD_SLIDE_OUT_TO_TOP;
    public static int STREAMAPP_DELETE_THEME;
    public static int STREAMAPP_DRAWABLE_APPDEFULTICON;
    public static int DRAWBLE_PROGRESSBAR_BLACK_DCLOUD;
    public static int DRAWBLE_PROGRESSBAR_WHITE_DCLOUD;
    public static int DRAWEBL_SHADOW_LEFT;

    public PdrR() {
    }

    public static void init(Context var0) {
        if(var0 != null) {
            a = var0.getPackageName();
        }

    }

    public static void checkInit() {
        if(TextUtils.isEmpty(a)) {
            if(DCloudAdapterUtil.isPlugin() && !TextUtils.isEmpty(DCloudAdapterUtil.getPageName())) {
                a = DCloudAdapterUtil.getPageName();
            } else if(DeviceInfo.sApplicationContext != null) {
                init(DeviceInfo.sApplicationContext);
            } else if(DCloudApplication.getInstance() != null) {
                init(DCloudApplication.getInstance());
            }
        }

    }

    public static int getInt(String var0, String var1) {
        try {
            checkInit();
            return Class.forName(a + ".R$" + var0).getField(var1).getInt((Object)null);
        } catch (NoSuchFieldException var3) {
            Logger.e("Not found " + a + ".R$" + var0 + "." + var1);
            if(BaseInfo.ISDEBUG) {
                var3.printStackTrace();
            }
        } catch (Exception var4) {
            Logger.e("Not init RInfomation sPackageName=" + a);
            if(BaseInfo.ISDEBUG) {
                var4.printStackTrace();
            }
        }

        return 0;
    }

    public static int[] getIntArray(String var0, String var1) {
        try {
            checkInit();
            return (int[])((int[])Class.forName(a + ".R$" + var0).getField(var1).get((Object)null));
        } catch (NoSuchFieldException var3) {
            var3.printStackTrace();
            Logger.e("Not found " + a + ".R." + var0 + "." + var1);
        } catch (Exception var4) {
            Logger.e("Not init RInfomation sPackageName=" + a);
            var4.printStackTrace();
        }

        return null;
    }

    static {
        DRAWABLE_ICON = AndroidResources.mApplicationInfo.applicationInfo.icon;
        LAYOUT_SNOW_WHITE_PROGRESS = RInformation.getInt("layout", "snow_white_progress");
        LAYOUT_SNOW_BLACK_PROGRESS = RInformation.getInt("layout", "snow_black_progress");
        ID_PROGRESSBAR = RInformation.getInt("id", "progressBar");
        FEATURE_LOSS_STYLE = RInformation.getInt("style", "featureLossDialog");
        ID_ICON_SPLASH = RInformation.getInt("id", "iv_icon_splash_dcloud");
        ID_TEXT_COPYRIGHT_SPLASH = RInformation.getInt("id", "tv_copyright_splash_dcloud");
        ID_TEXT_LOADING_SPLASH = RInformation.getInt("id", "tv_loading_splash_dcloud");
        ID_TEXT_NAME_SPLASH = RInformation.getInt("id", "tv_name_splash_dcloud");
        LAYOUT_CUSTION_NOTIFICATION_DCLOUD = RInformation.getInt("layout", "custom_notification");
        ID_IMAGE_NOTIFICATION_DCLOUD = RInformation.getInt("id", "image");
        ID_TITLE_NOTIFICATION_DCLOUD = RInformation.getInt("id", "title");
        ID_TEXT_NOTIFICATION = RInformation.getInt("id", "text");
        ID_TIME_NOTIFICATION_DCLOUD = RInformation.getInt("id", "time");
        DRAWABLE_DCLOUD_DIALOG_SHAPE = RInformation.getInt("drawable", "dcloud_dialog_shape");
        LAYOUT_DIALOG_LAYOUT_DCLOUD_DIALOG = RInformation.getInt("layout", "dcloud_dialog");
        ID_DCLOUD_DIALOG_ROOTVIEW = RInformation.getInt("id", "dcloud_dialog_rootview");
        ID_DCLOUD_DIALOG_TITLE = RInformation.getInt("id", "dcloud_dialog_title");
        ID_DCLOUD_DIALOG_ICON = RInformation.getInt("id", "dcloud_dialog_icon");
        ID_DCLOUD_DIALOG_MSG = RInformation.getInt("id", "dcloud_dialog_msg");
        ID_DCLOUD_DIALOG_BTN1 = RInformation.getInt("id", "dcloud_dialog_btn1");
        ID_DCLOUD_DIALOG_BTN2 = RInformation.getInt("id", "dcloud_dialog_btn2");
        STYLE_DIALOG_DCLOUD_DEFALUT_DIALOG = RInformation.getInt("style", "dcloud_defalut_dialog");
        STYLE_DIALOG_STYLE_DCLOUD_ANIM_DIALOG_WINDOW_IN_OUT = RInformation.getInt("style", "dcloud_anim_dialog_window_in_out");
        ANIM_DIALOG_ANIM_DCLOUD_SLIDE_IN_FROM_TOP = RInformation.getInt("anim", "dcloud_slide_in_from_top");
        ANIM_DIALOG_ANIM_DCLOUD_SLIDE_OUT_TO_TOP = RInformation.getInt("anim", "dcloud_slide_out_to_top");
        STREAMAPP_DELETE_THEME = RInformation.getInt("style", "streamDelete19Dialog");
        STREAMAPP_DRAWABLE_APPDEFULTICON = RInformation.getInt("drawable", "dcloud_streamapp_icon_appdefault");
        DRAWBLE_PROGRESSBAR_BLACK_DCLOUD = RInformation.getInt("drawable", "dcloud_snow_black_progress");
        DRAWBLE_PROGRESSBAR_WHITE_DCLOUD = RInformation.getInt("drawable", "dcloud_snow_white_progress");
        DRAWEBL_SHADOW_LEFT = RInformation.getInt("drawable", "dcloud_shadow_left");
    }
}
