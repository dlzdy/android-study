//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.common.a;

import android.app.Activity;
import android.text.TextUtils;
import io.dcloud.common.DHInterface.IMgr.MgrType;
import io.dcloud.common.DHInterface.ISysEventListener.SysEventType;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MobilePhoneModel;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.ShortCutUtil;
import java.util.ArrayList;
import java.util.Iterator;

class b {
    a a = null;
    private ArrayList<d> b = new ArrayList();
    private ArrayList<String> c;

    b(a var1) {
        this.c = BaseInfo.sRunningApp = new ArrayList();
        this.a = var1;
    }

    boolean a(d var1, SysEventType var2, Object var3) {
        if(var2.equals(SysEventType.onResume)) {
            for(int var4 = 0; var4 < this.b.size(); ++var4) {
                d var5 = (d)this.b.get(var4);
                if(var5 != null) {
                    boolean var6 = ShortCutUtil.isOpsCreateShortcut(var5.getActivity(), var5.obtainAppId());
                    if(var6 || ShortCutUtil.isRunShortCut(var5.obtainAppId())) {
                        if(!MobilePhoneModel.isSmartisanLauncherPhone(var5.getActivity())) {
                            ShortCutUtil.onResumeCreateShortcut(var5);
                        } else {
                            ShortCutUtil.commitShortcut(var5, 12, true, false, true, 0);
                        }
                    }
                }
            }
        } else if(var1 != null && var2.equals(SysEventType.onKeyDown)) {
            Object[] var11 = (Object[])((Object[])var3);
            int var13 = ((Integer)var11[0]).intValue();
            if(var13 == 4 && BaseInfo.isPostChcekShortCut) {
                ShortCutUtil.isHasShortCut(var1, 500L, "back");
                return true;
            }
        }

        boolean var12 = false;
        boolean var14 = var1 == null;
        int var15 = this.b.size();
        d var7 = null;

        for(int var8 = var15 - 1; var8 >= 0; --var8) {
            d var9 = (d)this.b.get(var8);
            boolean var10 = var14;
            if(!var14) {
                var10 = var9 == var1;
            }

            if(var10) {
                var7 = var9;
                var12 |= var9.onExecute(var2, var3);
                if(var12 && !d.a(var2)) {
                    break;
                }
            }
        }

        if(!var12 && var2.equals(SysEventType.onKeyUp) && var15 > 1 && var7 != null) {
            Object[] var16 = (Object[])((Object[])var3);
            int var17 = ((Integer)var16[0]).intValue();
            if(var17 == 4) {
                this.a.processEvent(MgrType.WindowMgr, 20, var7);
                var12 = true;
            }
        }

        return var12;
    }

    protected d a(String var1) {
        int var2 = this.c.indexOf(var1);
        return var2 >= 0?(d)this.b.get(var2):null;
    }

    void a(String var1, d var2) {
        this.c.add(var1);
        this.b.add(var2);
        BaseInfo.isStreamAppRuning = true;
    }

    d b(String var1) {
        d var2 = null;
        Iterator var3 = this.b.iterator();

        while(var3.hasNext()) {
            d var4 = (d)var3.next();
            if(TextUtils.equals(var4.obtainAppId(), var1)) {
                var2 = var4;
                break;
            }
        }

        Logger.d("AppCache", "removeWebApp " + var2 + ";mAppIdList=" + this.c);
        this.b.remove(var2);
        this.c.remove(var1);
        if(this.b.size() == 0) {
            BaseInfo.isStreamAppRuning = false;
        }

        return var2;
    }

    protected d a() {
        long var1 = 0L;
        d var3 = null;

        for(int var4 = this.b.size() - 1; var4 >= 0; --var4) {
            d var5 = (d)this.b.get(var4);
            if(var5.d == 3 && var5.Q > var1) {
                var3 = var5;
                var1 = var5.Q;
            }
        }

        return var3;
    }

    protected d a(Activity var1, d var2) {
        if(this.b.contains(var2)) {
            return null;
        } else {
            long var3 = System.currentTimeMillis();
            int var5 = this.b.size();
            d var6 = null;
            if(var5 >= BaseInfo.s_Runing_App_Count_Max) {
                var6 = this.b();
            }

            return var6;
        }
    }

    public d b() {
        d var1 = null;
        long var2 = System.currentTimeMillis();

        for(int var4 = 0; var4 < this.b.size(); ++var4) {
            d var5 = (d)this.b.get(var4);
            if(var5.Q < var2) {
                var1 = var5;
                var2 = var5.Q;
            }
        }

        return var1;
    }

    protected ArrayList<d> c() {
        ArrayList var1 = new ArrayList(this.d());
        int var2 = this.d();

        for(int var3 = 0; var3 < var2; ++var3) {
            d var4 = (d)this.b.get(var3);
            int var5 = 0;

            for(int var6 = 0; var6 < var1.size(); ++var6) {
                d var7 = (d)var1.get(var6);
                if(var4.Q <= var7.Q) {
                    break;
                }

                var5 = var6 + 1;
            }

            var1.add(var5, var4);
        }

        return var1;
    }

    protected int d() {
        return this.b.size();
    }

    void e() {
        this.b.clear();
        this.c.clear();
    }
}
