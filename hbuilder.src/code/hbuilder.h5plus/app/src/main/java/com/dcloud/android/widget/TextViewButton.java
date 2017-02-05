//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.dcloud.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

public class TextViewButton extends TextView {
    public TextViewButton(Context var1, AttributeSet var2) {
        super(var1, var2);
    }

    public boolean onTouchEvent(MotionEvent var1) {
        int var2 = var1.getAction();
        if(var2 == 0) {
            this.setBackgroundColor(-3355444);
        } else if(var2 == 1 || var2 == 4) {
            this.setBackgroundColor(-1118482);
        }

        return super.onTouchEvent(var1);
    }
}
