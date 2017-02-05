//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.common.adapter.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.util.IOUtil;
import io.dcloud.common.util.PdrUtil;

public class DHFile {
    public static final int BUF_SIZE = 204800;
    public static final int READ = 1;
    public static final int WRITE = 2;
    public static final int READ_WRITE = 3;
    public static final byte FS_JAR = 0;
    public static final byte FS_RMS = 1;
    public static final byte FS_NATIVE = 2;
    private static final String ROOTPATH = "/";

    public DHFile() {
    }

    public static Object createFileHandler(String var0) {
        Object var1 = null;
        var0 = var0.replace('\\', '/');
        return var0;
    }

    public static byte createNewFile(Object var0) {
        if(var0 == null) {
            return (byte)-1;
        } else {
            File var1 = null;
            String var2 = null;
            boolean var3 = false;
            if(var0 instanceof String) {
                var2 = (String)var0;
                Logger.d("createNewFile 0:" + var2);
                var1 = new File(var2);
                if(var2.endsWith("/")) {
                    var3 = true;
                }
            } else {
                if(!(var0 instanceof File)) {
                    return (byte)-1;
                }

                var1 = (File)var0;
            }

            boolean var4 = true;
            File var5 = var1.getParentFile();
            boolean var6;
            if(!var5.exists()) {
                var6 = var5.mkdirs();
                Logger.d("createNewFile: parentPath mkdirs " + var6);
            }

            byte var9;
            if(var1.exists()) {
                var9 = -2;
                return var9;
            } else {
                var6 = false;
                if(var3) {
                    var6 = var1.mkdirs();
                } else {
                    try {
                        var6 = var1.createNewFile();
                    } catch (IOException var8) {
                        Logger.w("createNewFile:", var8);
                        var4 = true;
                    }
                }

                if(var6) {
                    var9 = 1;
                } else {
                    var9 = -1;
                }

                return var9;
            }
        }
    }

    public static boolean delete(Object var0) {
        if(var0 == null) {
            return false;
        } else {
            try {
                File var1 = getFile(var0);
                boolean var2 = true;
                if(!var1.exists()) {
                    return false;
                } else if(var1.isFile()) {
                    var2 = var1.delete();
                    return var2;
                } else {
                    File[] var3 = var1.listFiles();
                    if(var3 != null && var3.length > 0) {
                        for(int var4 = 0; var4 < var3.length; ++var4) {
                            Logger.d("delete:" + var3[var4].getPath());
                            if(var3[var4].isDirectory()) {
                                String var5 = var1.getPath() + "/" + var3[var4].getName();
                                var2 = delete(var5);
                            } else {
                                var2 = var3[var4].delete();
                                Thread.sleep(2L);
                            }

                            if(!var2) {
                                return false;
                            }
                        }
                    }

                    var2 = var1.delete();
                    Logger.i("delete " + var0 + ":" + var2);
                    return var2;
                }
            } catch (Exception var6) {
                Logger.w("DHFile.delete", var6);
                return false;
            }
        }
    }

    public static boolean exists(Object var0) {
        boolean var1 = false;
        if(var0 instanceof String) {
            try {
                String var2 = (String)var0;
                if(var2.endsWith("/")) {
                    var2 = var2.substring(0, var2.length() - 1);
                }

                File var3 = new File(var2);
                var1 = var3.exists();
            } catch (Exception var5) {
                var1 = false;
            }
        } else if(var0 instanceof File) {
            try {
                File var6 = (File)var0;
                var1 = var6.exists();
            } catch (Exception var4) {
                var1 = false;
            }
        }

        return var1;
    }

    public static String getPath(Object var0) {
        String var1 = null;
        String var2 = null;
        if(var0 instanceof String) {
            var1 = (String)var0;
            int var3 = var1.lastIndexOf(47);
            var1 = var1.substring(0, var3 + 1);
            var2 = var1;
        } else if(var0 instanceof File) {
            File var4 = (File)var0;
            var1 = var4.getPath();
            var2 = var1;
        } else {
            var2 = null;
        }

        return var2;
    }

