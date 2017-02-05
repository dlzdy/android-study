//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.dcloud.android.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.Scroller;

import org.json.JSONObject;

import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.PdrUtil;

public class SlideLayout extends AbsoluteLayout {
    private boolean mInterceptEventEnable = true;
    private boolean mIsHandledTouchEvent = false;
    private int mTouchSlop;
    private float mLastMotionX = -1.0F;
    private VelocityTracker mVelocityTracker;
    private static final int SNAP_VELOCITY = 1000;
    private boolean mCanDoSlideTransverseEvent = false;
    private int mSlideTransverseLeftMaxWitch = 0;
    private int mSlideTransverseRightMaxWitch = 0;
    private int mSlideLeftPosition = -1;
    private int mSlideRightPosition = -1;
    boolean isRightSlide = false;
    boolean isLeftSlide = false;
    boolean isSlideOpen = false;
    private static final int SCROLL_DURATION = 150;
    private Scroller mScroller = new Scroller(this.getContext());
    private float mFirstX = 0.0F;
    private static String LEFT = "left";
    private static String RIGHT = "right";
    private static String BEFORE_SLIDE = "beforeSlide";
    private static String AFTER_SLIDE = "afterSlide";
    private SlideLayout.OnStateChangeListener mChangeListener;

    public void setOnStateChangeListener(SlideLayout.OnStateChangeListener var1) {
        this.mChangeListener = var1;
    }

    public SlideLayout(Context var1) {
        super(var1);
        this.mTouchSlop = ViewConfiguration.get(var1).getScaledTouchSlop();
    }

    public void setWidth(int var1) {
        ViewGroup.LayoutParams var2 = this.getLayoutParams();
        if(var2 != null) {
            var2.width = var1;
            this.requestLayout();
        }

    }

    public void setHeight(int var1) {
        ViewGroup.LayoutParams var2 = this.getLayoutParams();
        if(var2 != null) {
            var2.height = var1;
            this.requestLayout();
        }

    }

    public void initSlideInfo(JSONObject var1, float var2, int var3) {
        JSONObject var4 = JSONUtil.getJSONObject(var1, "slideoffset");
        if(var4 != null) {
            JSONObject var5 = JSONUtil.getJSONObject(var1, "position");
            String var6;
            String var7;
            if(var5 != null) {
                var6 = var5.optString(LEFT);
                var7 = var5.optString(RIGHT);
                if(!TextUtils.isEmpty(var6)) {
                    this.mSlideLeftPosition = PdrUtil.convertToScreenInt(var6, var3, var3 / 2, var2);
                }

                if(!TextUtils.isEmpty(var7)) {
                    this.mSlideRightPosition = PdrUtil.convertToScreenInt(var7, var3, var3 / 2, var2);
                }
            }

            this.mInterceptEventEnable = var1.optBoolean("preventTouchEvent", true);
            var6 = JSONUtil.getString(var4, LEFT);
            if(!TextUtils.isEmpty(var6)) {
                this.isLeftSlide = this.mSlideLeftPosition > 0;
                this.mSlideTransverseLeftMaxWitch = PdrUtil.convertToScreenInt(var6, var3, var3 / 2, var2);
            }

            var7 = JSONUtil.getString(var4, RIGHT);
            if(!TextUtils.isEmpty(var7)) {
                this.isRightSlide = this.mSlideRightPosition > 0;
                this.mSlideTransverseRightMaxWitch = PdrUtil.convertToScreenInt(var7, var3, var3 / 2, var2);
            }
        }

    }

    public boolean onInterceptTouchEvent(MotionEvent var1) {
        int var2 = var1.getAction();
        if(!this.mInterceptEventEnable) {
            return false;
        } else if(!this.isLeftSlide && !this.isRightSlide) {
            return false;
        } else if(var2 != 3 && var2 != 1) {
            if(var2 != 0 && this.mIsHandledTouchEvent) {
                return true;
            } else {
                switch(var2) {
                    case 0:
                        this.mLastMotionX = var1.getX();
                        this.mFirstX = var1.getX();
                        this.mIsHandledTouchEvent = false;
                        this.mCanDoSlideTransverseEvent = false;
                        break;
                    case 2:
                        float var3 = var1.getX();
                        int var4 = (int)Math.abs(var3 - this.mFirstX);
                        if(var4 > this.mTouchSlop) {
                            this.enableChildrenCache();
                            this.mIsHandledTouchEvent = true;
                            this.mCanDoSlideTransverseEvent = true;
                            this.requestDisallowInterceptTouchEvent(true);
                        }
                }

                return this.mIsHandledTouchEvent;
            }
        } else {
            this.mIsHandledTouchEvent = false;
            this.clearChildrenCache();
            return this.mIsHandledTouchEvent;
        }
    }

