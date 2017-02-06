//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.common.adapter.ui.fresh;

import android.content.Context;
import android.webkit.WebView;

public class PullToRefreshWebView extends PullToRefreshBase<WebView> {
    public PullToRefreshWebView(Context var1) {
        super(var1);
    }

    protected boolean isReadyForPullDown() {
        return ((WebView)this.mRefreshableView).getScrollY() == 0;
    }

    protected boolean isReadyForPullUp() {
        double exactContentHeight = Math.floor((float)((WebView)this.mRefreshableView).getContentHeight() * ((WebView)this.mRefreshableView).getScale());
        return mRefreshableView.getScrollY() >= (exactContentHeight - mRefreshableView.getHeight());
    }
//    protected boolean isReadyForPullEnd() {
//        float exactContentHeight = FloatMath.floor(mRefreshableView.getContentHeight() * mRefreshableView.getScale());
//        return mRefreshableView.getScrollY() >= (exactContentHeight - mRefreshableView.getHeight());
//    }

    protected void onScrollChanged(int var1, int var2, int var3, int var4) {
        super.onScrollChanged(var1, var2, var3, var4);
    }
}