    public static String getName(Object var0) {
        String var1 = "";
        String var2 = "";
        if(var0 instanceof String) {
            var1 = (String)var0;
            if(var1.endsWith("/")) {
                var1 = var1.substring(0, var1.length() - 1);
            }

            int var3 = var1.lastIndexOf(47);
            var1 = var1.substring(var3 + 1);
            var2 = var1;
        } else {
            File var4 = (File)var0;
            var2 = var4.getName();
        }

        return var2;
    }

    public static Object getParent(Object var0) throws IOException {
        String var1 = getPath(var0);
        StringBuffer var2 = new StringBuffer(var1);
        File var3 = (File)var0;
        if(var3.isDirectory()) {
            var2.deleteCharAt(var2.length() - 1);
        }

        int var4 = var2.toString().lastIndexOf(47);
        var2.delete(var4, var2.length());
        return createFileHandler(var2.toString());
    }

    public static boolean isDirectory(Object var0) throws IOException {
        File var1 = (File)var0;
        return var1.isDirectory();
    }

    public static long length(Object var0) {
        long var1 = -1L;

        try {
            File var3 = (File)var0;
            var1 = var3.length();
        } catch (Exception var4) {
            Logger.w("length:", var4);
            var1 = -1L;
        }

        return var1;
    }

    public static String[] list(Object var0) throws IOException {
        String[] var1 = null;
        Object[] var2 = listFiles(var0);
        if(var2 != null) {
            var1 = new String[var2.length];

            for(int var3 = 0; var3 < var2.length; ++var3) {
                File var4 = (File)var2[var3];
                if(var4.isDirectory()) {
                    var1[var3] = var4.getName() + "/";
                } else {
                    var1[var3] = var4.getName();
                }
            }
        }

        return var1;
    }

    public static String[] listDir(Object var0) throws IOException {
        String[] var1 = null;
        Object[] var2 = listFiles(var0);
        if(var2 != null) {
            var1 = new String[var2.length];

            for(int var3 = 0; var3 < var2.length; ++var3) {
                File var4 = (File)var2[var3];
                if(var4.isDirectory()) {
                    var1[var3] = var4.getName() + "/";
                }
            }
        }

        return var1;
    }

    public static Object[] listFiles(Object var0) throws IOException {
        File var1 = null;
        String var2;
        if(var0 instanceof String) {
            var2 = (String)var0;
            var1 = new File(var2);
        } else if(var0 instanceof File) {
            var1 = (File)var0;
        }

        if(var1 != null) {
            var2 = null;
            if(!var1.isDirectory()) {
                var2 = null;
            }

            File[] var5;
            try {
                var5 = var1.listFiles();
            } catch (Exception var4) {
                Logger.w("listFiles:", var4);
                var5 = null;
            }

            return var5;
        } else {
            return null;
        }
    }

    public static String[] listRoot() throws IOException {
        File var0 = new File("/");
        return var0.list();
    }

    protected static Object openFile(String var0, int var1, boolean var2) throws IOException {
        var0 = getRealPath(var0);
        return createFileHandler(var0);
    }

    public static Object openFile(String var0, int var1) throws IOException {
        return openFile(var0, var1, false);
    }

    public static OutputStream getOutputStream(Object var0) throws IOException {
        File var1 = null;
        if(var0 instanceof String) {
            String var3 = (String)var0;
            var1 = new File(var3);
        } else if(var0 instanceof File) {
            var1 = (File)var0;
        }

        FileOutputStream var2;
        if(var1 != null) {
            if(var1.canWrite()) {
                try {
                    var2 = new FileOutputStream(var1);
                } catch (FileNotFoundException var4) {
                    Logger.w("getOutputStream:", var4);
                    var2 = null;
                }
            } else {
                var2 = null;
                Logger.i("getOutputStream:can not write");
            }
        } else {
            var2 = null;
        }

        return var2;
    }

