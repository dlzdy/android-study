//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.dcloud.android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Path.Direction;
import android.util.AttributeSet;
import android.widget.ImageView;
import io.dcloud.common.adapter.util.CanvasHelper;

public class RoundAngleImageView extends ImageView {
    Path path = new Path();

    public RoundAngleImageView(Context var1, AttributeSet var2) {
        super(var1, var2);
    }

    protected void onDraw(Canvas var1) {
        var1.save();
        RectF var2 = new RectF(0.0F, 0.0F, (float)this.getWidth(), (float)this.getHeight());
        this.path.addRoundRect(var2, (float)CanvasHelper.dip2px(this.getContext(), 8.0F), (float)CanvasHelper.dip2px(this.getContext(), 8.0F), Direction.CCW);
        var1.clipPath(this.path);
        if(this.getDrawable() == null) {
            var1.drawColor(-3355444);
        }

        super.onDraw(var1);
        var1.restore();
    }
}
