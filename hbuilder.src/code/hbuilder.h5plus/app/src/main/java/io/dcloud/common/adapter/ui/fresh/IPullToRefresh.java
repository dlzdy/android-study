//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.common.adapter.ui.fresh;

import android.view.View;

import io.dcloud.common.adapter.ui.fresh.PullToRefreshBase.OnRefreshListener;

public interface IPullToRefresh<T extends View> {
    void setPullRefreshEnabled(boolean var1);

    void setPullLoadEnabled(boolean var1);

    void setScrollLoadEnabled(boolean var1);

    boolean isPullRefreshEnabled();

    boolean isPullLoadEnabled();

    boolean isScrollLoadEnabled();

    void setOnRefreshListener(OnRefreshListener<T> var1);

    void onPullDownRefreshComplete();

    void onPullUpRefreshComplete();

    T getRefreshableView();

    LoadingLayout getHeaderLoadingLayout();

    LoadingLayout getFooterLoadingLayout();

    void setLastUpdatedLabel(CharSequence var1);
}
