//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.common.adapter.ui.fresh;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public abstract class LoadingLayout extends FrameLayout implements ILoadingLayout {
    private View mContainer;
    private State mCurState;
    private State mPreState;

    public LoadingLayout(Context var1) {
        this(var1, (AttributeSet)null);
    }

    public LoadingLayout(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }

    public LoadingLayout(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.mCurState = State.NONE;
        this.mPreState = State.NONE;
        this.init(var1, var2);
    }

    protected void init(Context var1, AttributeSet var2) {
    }

    public void show(boolean var1) {
        if(var1 != ( 0 == this.getVisibility())) {//0
            ViewGroup.LayoutParams var2 = this.mContainer.getLayoutParams();
            if(null != var2) {
                if(var1) {
                    var2.height = -2;
                } else {
                    var2.height = 0;
                }

                this.setVisibility(var1 ? 0: 4);
            }

        }
    }

    public void setLastUpdatedLabel(CharSequence var1) {
    }

    public void setLoadingDrawable(Drawable var1) {
    }

    public void setPullLabel(CharSequence var1) {
    }

    public void setRefreshingLabel(CharSequence var1) {
    }

    public void setReleaseLabel(CharSequence var1) {
    }

    public void setState(State var1) {
        if(this.mCurState != var1) {
            this.mPreState = this.mCurState;
            this.mCurState = var1;
            this.onStateChanged(var1, this.mPreState);
        }

    }

    public State getState() {
        return this.mCurState;
    }

    public void onPull(float var1) {
    }

    protected State getPreState() {
        return this.mPreState;
    }

    protected void onStateChanged(State var1, State var2) {
        switch(LoadingLayout.SyntheticClass_1.$SwitchMap$io$dcloud$common$adapter$ui$fresh$ILoadingLayout$State[var1.ordinal()]) {
            case 1:
                this.onReset();
                break;
            case 2:
                this.onReleaseToRefresh();
                break;
            case 3:
                this.onPullToRefresh();
                break;
            case 4:
                this.onRefreshing();
                break;
            case 5:
                this.onNoMoreData();
        }

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

    public abstract int getContentSize();

    protected abstract View createLoadingView(Context var1, AttributeSet var2);
}
