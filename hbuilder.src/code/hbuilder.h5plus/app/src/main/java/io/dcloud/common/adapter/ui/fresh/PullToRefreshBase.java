//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.common.adapter.ui.fresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import io.dcloud.common.adapter.ui.fresh.ILoadingLayout.State;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.util.BaseInfo;

@SuppressLint({"NewApi"})
public abstract class PullToRefreshBase<T extends View> extends LinearLayout implements IPullToRefresh<T> {
    private static final int SCROLL_DURATION = 150;
    private static final float OFFSET_RADIO = 2.5F;
    private float mLastMotionY = -1.0F;
    private float mLastMotionX = -1.0F;
    private boolean mCanDoPullDownEvent = false;
    PullToRefreshBase.OnPullUpListener mOnPullUpListener;
    private PullToRefreshBase.OnRefreshListener<T> mRefreshListener;
    PullToRefreshBase.OnStateChangeListener mOnStateChangeListener;
    private LoadingLayout mHeaderLayout;
    private LoadingLayout mFooterLayout;
    private int mHeaderPullDownMaxHeight;
    private int mHeaderHeight;
    private int mFooterHeight;
    private boolean mPullRefreshEnabled = true;
    private boolean mPullLoadEnabled = false;
    private boolean mScrollLoadEnabled = false;
    private boolean mInterceptEventEnable = true;
    private boolean mIsHandledTouchEvent = false;
    private int mTouchSlop;
    private State mPullDownState;
    private State mPullUpState;
    T mRefreshableView;
    private PullToRefreshBase<T>.SmoothScrollRunnable mSmoothScrollRunnable;
    float mLastMotionY_pullup;
    final int UP;
    final int DOWN;
    final int LEFT;
    final int RIGHT;

    public PullToRefreshBase(Context var1) {
        super(var1);
        this.mPullDownState = State.NONE;
        this.mPullUpState = State.NONE;
        this.mLastMotionY_pullup = -1.0F;
        this.UP = 0;
        this.DOWN = 1;
        this.LEFT = 2;
        this.RIGHT = 3;
    }

    public void init(Context var1) {
        this.setOrientation(VERTICAL);//1
        this.mTouchSlop = ViewConfiguration.get(var1).getScaledTouchSlop();
        this.mHeaderLayout = this.createHeaderLoadingLayout(var1);
        this.mFooterLayout = this.createFooterLoadingLayout(var1);
        this.addHeaderAndFooter(var1);
        this.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                PullToRefreshBase.this.refreshLoadingViewsSize();
                PullToRefreshBase.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        this.smoothScrollTo(0);
    }

    public void setHeaderHeight(int var1) {
        this.mHeaderHeight = var1;
    }

    public void setHeaderPullDownMaxHeight(int var1) {
        this.mHeaderPullDownMaxHeight = var1;
    }

    public void refreshLoadingViewsSize() {
        int var1 = this.mHeaderHeight;
        int var2 = null != this.mFooterLayout?this.mFooterLayout.getContentSize():0;
        if(var1 < 0) {
            var1 = 0;
        }

        if(var2 < 0) {
            var2 = 0;
        }

        this.mHeaderHeight = var1;
        this.mFooterHeight = var2;
        var1 = null != this.mHeaderLayout?this.mHeaderLayout.getMeasuredHeight():0;
        Logger.d("View_Visible_Path", "PullToRefreshBase.refreshLoadingViewsSize mHeaderHeight=" + this.mHeaderHeight + ";headerHeight=" + var1);
        var2 = null != this.mFooterLayout?this.mFooterLayout.getMeasuredHeight():0;
        if(0 == var2) {
            var2 = this.mFooterHeight;
        }

        int var3 = this.getPaddingLeft();
        int var4 = this.getPaddingTop();
        int var5 = this.getPaddingRight();
        int var6 = this.getPaddingBottom();
        var4 = -var1;
        var6 = -var2;
        this.setPadding(var3, var4, var5, var6);
    }

    protected final void onSizeChanged(int var1, int var2, int var3, int var4) {
        super.onSizeChanged(var1, var2, var3, var4);
        this.refreshLoadingViewsSize();
        this.refreshRefreshableViewSize(var1, var2);
        this.post(new Runnable() {
            public void run() {
                PullToRefreshBase.this.requestLayout();
            }
        });
    }

    public void setOrientation(int var1) {
        if(VERTICAL != var1) {//1
            throw new IllegalArgumentException("This class only supports VERTICAL orientation.");
        } else {
            super.setOrientation(var1);
        }
    }

