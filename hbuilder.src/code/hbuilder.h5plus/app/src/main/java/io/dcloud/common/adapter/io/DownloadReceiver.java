//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.common.adapter.io;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.PlatformUtil;

public class DownloadReceiver extends BroadcastReceiver {
    public static String ACTION_DOWNLOAD_COMPLETED = "action.download.completed_io.dcloud";
    public static String ACTION_OPEN_FILE = "action.openfile.io.dcloud";
    public static String KEY_FILEURI = "FileUri";

    public DownloadReceiver() {
    }

    public void onReceive(Context var1, Intent var2) {
        String var3 = var2.getAction();
        if(ACTION_OPEN_FILE.equals(var3)) {
            String var4 = var2.getStringExtra(KEY_FILEURI);
            Logger.d("DownloadReceiver", "action=" + ACTION_OPEN_FILE + ";" + KEY_FILEURI + "=" + var4);
            PlatformUtil.openFileBySystem(var1, String.valueOf(var4), (String)null, (ICallBack)null);
        } else {
            Logger.d("not handle \'" + var3 + "\' cation");
        }

    }
}
