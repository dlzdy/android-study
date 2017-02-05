//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.common.adapter.ui.fresh;

import android.content.Context;
import android.util.FloatMath;
import android.webkit.WebView;
import io.dcloud.common.adapter.ui.fresh.PullToRefreshBase;

public class PullToRefreshWebView extends PullToRefreshBase<WebView> {
    public PullToRefreshWebView(Context var1) {
        super(var1);
    }

    protected boolean isReadyForPullDown() {
        return ((WebView)this.mRefreshableView).getScrollY() == 0;
    }

    protected boolean isReadyForPullUp() {
        float var1 = FloatMath.floor((float)((WebView)this.mRefreshableView).getContentHeight() * ((WebView)this.mRefreshableView).getScale());
        return (float)((WebView)this.mRefreshableView).getScrollY() >= var1 - (float)((WebView)this.mRefreshableView).getHeight();
    }

    protected void onScrollChanged(int var1, int var2, int var3, int var4) {
        super.onScrollChanged(var1, var2, var3, var4);
    }
}