    private boolean handlePullUpEvent(MotionEvent var1) {
        int var2 = var1.getAction();
        if(var2 == 1) {
            float var3 = var1.getY() - this.mLastMotionY_pullup;
            this.mLastMotionY_pullup = var3;
            if(var3 < -3.0F && this.isReadyForPullUp()) {
                this.mOnPullUpListener.onPlusScrollBottom();
                return false;
            }
        } else if(var2 == 0) {
            this.mLastMotionY_pullup = var1.getY();
        }

        return false;
    }

    private double getAngle(float var1, float var2, float var3, float var4) {
        return Math.atan2((double)(var4 - var2), (double)(var3 - var1)) * 180.0D / 3.141592653589793D;
    }

    private int getDirectionByAngle(double var1) {
        return var1 < -45.0D && var1 > -135.0D?0:(var1 >= 45.0D && var1 < 135.0D?1:(var1 < 135.0D && var1 > -135.0D?(var1 >= -45.0D && var1 <= 45.0D?3:-1):2));
    }

    private boolean canDoPullDownEvent(float var1, float var2) {
        if(var2 < this.mLastMotionY) {
            return true;
        } else {
            if(!this.mCanDoPullDownEvent) {
                this.mCanDoPullDownEvent = 1 == this.getDirectionByAngle(this.getAngle(this.mLastMotionX, this.mLastMotionY, var1, var2));
            }

            return this.mCanDoPullDownEvent;
        }
    }

    public final boolean onInterceptTouchEvent(MotionEvent var1) {
        if(BaseInfo.isShowTitleBar(this.getContext()) && (this.isPullLoadEnabled() || this.isPullRefreshEnabled())) {
            this.mRefreshableView.onTouchEvent(var1);
        }

        if(!this.isInterceptTouchEventEnabled()) {
            return false;
        } else if(!this.isPullLoadEnabled() && !this.isPullRefreshEnabled()) {
            return false;
        } else {
            int var2 = var1.getAction();
            if(var2 != 3 && var2 != 1) {
                if(var2 != 0 && this.mIsHandledTouchEvent) {
                    return true;
                } else {
                    switch(var2) {
                        case 0:
                            this.mLastMotionY = var1.getY();
                            this.mLastMotionX = var1.getX();
                            this.mIsHandledTouchEvent = false;
                            this.mCanDoPullDownEvent = false;
                            break;
                        case 2:
                            if(this.canDoPullDownEvent(var1.getX(), var1.getY())) {
                                float var3 = var1.getY() - this.mLastMotionY;
                                float var4 = Math.abs(var3);
                                if(var4 > (float)this.mTouchSlop || this.isPullRefreshing() || !this.isPullLoading()) {
                                    this.mLastMotionY = var1.getY();
                                    if(this.isPullRefreshEnabled() && this.isReadyForPullDown()) {
                                        this.mIsHandledTouchEvent = Math.abs(this.getScrollYValue()) > 0 || var3 > 0.5F;
                                        if(this.mIsHandledTouchEvent) {
                                            this.mRefreshableView.onTouchEvent(var1);
                                            this.requestDisallowInterceptTouchEvent(true);
                                        }
                                    } else if(this.isPullLoadEnabled() && this.isReadyForPullUp()) {
                                        this.mIsHandledTouchEvent = Math.abs(this.getScrollYValue()) > 0 || var3 < -0.5F;
                                        if(this.mIsHandledTouchEvent) {
                                            this.requestDisallowInterceptTouchEvent(true);
                                        }
                                    }
                                }
                            }
                    }

                    return this.mIsHandledTouchEvent?this.mIsHandledTouchEvent:super.onTouchEvent(var1);
                }
            } else {
                this.mIsHandledTouchEvent = false;
                this.mCanDoPullDownEvent = false;
                return this.mIsHandledTouchEvent;
            }
        }
    }

