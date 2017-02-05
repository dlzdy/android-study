//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.dcloud.android.graphics;

import android.os.Build.VERSION;

public class Region extends android.graphics.Region {
    private int HOLD_SCREEN_COUNT;
    int fillScreenCounter;

    public Region() {
        this(0);
    }

    public Region(int var1) {
        this.HOLD_SCREEN_COUNT = 2;
        this.fillScreenCounter = 1;
        if(VERSION.SDK_INT >= 21) {
            this.HOLD_SCREEN_COUNT = 1;
        } else {
            this.HOLD_SCREEN_COUNT = var1;
        }

    }

    boolean fillWholeScreen() {
        return this.fillScreenCounter == this.HOLD_SCREEN_COUNT;
    }

    void count() {
        ++this.fillScreenCounter;
    }
}
