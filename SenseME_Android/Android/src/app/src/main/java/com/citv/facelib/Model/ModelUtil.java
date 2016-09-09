package com.citv.facelib.Model;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by kay on 2016/7/22.<br/>
 * 3D模型工具
 * @author kay
 */
public class ModelUtil {

    //模型存储路径
    public static String MODEL_PATH = Environment.getExternalStorageDirectory() + "/face_model";

    /**
     * 判断路径是否存在
     * @return {boolean}
     */
    public static boolean isModelDirExist() {
        File dir = new File(MODEL_PATH);
        return dir.exists();
    }

    /**
     * 从assets文件夹下将模型复制到手机存储
     * @param {Context} context 上下文
     * @param {String} oldPath assets目录
     * @param {String} newPath 手机存储目录
     * @return 无返回值
     * @throws Exception 读写文件异常
     */
    public static void copyFilesFassets(Context context, String oldPath, String newPath) {
        try {
            String fileNames[] = context.getResources().getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {//如果是目录
                File file = new File(newPath);
                file.mkdirs();//如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesFassets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                }
            } else {//如果是文件
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {//循环从输入流读取 buffer字节
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                }
                fos.flush();//刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