    public boolean onTouchEvent(MotionEvent var1) {
        if(BaseInfo.isShowTitleBar(this.getContext()) && (this.isPullLoadEnabled() || this.isPullRefreshEnabled())) {
            this.mRefreshableView.onTouchEvent(var1);
        }

        boolean var2 = false;
        switch(var1.getAction()) {
            case 0:
                this.mLastMotionY = var1.getY();
                this.mIsHandledTouchEvent = false;
                break;
            case 1:
            case 3:
                if(this.mIsHandledTouchEvent) {
                    this.mIsHandledTouchEvent = false;
                    if(this.isReadyForPullDown()) {
                        if(this.mPullRefreshEnabled && this.mPullDownState == State.RELEASE_TO_REFRESH) {
                            this.startRefreshing();
                            var2 = true;
                        }

                        this.resetHeaderLayout();
                    } else if(this.isReadyForPullUp()) {
                        if(this.isPullLoadEnabled() && this.mPullUpState == State.RELEASE_TO_REFRESH) {
                            this.startLoading();
                            var2 = true;
                        }

                        this.resetFooterLayout();
                    }
                }

                this.requestDisallowInterceptTouchEvent(false);
                break;
            case 2:
                float var3 = var1.getY() - this.mLastMotionY;
                this.mLastMotionY = var1.getY();
                if(this.isPullRefreshEnabled() && this.isReadyForPullDown()) {
                    this.pullHeaderLayout(var3 / 2.5F);
                    var2 = true;
                } else if(this.isPullLoadEnabled() && this.isReadyForPullUp()) {
                    this.pullFooterLayout(var3 / 2.5F);
                    var2 = true;
                } else {
                    this.mIsHandledTouchEvent = false;
                }
        }

        return var2;
    }

    public void setPullRefreshEnabled(boolean var1) {
        this.mPullRefreshEnabled = var1;
    }

    public void setPullLoadEnabled(boolean var1) {
        this.mPullLoadEnabled = var1;
    }

    public void setScrollLoadEnabled(boolean var1) {
        this.mScrollLoadEnabled = var1;
    }

    public boolean isPullRefreshEnabled() {
        return this.mPullRefreshEnabled && null != this.mHeaderLayout;
    }

    public boolean isPullLoadEnabled() {
        return this.mPullLoadEnabled && null != this.mFooterLayout;
    }

    public boolean isScrollLoadEnabled() {
        return this.mScrollLoadEnabled;
    }

    public void setOnOnPullUpListener(PullToRefreshBase.OnPullUpListener var1) {
        this.mOnPullUpListener = var1;
    }

    public void setOnRefreshListener(PullToRefreshBase.OnRefreshListener<T> var1) {
        this.mRefreshListener = var1;
    }

    public void setOnStateChangeListener(PullToRefreshBase.OnStateChangeListener var1) {
        this.mOnStateChangeListener = var1;
    }

    public void onPullDownRefreshComplete() {
        if(this.isPullRefreshing()) {
            this.mPullDownState = State.RESET;
            this.onStateChanged(State.RESET, true);
            this.postDelayed(new Runnable() {
                public void run() {
                    PullToRefreshBase.this.setInterceptTouchEventEnabled(true);
                    PullToRefreshBase.this.mHeaderLayout.setState(State.RESET);
                }
            }, this.getSmoothScrollDuration());
            this.resetHeaderLayout();
            this.setInterceptTouchEventEnabled(false);
        }

    }

    public void onPullUpRefreshComplete() {
        if(this.isPullLoading()) {
            this.mPullUpState = State.RESET;
            this.onStateChanged(State.RESET, false);
            this.postDelayed(new Runnable() {
                public void run() {
                    PullToRefreshBase.this.setInterceptTouchEventEnabled(true);
                    PullToRefreshBase.this.mFooterLayout.setState(State.RESET);
                }
            }, this.getSmoothScrollDuration());
            this.resetFooterLayout();
            this.setInterceptTouchEventEnabled(false);
        }

    }

    public T getRefreshableView() {
        return this.mRefreshableView;
    }

    public LoadingLayout getHeaderLoadingLayout() {
        return this.mHeaderLayout;
    }

    public LoadingLayout getFooterLoadingLayout() {
        return this.mFooterLayout;
    }

    public void setLastUpdatedLabel(CharSequence var1) {
        if(null != this.mHeaderLayout) {
            this.mHeaderLayout.setLastUpdatedLabel(var1);
        }

        if(null != this.mFooterLayout) {
            this.mFooterLayout.setLastUpdatedLabel(var1);
        }

    }

    public void doPullRefreshing(final boolean var1, long var2) {
        this.postDelayed(new Runnable() {
            public void run() {
                int var1x = -PullToRefreshBase.this.mHeaderHeight;
                int var2 = var1?150:0;
                PullToRefreshBase.this.startRefreshing();
                PullToRefreshBase.this.smoothScrollTo(var1x, var2, 0L);
            }
        }, var2);
    }

    public void setRefreshableView(T var1) {
        this.mRefreshableView = var1;
    }

    protected abstract boolean isReadyForPullDown();

    protected abstract boolean isReadyForPullUp();

