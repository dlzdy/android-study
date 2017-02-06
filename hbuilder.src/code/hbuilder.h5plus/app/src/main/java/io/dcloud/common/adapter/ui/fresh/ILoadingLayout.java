//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.common.adapter.ui.fresh;

public interface ILoadingLayout {
    void setState(ILoadingLayout.State var1);

    ILoadingLayout.State getState();

    int getContentSize();

    void onPull(float var1);

    public static enum State {
        NONE,
        RESET,
        PULL_TO_REFRESH,
        RELEASE_TO_REFRESH,
        REFRESHING,
        LOADING,
        NO_MORE_DATA;
    }
}
