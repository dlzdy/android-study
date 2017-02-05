//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

class c extends BroadcastReceiver {
    io.dcloud.feature.internal.reflect.BroadcastReceiver a = null;
    IntentFilter b = null;

    c(io.dcloud.feature.internal.reflect.BroadcastReceiver var1, IntentFilter var2) {
        this.a = var1;
        this.b = var2;
    }

    public void onReceive(Context var1, Intent var2) {
        if(this.a != null && this.b != null && this.b.hasAction(var2.getAction())) {
            this.a.onReceive(var1, var2);
        }

    }
}
