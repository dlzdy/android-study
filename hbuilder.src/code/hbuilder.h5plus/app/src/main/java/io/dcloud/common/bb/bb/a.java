//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.common.bb.bb;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.DeviceInfo;

public class a {
    private static a a;
    private View b;
    private int c;
    private LayoutParams d;
    private boolean e = false;

    public static void a(FrameLayout var0) {
        if(a == null) {
            a = new a(var0);
        }

    }

    private a(FrameLayout var1) {
        this.b = var1;
        this.b.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                a.this.b();
            }
        });
        this.d = (LayoutParams)this.b.getLayoutParams();
        this.e = AndroidResources.checkImmersedStatusBar(var1.getContext());
    }

    private void b() {
        int var1 = this.c();
        if(var1 != this.c) {
            int var2 = this.b.getRootView().getHeight();
            int var3 = var2 - var1;
            if(var3 > var2 / 4) {
                this.d.height = var2 - var3 + (this.e?DeviceInfo.sStatusBarHeight:0);
            } else {
                this.d.height = var2;
            }

            this.b.requestLayout();
            this.c = var1;
        }

    }

    private int c() {
        Rect var1 = new Rect();
        this.b.getWindowVisibleDisplayFrame(var1);
        return var1.bottom - var1.top;
    }

    public static void a() {
        a = null;
    }
}
