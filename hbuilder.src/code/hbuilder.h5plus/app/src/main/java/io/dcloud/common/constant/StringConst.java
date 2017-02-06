//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.common.constant;

import android.text.TextUtils;

import io.dcloud.common.adapter.util.AndroidResources;

public final class StringConst extends AndroidResources implements AbsoluteConst {
    static String T_URL_BASE_DATA = "appid=%s&imei=%s&net=%d&md=%s&os=%d&vb=%s&sf=%d&p=a&d1=%d&sfd=%s&vd=%s";

    public StringConst() {
    }

    public static int getIntSF(String var0) {
        if(!TextUtils.isEmpty(var0)) {
            if("barcode".equals(var0)) {
                return 2;
            }

            if("scheme".equals(var0)) {
                return 3;
            }

            if("stream".equals(var0)) {
                return 6;
            }

            if("shortcut".equals(var0)) {
                return 5;
            }

            if("push".equals(var0)) {
                return 4;
            }

            if("myapp".equals(var0)) {
                return 7;
            }

            if(var0.indexOf("third:") == 0) {
                return 9;
            }
        }

        return 1;
    }
}
