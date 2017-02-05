//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.common.adapter.ui.fresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import io.dcloud.common.adapter.ui.fresh.LoadingLayout;
import io.dcloud.common.adapter.ui.fresh.ILoadingLayout.State;

public class FooterLoadingLayout extends LoadingLayout {
    public FooterLoadingLayout(Context var1) {
        super(var1);
        this.init(var1);
    }

    public FooterLoadingLayout(Context var1, AttributeSet var2) {
        super(var1, var2);
        this.init(var1);
    }

    private void init(Context var1) {
        this.setState(State.RESET);
    }

    protected View createLoadingView(Context var1, AttributeSet var2) {
        TextView var3 = new TextView(var1);
        var3.setText("createLoadingView");
        return var3;
    }

    public void setLastUpdatedLabel(CharSequence var1) {
    }

    public int getContentSize() {
        return (int)(this.getResources().getDisplayMetrics().density * 100.0F);
    }

    protected void onStateChanged(State var1, State var2) {
        super.onStateChanged(var1, var2);
    }

    protected void onReset() {
    }

    protected void onPullToRefresh() {
    }

    protected void onReleaseToRefresh() {
    }

    protected void onRefreshing() {
    }

    protected void onNoMoreData() {
    }
}