    protected LoadingLayout createHeaderLoadingLayout(Context var1) {
        return (LoadingLayout)(this.mHeaderLayout == null?new HeaderLoadingLayout(var1):this.mHeaderLayout);
    }

    protected LoadingLayout createFooterLoadingLayout(Context var1) {
        return null;
    }

    protected long getSmoothScrollDuration() {
        return 150L;
    }

    protected void refreshRefreshableViewSize(int var1, int var2) {
        if(null != this.mRefreshableView) {
            LayoutParams var3 = (LayoutParams)this.mRefreshableView.getLayoutParams();
            if(var3.height != var2) {
                var3.height = var2;
                this.mRefreshableView.requestLayout();
            }
        }

    }

    public void addRefreshableView(T var1) {
        byte var2 = -1;
        byte var3 = -1;
        this.addView(var1, new LayoutParams(var2, var3));
    }

    protected void addHeaderAndFooter(Context var1) {
        LayoutParams var2 = new LayoutParams(-1, -2);
        LoadingLayout var3 = this.mHeaderLayout;
        LoadingLayout var4 = this.mFooterLayout;
        if(null != var3) {
            if(this == var3.getParent()) {
                this.removeView(var3);
            }

            this.addView(var3, 0, var2);
        }

        if(null != var4) {
            if(this == var4.getParent()) {
                this.removeView(var4);
            }

            this.addView(var4, -1, var2);
        }

    }

    protected void pullHeaderLayout(float var1) {
        int var2 = this.getScrollYValue();
        if(var1 <= 0.0F || Math.abs(var2) < this.mHeaderPullDownMaxHeight) {
            if(var1 < 0.0F && (float)var2 - var1 >= 0.0F) {
                this.setScrollTo(0, 0);
            } else {
                this.setScrollBy(0, -((int)var1));
                if(null != this.mHeaderLayout && 0 != this.mHeaderHeight) {
                    float var3 = (float)Math.abs(this.getScrollYValue()) / (float)this.mHeaderHeight;
                    this.mHeaderLayout.onPull(var3);
                }

                int var4 = Math.abs(this.getScrollYValue());
                if(this.isPullRefreshEnabled() && !this.isPullRefreshing()) {
                    if(var4 > this.mHeaderHeight) {
                        this.mPullDownState = State.RELEASE_TO_REFRESH;
                    } else {
                        this.mPullDownState = State.PULL_TO_REFRESH;
                    }

                    this.mHeaderLayout.setState(this.mPullDownState);
                    this.onStateChanged(this.mPullDownState, true);
                }

            }
        }
    }

    protected void pullFooterLayout(float var1) {
        int var2 = this.getScrollYValue();
        if(var1 > 0.0F && (float)var2 - var1 <= 0.0F) {
            this.setScrollTo(0, 0);
        } else {
            this.setScrollBy(0, -((int)var1));
            if(null != this.mFooterLayout && 0 != this.mFooterHeight) {
                float var3 = (float)Math.abs(this.getScrollYValue()) / (float)this.mFooterHeight;
                this.mFooterLayout.onPull(var3);
            }

            int var4 = Math.abs(this.getScrollYValue());
            if(this.isPullLoadEnabled() && !this.isPullLoading()) {
                if(var4 > this.mFooterHeight) {
                    this.mPullUpState = State.RELEASE_TO_REFRESH;
                } else {
                    this.mPullUpState = State.PULL_TO_REFRESH;
                }

                this.mFooterLayout.setState(this.mPullUpState);
                this.onStateChanged(this.mPullUpState, false);
            }

        }
    }

    protected void resetHeaderLayout() {
        int var1 = Math.abs(this.getScrollYValue());
        boolean var2 = this.isPullRefreshing();
        if(!var2 || var1 > this.mHeaderHeight) {
            if(var2) {
                this.smoothScrollTo(-this.mHeaderHeight);
            } else {
                this.smoothScrollTo(0);
            }

        }
    }

    protected void resetFooterLayout() {
        int var1 = Math.abs(this.getScrollYValue());
        boolean var2 = this.isPullLoading();
        if(var2 && var1 <= this.mFooterHeight) {
            this.smoothScrollTo(0);
        } else {
            if(var2) {
                this.smoothScrollTo(this.mFooterHeight);
            } else {
                this.smoothScrollTo(0);
            }

        }
    }

    protected boolean isPullRefreshing() {
        return this.mPullDownState == State.REFRESHING;
    }

    protected boolean isPullLoading() {
        return this.mPullUpState == State.REFRESHING;
    }