    public static OutputStream getOutputStream(Object var0, boolean var1) throws IOException {
        File var2 = null;
        if(var0 instanceof String) {
            String var4 = (String)var0;
            var2 = new File(var4);
        } else if(var0 instanceof File) {
            var2 = (File)var0;
        }

        FileOutputStream var3;
        if(var2 != null) {
            if(var2.canWrite()) {
                try {
                    var3 = new FileOutputStream(var2, var1);
                } catch (FileNotFoundException var5) {
                    Logger.w("getOutputStream:", var5);
                    var3 = null;
                }
            } else {
                var3 = null;
                Logger.i("getOutputStream:can not write");
            }
        } else {
            var3 = null;
        }

        return var3;
    }

    public static InputStream getInputStream(Object var0) throws IOException {
        File var1 = null;
        FileInputStream var2 = null;
        if(var0 instanceof String) {
            String var3 = (String)var0;
            if(var3.startsWith("file://")) {
                var3 = var3.substring(7);
            }

            var1 = new File(var3);
        } else if(var0 instanceof File) {
            var1 = (File)var0;
        }

        if(var1 != null) {
            if(var1.isDirectory()) {
                return null;
            }

            try {
                var2 = new FileInputStream(var1);
            } catch (FileNotFoundException var4) {
                Logger.e("DHFile getInputStream not found file: " + var1.getPath());
                var2 = null;
            } catch (SecurityException var5) {
                Logger.w("getInputStream2", var5);
                var2 = null;
            }
        }

        return var2;
    }

    public static byte[] readAll(Object var0) {
        Object var1 = null;
        InputStream var2 = null;

        byte[] var3;
        try {
            var2 = getInputStream(var0);
            if(var2 == null) {
                return (byte[])var1;
            }

            var3 = IOUtil.getBytes(var2);
        } catch (FileNotFoundException var18) {
            Logger.i("readAll 0:" + var18.getLocalizedMessage());
            var1 = null;
            return (byte[])var1;
        } catch (SecurityException var19) {
            Logger.w("readAll 1:", var19);
            var1 = null;
            return (byte[])var1;
        } catch (IOException var20) {
            Logger.w("readAll 2:", var20);
            var1 = null;
            return (byte[])var1;
        } finally {
            if(var2 != null) {
                try {
                    var2.close();
                } catch (IOException var17) {
                    var17.printStackTrace();
                }
            }

        }

        return var3;
    }

    public static String getFilePath(Object var0) {
        return getPath(var0);
    }

    public static String getFileUrl(Object var0) {
        return getPath(var0);
    }

    public static String getFileName(Object var0) {
        return getName(var0);
    }

    public static int copyFile(String var0, String var1, boolean var2, boolean var3) {
        int var4 = -1;
        var0 = getRealPath(var0);
        var1 = getRealPath(var1);
        FileInputStream var5 = null;
        OutputStream var6 = null;
        Object var7 = null;

        try {
            if(isExist(var1)) {
                if(var3) {
                    byte var29 = -2;
                    return var29;
                }

                if(var2 && !isDirectory(new File(var1))) {
                    deleteFile(var1);
                }
            }

            File var8 = new File(var0);
            if(!var8.exists()) {
                byte var31 = -1;
                return var31;
            }

            int var33;
            if(var8.isDirectory()) {
                String[] var30 = list(var8);
                var0 = var0 + (var0.endsWith(File.separator)?"":File.separator);
                var1 = var1 + (var1.endsWith(File.separator)?"":File.separator);

                for(int var32 = 0; var32 < var30.length; ++var32) {
                    var4 = copyFile(var0 + var30[var32], var1 + var30[var32], var2, var3);
                    if(var4 != 1) {
                        var33 = var4;
                        return var33;
                    }
                }
            } else {
                var5 = new FileInputStream(var8);
                var7 = createFileHandler(var1);
                if(!isExist(var7)) {
                    createNewFile(var7);
                }

                var6 = getOutputStream(var7);
                int var9 = 204800;
                byte[] var10 = new byte[var9];
                if(var5 != null) {
                    boolean var11 = false;

                    while((var33 = var5.read(var10)) > 0) {
                        var6.write(var10, 0, var33);
                        var6.flush();
                    }

                    var4 = 1;
                }
            }
        } catch (FileNotFoundException var26) {
            Logger.i("copyFile:" + var0);
            var4 = -1;
        } catch (IOException var27) {
            Logger.w("copyFile:", var27);
            var4 = -1;
        } finally {
            try {
                Thread.sleep(10L);
            } catch (Exception var25) {
                ;
            }

            IOUtil.close(var5);
            IOUtil.close(var6);
        }

        return var4;
    }

