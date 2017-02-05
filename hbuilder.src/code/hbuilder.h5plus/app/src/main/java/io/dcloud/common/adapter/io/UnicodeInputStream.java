//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.common.adapter.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class UnicodeInputStream extends InputStream {
    PushbackInputStream internalIn;
    boolean isInited = false;
    String defaultEnc;
    String encoding;
    private static final int BOM_SIZE = 4;

    public UnicodeInputStream(InputStream var1, String var2) {
        this.internalIn = new PushbackInputStream(var1, 4);
        this.defaultEnc = var2;
    }

    public String getDefaultEncoding() {
        return this.defaultEnc;
    }

    public String getEncoding() {
        if(!this.isInited) {
            try {
                this.init();
            } catch (IOException var3) {
                IllegalStateException var2 = new IllegalStateException("Init method failed.");
                var2.initCause(var2);
                throw var2;
            }
        }

        return this.encoding;
    }

    protected void init() throws IOException {
        if(!this.isInited) {
            byte[] var1 = new byte[4];
            int var2 = this.internalIn.read(var1, 0, var1.length);
            int var3;
            if(var1[0] == 0 && var1[1] == 0 && var1[2] == -2 && var1[3] == -1) {
                this.encoding = "UTF-32BE";
                var3 = var2 - 4;
            } else if(var1[0] == -1 && var1[1] == -2 && var1[2] == 0 && var1[3] == 0) {
                this.encoding = "UTF-32LE";
                var3 = var2 - 4;
            } else if(var1[0] == -17 && var1[1] == -69 && var1[2] == -65) {
                this.encoding = "UTF-8";
                var3 = var2 - 3;
            } else if(var1[0] == -2 && var1[1] == -1) {
                this.encoding = "UTF-16BE";
                var3 = var2 - 2;
            } else if(var1[0] == -1 && var1[1] == -2) {
                this.encoding = "UTF-16LE";
                var3 = var2 - 2;
            } else {
                this.encoding = this.defaultEnc;
                var3 = var2;
            }

            if(var3 > 0) {
                this.internalIn.unread(var1, var2 - var3, var3);
            }

            this.isInited = true;
        }
    }

    public void close() throws IOException {
        this.isInited = true;
        this.internalIn.close();
    }

    public int read() throws IOException {
        this.init();
        this.isInited = true;
        return this.internalIn.read();
    }
}