    protected void startRefreshing() {
        if(!this.isPullRefreshing()) {
            this.mPullDownState = State.REFRESHING;
            this.onStateChanged(State.REFRESHING, true);
            if(null != this.mHeaderLayout) {
                this.mHeaderLayout.setState(State.REFRESHING);
            }

            if(null != this.mRefreshListener) {
                this.postDelayed(new Runnable() {
                    public void run() {
                        PullToRefreshBase.this.mRefreshListener.onPullDownToRefresh(PullToRefreshBase.this);
                    }
                }, this.getSmoothScrollDuration());
            }

        }
    }

    protected void startLoading() {
        if(!this.isPullLoading()) {
            this.mPullUpState = State.REFRESHING;
            this.onStateChanged(State.REFRESHING, false);
            if(null != this.mFooterLayout) {
                this.mFooterLayout.setState(State.REFRESHING);
            }

            if(null != this.mRefreshListener) {
                this.postDelayed(new Runnable() {
                    public void run() {
                        PullToRefreshBase.this.mRefreshListener.onPullUpToRefresh(PullToRefreshBase.this);
                    }
                }, this.getSmoothScrollDuration());
            }

        }
    }

    protected void onStateChanged(State var1, boolean var2) {
        if(this.mOnStateChangeListener != null) {
            this.mOnStateChangeListener.onStateChanged(var1, var2);
        }

    }

    private void setScrollTo(int var1, int var2) {
        this.scrollTo(var1, var2);
    }

    private void setScrollBy(int var1, int var2) {
        this.scrollBy(var1, var2);
    }

    private int getScrollYValue() {
        return this.getScrollY();
    }

    public void smoothScrollTo(int var1) {
        this.smoothScrollTo(var1, this.getSmoothScrollDuration(), 0L);
    }

    private void smoothScrollTo(int var1, long var2, long var4) {
        if(null != this.mSmoothScrollRunnable) {
            this.mSmoothScrollRunnable.stop();
        }

        int var6 = this.getScrollYValue();
        boolean var7 = var6 != var1;
        if(var7) {
            this.mSmoothScrollRunnable = new PullToRefreshBase.SmoothScrollRunnable(var6, var1, var2);
        }

        if(var7) {
            if(var4 > 0L) {
                this.postDelayed(this.mSmoothScrollRunnable, var4);
            } else {
                this.post(this.mSmoothScrollRunnable);
            }
        }

    }

    public void setInterceptTouchEventEnabled(boolean var1) {
        this.mInterceptEventEnable = var1;
    }

    public boolean isInterceptTouchEventEnabled() {
        return this.mInterceptEventEnable;
    }

    final class SmoothScrollRunnable implements Runnable {
        private final Interpolator mInterpolator;
        private final int mScrollToY;
        private final int mScrollFromY;
        private final long mDuration;
        private boolean mContinueRunning = true;
        private long mStartTime = -1L;
        private int mCurrentY = -1;

        public SmoothScrollRunnable(int var2, int var3, long var4) {
            this.mScrollFromY = var2;
            this.mScrollToY = var3;
            this.mDuration = var4;
            this.mInterpolator = new DecelerateInterpolator();
        }

        public void run() {
            if(this.mDuration <= 0L) {
                PullToRefreshBase.this.setScrollTo(0, this.mScrollToY);
            } else {
                if(this.mStartTime == -1L) {
                    this.mStartTime = System.currentTimeMillis();
                } else {
                    long var1 = 1000L;
                    long var3 = 1000L * (System.currentTimeMillis() - this.mStartTime) / this.mDuration;
                    var3 = Math.max(Math.min(var3, 1000L), 0L);
                    int var5 = Math.round((float)(this.mScrollFromY - this.mScrollToY) * this.mInterpolator.getInterpolation((float)var3 / 1000.0F));
                    this.mCurrentY = this.mScrollFromY - var5;
                    PullToRefreshBase.this.setScrollTo(0, this.mCurrentY);
                }

                if(this.mContinueRunning && this.mScrollToY != this.mCurrentY) {
                    PullToRefreshBase.this.postDelayed(this, 16L);
                }

            }
        }

        public void stop() {
            this.mContinueRunning = false;
            PullToRefreshBase.this.removeCallbacks(this);
        }
    }

    public interface OnPullUpListener {
        void onPlusScrollBottom();
    }

    public interface OnStateChangeListener {
        void onStateChanged(State var1, boolean var2);
    }

    public interface OnRefreshListener<V extends View> {
        void onPullDownToRefresh(PullToRefreshBase<V> var1);

        void onPullUpToRefresh(PullToRefreshBase<V> var1);
    }
}