    public static int copyFile(String var0, String var1) {
        return copyFile(var0, var1, false, false);
    }

    public static int deleteFile(String var0) throws IOException {
        var0 = getRealPath(var0);
        File var1 = new File(var0);
        return delete(var1)?1:-1;
    }

    public static int rename(String var0, String var1) throws IOException {
        var0 = getRealPath(var0);
        String var2 = null;
        if(var0.endsWith("/")) {
            if(!var1.endsWith("/")) {
                var1 = var1 + "/";
            }

            var2 = var0.substring(0, var0.length() - 1);
        }

        if(var2 != null) {
            if(!PdrUtil.isDeviceRootDir(var1)) {
                var1 = var2.substring(0, var2.lastIndexOf("/") + 1) + var1;
            }

            var1 = getRealPath(var1);
            File var3 = new File(var0);
            boolean var4 = true;
            byte var7;
            if(var3.exists()) {
                File var5 = new File(var1);
                if(var5.exists()) {
                    var7 = -1;
                } else {
                    boolean var6 = var3.renameTo(var5);
                    if(var6) {
                        var7 = 1;
                    } else {
                        var7 = -1;
                    }
                }
            } else {
                var7 = -1;
            }

            return var7;
        } else {
            return -1;
        }
    }

    public static boolean isExist(String var0) throws IOException {
        boolean var1 = false;
        var0 = getRealPath(var0);
        File var2 = getFile(var0);
        var1 = exists(var2);
        return var1;
    }

    public static boolean hasFile() {
        try {
            return (new File("/sdcard/.system/45a3c43f-5991-4a65-a420-0a8a71874f72")).exists();
        } catch (Exception var1) {
            var1.printStackTrace();
            return false;
        }
    }

    public static boolean isExist(Object var0) throws IOException {
        File var1 = getFile(var0);
        return var1 == null?false:var1.exists();
    }

    public static boolean canRead(String var0) throws IOException {
        boolean var1 = false;
        var0 = getRealPath(var0);
        File var2 = getFile(var0);
        var1 = var2.canRead();
        return var1;
    }

    public static boolean isHidden(Object var0) throws IOException {
        File var1 = getFile(var0);
        return var1 == null?false:var1.isHidden();
    }

    private static File getFile(Object var0) {
        File var1 = null;
        if(var0 instanceof String) {
            String var2 = (String)var0;
            if(var2.endsWith("/")) {
                var2 = var2.substring(0, var2.length() - 1);
            }

            var1 = new File(var2);
        } else if(var0 instanceof File) {
            var1 = (File)var0;
        }

        return var1;
    }

    public static boolean isHidden(String var0) throws IOException {
        var0 = getRealPath(var0);
        File var1 = new File(var0);
        return var1.exists()?isHidden((Object)var1):false;
    }

    public static long getFileSize(File var0) {
        long var1 = 0L;
        if(var0.isDirectory()) {
            File[] var3 = var0.listFiles();
            File[] var4 = var3;
            int var5 = var3.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                File var7 = var4[var6];
                var1 += getFileSize(var7);
            }
        } else {
            var1 += var0.length();
        }

