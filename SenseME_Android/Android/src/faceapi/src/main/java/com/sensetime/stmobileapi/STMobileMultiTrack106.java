package com.sensetime.stmobileapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.sensetime.stmobileapi.STMobileApiBridge.ResultCode;
import com.sensetime.stmobileapi.STMobileApiBridge.st_mobile_106_t;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class STMobileMultiTrack106 {
	private Pointer trackHandle;
    private static final int FACE_KEY_POINTS_COUNT = 106;
    static boolean DEBUG = true;// false;
    
    private int ST_MOBILE_TRACKING_DEFAULT_CONFIG = 0x00000000;
    private int ST_MOBILE_TRACKING_SINGLE_THREAD = 0x00000001;
    
	private Context mContext;
	private static final String BEAUTIFY_MODEL_NAME = "track.tar";
      
    PointerByReference ptrToArray = new PointerByReference();
    IntByReference ptrToSize = new IntByReference();
    
    /**
     * 
     * Note
        track only one face锛� 
        frist:trackHandle = STMobileApiBridge.FACESDK_INSTANCE.st_mobile_tracker_106_create(modulePath, handlerPointer);
        second: setMaxDetectableFaces(1)鍙傛暟璁句负1
     *  track澶氬紶浜鸿劯锛�	
     *  trackHandle = STMobileApiBridge.FACESDK_INSTANCE.st_mobile_tracker_106_create(modulePath, handlerPointer);
        second:setMaxDetectableFaces(num)鍙傛暟璁句负-1
     */
     
    public STMobileMultiTrack106(Context context) {
        PointerByReference handlerPointer = new PointerByReference();
		mContext = context;
		synchronized(this.getClass())
		{
		   copyModelIfNeed(BEAUTIFY_MODEL_NAME);
		}
		String modulePath = getModelPath(BEAUTIFY_MODEL_NAME);
    	int rst = STMobileApiBridge.FACESDK_INSTANCE.st_mobile_tracker_106_create(modulePath, ST_MOBILE_TRACKING_DEFAULT_CONFIG, handlerPointer);
    	trackHandle = handlerPointer.getValue();
    }
    
	private void copyModelIfNeed(String modelName) {
		String path = getModelPath(modelName);
		if (path != null) {
			File modelFile = new File(path);
			if (!modelFile.exists()) {
				//濡傛灉妯″瀷鏂囦欢涓嶅瓨鍦ㄦ垨鑰呭綋鍓嶆ā鍨嬫枃浠剁殑鐗堟湰璺焥dcard涓殑鐗堟湰涓嶄竴鏍�
				try {
					if (modelFile.exists())
						modelFile.delete();
					modelFile.createNewFile();
					InputStream in = mContext.getApplicationContext().getAssets().open(modelName);
					if(in == null)
					{
						Log.e("MultiTrack106", "the src module is not existed");
					}
					OutputStream out = new FileOutputStream(modelFile);
					byte[] buffer = new byte[4096];
					int n;
					while ((n = in.read(buffer)) > 0) {
						out.write(buffer, 0, n);
					}
					in.close();
					out.close();
				} catch (IOException e) {
					modelFile.delete();
				}
			}
		}
	}
	
	protected String getModelPath(String modelName) {
		String path = null;
		File dataDir = mContext.getApplicationContext().getExternalFilesDir(null);
		if (dataDir != null) {
			path = dataDir.getAbsolutePath() + File.separator + modelName;
		}
		return path;
	}
    
    public int setMaxDetectableFaces(int max)
    {
    	int rst = STMobileApiBridge.FACESDK_INSTANCE.st_mobile_tracker_106_set_facelimit(trackHandle,max);
        return rst;
    }
    
	public void destory()
	{
    	long start_destroy = System.currentTimeMillis();
    	if(trackHandle != null) {
    		STMobileApiBridge.FACESDK_INSTANCE.st_mobile_tracker_106_destroy(trackHandle);
    		trackHandle = null;
    	}
        long end_destroy = System.currentTimeMillis();
        Log.i("track106", "destroy cost "+(end_destroy - start_destroy)+" ms");
	}
    /**
     * Given the Image by Bitmap to track face
     * @param image Input image by Bitmap
     * @param orientation Image orientation
     * @return CvFace array, each one in array is Detected by SDK native API
     */
    public STMobile106[] track(Bitmap image, int orientation) {
    	if(DEBUG)System.out.println("SampleLiveness-------->CvFaceMultiTrack--------->track1");
    	
        int[] colorImage = STUtils.getBGRAImageByte(image);
        return track(colorImage, STImageFormat.CV_PIX_FMT_BGRA8888,image.getWidth(), image.getHeight(), image.getWidth(), orientation);
    }

    /**
     * Given the Image by Byte Array to track face
     * @param colorImage Input image by int
     * @param cvImageFormat Image format
     * @param imageWidth Image width
     * @param imageHeight Image height
     * @param imageStride Image stride
     * @param orientation Image orientation
     * @return CvFace array, each one in array is Detected by SDK native API
     */
    public STMobile106[] track(int[] colorImage,int cvImageFormat, int imageWidth, int imageHeight, int imageStride, int orientation) {
    	if(DEBUG)System.out.println("SampleLiveness-------->CvFaceMultiTrack--------->track2");
    	
        long startTime = System.currentTimeMillis();
        /*
        int rst = STMobileApiBridge.FACESDK_INSTANCE.cv_face_track_106(trackHandle, colorImage, cvImageFormat,imageWidth,
                imageHeight, imageStride, orientation, ptrToArray, ptrToSize);
        */
        int rst = STMobileApiBridge.FACESDK_INSTANCE.st_mobile_tracker_106_track(trackHandle, colorImage, cvImageFormat,imageWidth,
                imageHeight, imageStride, orientation, ptrToArray, ptrToSize);
        long endTime = System.currentTimeMillis();
        
        if(DEBUG)Log.d("Test", "multi track time: "+(endTime-startTime)+"ms");
        
        if (rst != ResultCode.CV_OK.getResultCode()) {
            throw new RuntimeException("Calling cv_face_multi_track() method failed! ResultCode=" + rst);
        }

        if (ptrToSize.getValue() == 0) {
        	if(DEBUG)Log.d("Test", "ptrToSize.getValue() == 0");
            return new STMobile106[0];
        }

        st_mobile_106_t arrayRef = new st_mobile_106_t(ptrToArray.getValue());
        arrayRef.read();
        st_mobile_106_t[] array = st_mobile_106_t.arrayCopy((st_mobile_106_t[]) arrayRef.toArray(ptrToSize.getValue()));
        STMobileApiBridge.FACESDK_INSTANCE.st_mobile_tracker_106_release_result(ptrToArray.getValue(), ptrToSize.getValue());
        
        STMobile106[] ret = new STMobile106[array.length]; 
        for (int i = 0; i < array.length; i++) {
        	ret[i] = new STMobile106(array[i]);
        }
        
        if(DEBUG)Log.d("Test", "track : "+ ret);
        
        return ret;
    }
    
    /**
     * Given the Image by Byte to track face
     * @param image Input image by byte
     * @param orientation Image orientation
     * @param width Image width
     * @param height Image height
     * @return CvFace array, each one in array is Detected by SDK native API
     */
    public STMobile106[] track(byte[] image, int orientation,int width,int height) {
    	if(DEBUG){
    		System.out.println("SampleLiveness-------->CvFaceMultiTrack--------->track3");
    	}
    	
        return track(image, STImageFormat.CV_PIX_FMT_NV21,width, height, width, orientation);
    }

    /**
     * Given the Image by Byte Array to track face
     * @param colorImage Input image by byte
     * @param cvImageFormat Image format
     * @param imageWidth Image width
     * @param imageHeight Image height
     * @param imageStride Image stride
     * @param orientation Image orientation
     * @return CvFace array, each one in array is Detected by SDK native API
     */
    public STMobile106[] track(byte[] colorImage,int cvImageFormat, int imageWidth, int imageHeight, int imageStride, int orientation) {
    	if(DEBUG){
    		System.out.println("SampleLiveness-------->CvFaceMultiTrack--------->track4");
    	}
    	
        long startTime = System.currentTimeMillis();
        /*
           int rst = STMobileApiBridge.FACESDK_INSTANCE.cv_face_track_106(trackHandle, colorImage, cvImageFormat,imageWidth,
                imageHeight, imageStride, orientation, ptrToArray, ptrToSize);
         */
        int rst = STMobileApiBridge.FACESDK_INSTANCE.st_mobile_tracker_106_track(trackHandle, colorImage, cvImageFormat,imageWidth,
                imageHeight, imageStride, orientation, ptrToArray, ptrToSize);
        long endTime = System.currentTimeMillis();
        
        if(DEBUG)Log.d("Test", "multi track time: "+(endTime-startTime)+"ms");
        
        if (rst != ResultCode.CV_OK.getResultCode()) {
            throw new RuntimeException("Calling cv_face_multi_track() method failed! ResultCode=" + rst);
        }

        if (ptrToSize.getValue() == 0) {
            return new STMobile106[0];
        }

        st_mobile_106_t arrayRef = new st_mobile_106_t(ptrToArray.getValue());
        arrayRef.read();
        st_mobile_106_t[] array = st_mobile_106_t.arrayCopy((st_mobile_106_t[]) arrayRef.toArray(ptrToSize.getValue()));
        STMobileApiBridge.FACESDK_INSTANCE.st_mobile_tracker_106_release_result(ptrToArray.getValue(), ptrToSize.getValue());
        
        STMobile106[] ret = new STMobile106[array.length]; 
        for (int i = 0; i < array.length; i++) {
        	ret[i] = new STMobile106(array[i]);
        }
        
        if(DEBUG)Log.d("Test", "track : "+ ret);
        
        return ret;
    }
}