    public boolean onTouchEvent(MotionEvent var1) {
        if(this.mCanDoSlideTransverseEvent && this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }

        int var4;
        int var5;
        int var6;
        switch(var1.getAction()) {
            case 0:
                this.mIsHandledTouchEvent = false;
                if(!this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                }
                break;
            case 1:
            case 3:
                if(this.mIsHandledTouchEvent) {
                    this.mIsHandledTouchEvent = false;
                    if(this.mCanDoSlideTransverseEvent) {
                        this.requestDisallowInterceptTouchEvent(false);
                        VelocityTracker var7 = this.mVelocityTracker;
                        var7.computeCurrentVelocity(1000);
                        var4 = (int)var7.getXVelocity();
                        var5 = this.getScrollX();
                        if(var4 > 1000) {
                            if(var5 < 0 && this.isLeftSlide && this.mSlideLeftPosition >= this.mSlideTransverseLeftMaxWitch) {
                                var6 = this.mSlideTransverseLeftMaxWitch - Math.abs(var5);
                                this.smoothScrollTo(-var6, 0);
                                this.isSlideOpen = true;
                                this.setState(LEFT, AFTER_SLIDE);
                            } else if(var5 > 0 && this.isRightSlide) {
                                this.smoothScrollTo(-var5, 0);
                                this.isSlideOpen = false;
                                this.setState(RIGHT, BEFORE_SLIDE);
                            } else {
                                this.upSlideTo(var5);
                            }
                        } else if(var4 < -1000) {
                            if(var5 < 0 && this.isLeftSlide) {
                                this.smoothScrollTo(-var5, 0);
                                this.isSlideOpen = false;
                                this.setState(LEFT, BEFORE_SLIDE);
                            } else if(var5 > 0 && this.isRightSlide && this.mSlideRightPosition >= this.mSlideTransverseRightMaxWitch) {
                                var6 = this.mSlideTransverseRightMaxWitch - Math.abs(var5);
                                this.smoothScrollTo(var6, 0);
                                this.isSlideOpen = true;
                                this.setState(RIGHT, AFTER_SLIDE);
                            } else {
                                this.upSlideTo(var5);
                            }
                        } else {
                            this.upSlideTo(var5);
                        }

                        if(this.mVelocityTracker != null) {
                            this.mVelocityTracker.recycle();
                            this.mVelocityTracker = null;
                        }
                    }
                }

                if(this.mVelocityTracker != null) {
                    this.mVelocityTracker.recycle();
                    this.mVelocityTracker = null;
                }
                break;
            case 2:
                if(this.mVelocityTracker != null) {
                    this.mVelocityTracker.addMovement(var1);
                }

                float var2 = var1.getX();
                if(this.mCanDoSlideTransverseEvent) {
                    int var3 = (int)(this.mLastMotionX - var2);
                    this.mLastMotionX = var2;
                    var4 = this.getScrollX();
                    var5 = this.getChildAt(this.getChildCount() - 1).getRight() - var4 - this.getWidth();
                    if(var4 == 0) {
                        if(this.isRightSlide && var3 > 0) {
                            this.scrollBy(var3, 0);
                        } else if(this.isLeftSlide && var3 < 0) {
                            this.scrollBy(var3, 0);
                        }
                    } else if(var4 > 0 && var5 < 0 && this.isRightSlide) {
                        var6 = var3 + var4;
                        if(Math.abs(var6) <= this.mSlideRightPosition) {
                            if(var6 < 0) {
                                this.scrollBy(0, 0);
                            } else if(Math.abs(var6) >= this.mSlideTransverseRightMaxWitch) {
                                this.scrollBy((int)((double)var3 / 1.5D), 0);
                            } else {
                                this.scrollBy(var3, 0);
                            }
                        }
                    } else if(var4 < 0 && var5 > 0 && this.isLeftSlide) {
                        var6 = var3 + var4;
                        if(Math.abs(var6) <= this.mSlideLeftPosition) {
                            if(var6 > 0) {
                                this.scrollBy(0, 0);
                            } else if(Math.abs(var6) >= this.mSlideTransverseLeftMaxWitch) {
                                this.scrollBy((int)((double)var3 / 1.5D), 0);
                            } else {
                                this.scrollBy(var3, 0);
                            }
                        }
                    }
                }
        }

        return super.onTouchEvent(var1);
    }