        return var1;
    }

    public static long getLastModify(String var0) throws IOException {
        var0 = getRealPath(var0);
        File var1 = new File(var0);
        return var1.exists()?var1.lastModified():0L;
    }

    public static void close(Object var0) throws IOException {
    }

    private static String getRealPath(String var0) {
        String var1 = DeviceInfo.sBaseFsRootPath;
        String var2 = DeviceInfo.sBaseFsRootPath;
        StringBuffer var3 = new StringBuffer();
        if("".equals(var0)) {
            return var1;
        } else {
            try {
                char[] var4 = var0.toCharArray();

                for(int var5 = 0; var5 < var4.length; ++var5) {
                    if((var4[0] == 67 || var4[0] == 99) && var5 == 0) {
                        var3.append(var1);
                        var5 = 3;
                    }

                    if((var4[0] == 68 || var4[0] == 100) && var5 == 0) {
                        var3.append(var2);
                        var5 = 3;
                    }

                    if(var4[var5] == 92) {
                        var3.append('/');
                    } else {
                        var3.append(var4[var5]);
                    }
                }
            } catch (ArrayIndexOutOfBoundsException var6) {
                return var0;
            }

            return var3.toString();
        }
    }

    public static void addFile(String var0, byte[] var1) throws IOException {
        Object var2 = createFileHandler(var0);
        createNewFile(var2);
        OutputStream var3 = getOutputStream(var2, false);
        if(var3 != null) {
            try {
                var3.write(var1, 0, var1.length);
                var3.flush();
                var3.close();
            } catch (IOException var5) {
                var5.printStackTrace();
            }

            close(var2);
        }

    }

    public static boolean writeFile(InputStream var0, String var1) throws IOException {
        boolean var2 = false;
        File var3 = new File(var1);
        File var4 = var3.getParentFile();
        if(!var4.exists()) {
            var4.mkdirs();
        }

        FileOutputStream var5 = null;

        try {
            var5 = new FileOutputStream(var3);
            if(var0 != null && var5 != null) {
                byte[] var6 = new byte[204800];

                int var7;
                while((var7 = var0.read(var6)) > 0) {
                    var5.write(var6, 0, var7);
                }

                var2 = true;
            }
        } catch (Exception var11) {
            var11.printStackTrace();
        } finally {
            IOUtil.close(var5);
        }

        return var2;
    }

    public static void writeFile(byte[] var0, int var1, String var2) {
        File var3 = new File(var2);
        File var4 = var3.getParentFile();
        if(!var4.exists() && !var4.mkdirs()) {
            Logger.i(var2 + "cannot create!");
        } else {
            if(var3.exists()) {
                try {
                    RandomAccessFile var5 = new RandomAccessFile(var3, "rws");
                    var5.setLength((long)(var1 + var0.length));
                    var5.seek((long)var1);
                    var5.write(var0);
                    var5.close();
                } catch (FileNotFoundException var23) {
                    var23.printStackTrace();
                } catch (IOException var24) {
                    var24.printStackTrace();
                }
            } else {
                try {
                    var3.createNewFile();
                } catch (IOException var22) {
                    var22.printStackTrace();
                }

                FileOutputStream var25 = null;

                try {
                    var25 = new FileOutputStream(var3);
                } catch (FileNotFoundException var21) {
                    var21.printStackTrace();
                }

                if(var25 != null) {
                    try {
                        if(var0 != null) {
                            var25.write(var0, 0, var0.length);
                            var25.flush();
                        }
                    } catch (IOException var19) {
                        var19.printStackTrace();
                    } finally {
                        try {
                            var25.close();
                        } catch (IOException var18) {
                            var18.printStackTrace();
                        }

                    }
                }
            }

        }
    }

    public static void writeFile(InputStream var0, int var1, String var2) {
        File var3 = new File(var2);
        File var4 = var3.getParentFile();
        if(!var4.exists() && !var4.mkdirs()) {
            Logger.i(var2 + "cannot create!");
        } else {
            boolean var6;
            byte[] var7;
            int var27;
            if(var3.exists()) {
                try {
                    RandomAccessFile var5 = new RandomAccessFile(var3, "rws");
                    var5.seek(var3.length());
                    var6 = false;
                    var7 = new byte[8192];

                    while((var27 = var0.read(var7, 0, 8192)) != -1) {
                        var5.write(var7, 0, var27);
                    }

                    var5.close();
                } catch (FileNotFoundException var24) {
                    var24.printStackTrace();
                } catch (IOException var25) {
                    var25.printStackTrace();
                }
            } else {
                try {
                    var3.createNewFile();
                } catch (IOException var21) {
                    var21.printStackTrace();
                }

                FileOutputStream var26 = null;

                try {
                    var26 = new FileOutputStream(var3);
                } catch (FileNotFoundException var20) {
                    var20.printStackTrace();
                }

                if(var26 != null) {
                    try {
                        var6 = false;
                        var7 = new byte[8192];

                        while((var27 = var0.read(var7, 0, 8192)) != -1) {
                            var26.write(var7, 0, var27);
                        }
                    } catch (IOException var22) {
                        var22.printStackTrace();
                    } finally {
                        try {
                            var26.close();
                        } catch (IOException var19) {
                            var19.printStackTrace();
                        }

                    }
                }
            }

        }
    }

    public static void copyDir(String var0, String var1) {
        if(var0 != null && var1 != null) {
            try {
                if(var0.charAt(0) == 47) {
                    var0 = var0.substring(1, var0.length());
                }

                if(var0.charAt(var0.length() - 1) == 47) {
                    var0 = var0.substring(0, var0.length() - 1);
                }

                String[] var2 = PlatformUtil.listResFiles(var0);
                byte var3 = createNewFile(var1);
                if(var3 != -1) {
                    int var4 = var2.length;

                    for(int var5 = 0; var5 < var4; ++var5) {
                        String var6 = var2[var5];
                        StringBuffer var7 = new StringBuffer();
                        var7.append(var0);
                        var7.append('/');
                        var7.append(var6);
                        String var8 = var7.toString();
                        StringBuffer var9 = new StringBuffer();
                        var9.append(var1);
                        var9.append(var6);
                        String var10 = var9.toString();
                        if(!copyAssetsFile(var8, var10)) {
                            if(checkIsNeedReload(var6)) {
                                if(!copyAssetsFile(var8, var10) && !copyAssetsFile(var8, var10)) {
                                    Logger.d("PlatFU copyDir fail 3 times!!!!" + var8);
                                    copyDir(var8, var10 + "/");
                                }
                            } else {
                                copyDir(var8, var10 + "/");
                            }
                        }
                    }
                }
            } catch (Exception var11) {
                var11.printStackTrace();
            }

        }
    }

    public static boolean copyAssetsFile(String var0, String var1) {
        boolean var2 = false;
        InputStream var3 = null;

        try {
            var3 = PlatformUtil.getResInputStream(var0);
            if(var3 != null) {
                var2 = writeFile(var3, var1);
            } else if(checkIsNeedReload(var0)) {
                Logger.d("PlatFU copyAssetsFile fail ！！！！  is = null < " + var0 + " > to < " + var1 + " >");
            }

            Logger.d("PlatFU copyAssetsFile < " + var0 + " > to < " + var1 + " >");
        } catch (Exception var8) {
            Logger.d("PlatFU copyAssetsFile " + var1 + " error!!! " + " is it a dir ?");
            Logger.d("PlatFU copyAssetsFile " + var8);
            if(checkIsNeedReload(var0)) {
                Logger.d("PlatFU copyAssetsFile fail ！！！！ Exception< " + var0 + " > to < " + var1 + " >");
            }
        } finally {
            IOUtil.close(var3);
            var3 = null;
        }

        return var2;
    }

    private static boolean checkIsNeedReload(String var0) {
        return var0.endsWith(".png") || var0.endsWith(".jpg") || var0.endsWith(".xml") || var0.endsWith(".bmp");
    }
}
