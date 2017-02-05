//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.dcloud.android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.view.MotionEvent;

import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.adapter.util.ViewOptions;

public class AbsoluteLayout extends SlideLayout {
    AdaFrameView mFrameView = null;
    ViewOptions mViewOptions = null;
    IApp mAppHandler = null;
    static final String STATE_CHANGED_TEMPLATE = "{status:\'%s\',offset:\'%s\'}";
    boolean canDoMaskClickEvent = true;
    float downX;
    float downY;

    public AbsoluteLayout(Context var1, AdaFrameView var2, IApp var3) {
        super(var1);
        this.mFrameView = var2;
        this.mAppHandler = var3;
        this.mViewOptions = this.mFrameView.obtainFrameOptions();
        this.setOnStateChangeListener(new OnStateChangeListener() {
            public void onStateChanged(String var1, String var2) {
                AbsoluteLayout.this.mFrameView.dispatchFrameViewEvents("slideBounce", String.format("{status:\'%s\',offset:\'%s\'}", new Object[]{var1, var2}));
            }
        });
    }

    private void doClickEvent(MotionEvent var1) {
        if(var1.getAction() == 0) {
            this.canDoMaskClickEvent = true;
            this.downX = var1.getX();
            this.downY = var1.getY();
        } else {
            float var2;
            float var3;
            byte var4;
            if(var1.getAction() == 1) {
                var2 = var1.getX();
                var3 = var1.getY();
                var4 = 10;
                if(this.canDoMaskClickEvent && Math.abs(this.downX - var2) <= (float)var4 && Math.abs(this.downY - var3) <= (float)var4) {
                    this.mFrameView.dispatchFrameViewEvents("maskClick", (Object)null);
                }
            } else if(var1.getAction() == 2) {
                var2 = var1.getX();
                var3 = var1.getY();
                var4 = 10;
                if(this.canDoMaskClickEvent && Math.abs(this.downX - var2) > (float)var4 && Math.abs(this.downY - var3) > (float)var4) {
                    this.canDoMaskClickEvent = false;
                }
            }
        }

    }

    public boolean dispatchTouchEvent(MotionEvent var1) {
        if(this.mViewOptions != null && this.mViewOptions.hasMask()) {
            this.doClickEvent(var1);
            return true;
        } else if(this.mViewOptions != null && this.mViewOptions.hasBackground()) {
            super.dispatchTouchEvent(var1);
            return true;
        } else {
            return super.dispatchTouchEvent(var1);
        }
    }

    protected void dispatchDraw(Canvas var1) {
        this.mFrameView.paint(var1);
        super.dispatchDraw(var1);
        if(this.mViewOptions != null && this.mViewOptions.hasMask()) {
            var1.drawColor(this.mViewOptions.maskColor);
        }

    }

    public String toString() {
        return this.mFrameView.toString();
    }

    protected void onConfigurationChanged(Configuration var1) {
        super.onConfigurationChanged(var1);
        this.mFrameView.onConfigurationChanged();
    }
}
