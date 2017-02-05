//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.common.a;

import java.util.ArrayList;
import java.util.Iterator;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IMgr.MgrType;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.MessageHandler.IMessages;

class c {
    AbsMgr a = null;

    c(AbsMgr var1) {
        this.a = var1;
    }

    private ArrayList<c.a> b(String var1) {
        ArrayList var2 = new ArrayList(1);
        if(var1.startsWith("snc:")) {
            var1 = var1.substring("snc:".length());
        }

        var1 = var1.trim();
        int var3 = 0;
        int var4 = var1.length();
        int var5 = 0;
        ArrayList var6 = new ArrayList();

        while(true) {
            char var7;
            do {
                if(var3 >= var4) {
                    int var10 = var6.size();
                    int var11 = 0;

                    while(var11 < var10) {
                        c.a var9 = new c.a();
                        var9.a = (String)var6.get(var11);
                        var9.b = (String)var6.get(var11 + 1);
                        var11 += 2;
                        var2.add(var9);
                    }

                    return var2;
                }

                var7 = var1.charAt(var3);
                ++var3;
            } while(var3 != var4 && (!this.b(var7) || var6.size() % 2 != 0) && !this.a(var7));

            String var8 = var1.substring(var5, var3).trim();
            if(!"".equals(var8)) {
                var5 = var3;
                var6.add(var8);
            }
        }
    }

    void a(final String var1) {
        MessageHandler.sendMessage(new IMessages() {
            public void execute(Object var1x) {
                ArrayList var2 = c.this.b(var1);
                Iterator var3 = var2.iterator();

                while(var3.hasNext()) {
                    c.a var4 = (c.a)var3.next();
                    if("update".equals(var4.a)) {
                        String var5 = var4.b;
                        if("all".equals(var5)) {
                            c.this.a.processEvent(MgrType.WindowMgr, 13, (Object)null);
                        } else if("current".equals(var5)) {
                            c.this.a.processEvent(MgrType.WindowMgr, 12, (Object)null);
                        } else {
                            c.this.a.processEvent(MgrType.WindowMgr, 14, var5);
                        }
                    }
                }

            }
        }, (Object)null);
    }

    boolean a(char var1) {
        return var1 == 13 || var1 == 10;
    }

    boolean b(char var1) {
        return var1 == 9 || var1 == 11 || var1 == 12 || var1 == 32 || var1 == 160 || var1 == 12288;
    }

    class a {
        String a;
        String b;

        a() {
        }
    }
}
