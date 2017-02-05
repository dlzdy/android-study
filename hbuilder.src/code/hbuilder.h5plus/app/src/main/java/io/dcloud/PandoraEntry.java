//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class PandoraEntry extends Activity {
    public PandoraEntry() {
    }

    protected void onCreate(Bundle var1) {
        super.onCreate(var1);
        Intent var2 = this.getIntent();
        boolean var3 = false;

        try {
            var3 = var2.getBooleanExtra("is_stream_app", var3);
        } catch (Exception var5) {
            var5.printStackTrace();
            this.finish();
            return;
        }

        if(var3) {
            var2.setClass(this, WebAppActivity.class);
            var2.putExtra("is_stream_app", true);
        } else {
            var2.putExtra("short_cut_class_name", PandoraEntry.class.getName());
            var2.setClass(this, PandoraEntryActivity.class);
        }

        this.startActivity(var2);
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                PandoraEntry.this.finish();
            }
        }, 20L);
    }
}