    public void upSlideTo(int var1) {
        int var2;
        if(var1 < 0 && Math.abs(var1) >= this.mSlideTransverseLeftMaxWitch / 2 && this.mSlideLeftPosition >= this.mSlideTransverseLeftMaxWitch) {
            var2 = this.mSlideTransverseLeftMaxWitch - Math.abs(var1);
            this.smoothScrollTo(-var2, 0);
            this.isSlideOpen = true;
            this.setState(LEFT, AFTER_SLIDE);
        } else if(var1 > 0 && Math.abs(var1) >= this.mSlideTransverseRightMaxWitch / 2 && this.mSlideRightPosition >= this.mSlideTransverseRightMaxWitch) {
            var2 = this.mSlideTransverseRightMaxWitch - Math.abs(var1);
            this.smoothScrollTo(var2, 0);
            this.isSlideOpen = true;
            this.setState(RIGHT, AFTER_SLIDE);
        } else {
            if(var1 > 0) {
                this.smoothScrollTo(-var1, 0);
                this.setState(RIGHT, BEFORE_SLIDE);
            } else {
                this.smoothScrollTo(-var1, 0);
                this.setState(LEFT, BEFORE_SLIDE);
            }

            this.isSlideOpen = false;
        }

    }

    private void setState(final String var1, final String var2) {
        if(this.mChangeListener != null) {
            this.postDelayed(new Runnable() {
                public void run() {
                    SlideLayout.this.mChangeListener.onStateChanged(var2, var1);
                }
            }, 150L);
        }

    }

    public void setOffset(String var1, String var2, float var3) {
        int var4 = PdrUtil.convertToScreenInt(var2, this.getWidth(), 0, var3);
        int var5 = this.getScrollX();
        int var6;
        if(var1.equals(LEFT)) {
            if(var4 == 0) {
                if(var5 != 0) {
                    this.smoothScrollTo(-var5, 0);
                    this.setState(LEFT, BEFORE_SLIDE);
                }
            } else {
                if(var4 > this.mSlideLeftPosition) {
                    var4 = this.mSlideLeftPosition;
                }

                var6 = var4 - Math.abs(var5);
                this.smoothScrollTo(-var6, 0);
                this.postDelayed(new Runnable() {
                    public void run() {
                        SlideLayout.this.upSlideTo(SlideLayout.this.getScrollX());
                    }
                }, (long)(var6 * 2 + 200));
            }
        } else if(var4 == 0) {
            if(var5 != 0) {
                this.smoothScrollTo(-var5, 0);
                this.setState(RIGHT, BEFORE_SLIDE);
            }
        } else {
            if(var4 > this.mSlideRightPosition) {
                var4 = this.mSlideRightPosition;
            }

            var6 = var4 - Math.abs(var5);
            this.smoothScrollTo(var6, 0);
            this.postDelayed(new Runnable() {
                public void run() {
                    SlideLayout.this.upSlideTo(SlideLayout.this.getScrollX());
                }
            }, (long)(var6 * 2 + 200));
        }

    }

    public void computeScroll() {
        if(this.mScroller.computeScrollOffset()) {
            this.scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
            this.postInvalidate();
        } else {
            this.clearChildrenCache();
        }

        super.computeScroll();
    }

    private void smoothScrollTo(int var1, int var2) {
        this.enableChildrenCache();
        this.mScroller.startScroll(this.getScrollX(), 0, var1, 0, Math.abs(var1) * 2);
        this.invalidate();
    }

    void enableChildrenCache() {
        int var1 = this.getChildCount();

        for(int var2 = 0; var2 < var1; ++var2) {
            View var3 = this.getChildAt(var2);
            var3.setDrawingCacheEnabled(true);
        }

    }

    void clearChildrenCache() {
        int var1 = this.getChildCount();

        for(int var2 = 0; var2 < var1; ++var2) {
            View var3 = this.getChildAt(var2);
            var3.setDrawingCacheEnabled(false);
        }

    }

    public void setInterceptTouchEventEnabled(boolean var1) {
        this.mIsHandledTouchEvent = var1;
    }

    public void reset() {
        int var1 = this.getScrollX();
        if(var1 != 0) {
            this.smoothScrollTo(-var1, 0);
            if(var1 < 0) {
                this.setState(LEFT, BEFORE_SLIDE);
            } else {
                this.setState(RIGHT, BEFORE_SLIDE);
            }

        }
    }

    public interface OnStateChangeListener {
        void onStateChanged(String var1, String var2);
    }
}
