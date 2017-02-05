//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.aa;

import java.util.HashMap;
import java.util.Map;

public class b {
    private HashMap<String, Map<String, String>> aaaa;

    private b() {
        this.aaaa = new HashMap();
    }

    public static b a() {
        return b.a.a;
    }

    public void a(String var1, Map<String, String> var2) {
        this.aaaa.put(var1, var2);
    }

    public Map<String, String> a(String var1) {
        return (Map)this.aaaa.get(var1);
    }

    private static class a {
        public static final b a = new b();
    }
}
