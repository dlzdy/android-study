package com.sensetime.stmobileapi;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public interface STMobileApiBridge extends Library {
    class st_rect_t extends Structure {
    	
        public static class ByValue extends st_rect_t implements Structure.ByValue {
        }

        public int left;
        public int top;
        public int right;
        public int bottom;

        @Override
        protected List getFieldOrder() {
            return Arrays.asList(new String[]{"left", "top", "right", "bottom"});
        }

        @Override
        public st_rect_t clone() {
        	st_rect_t copy = new st_rect_t();
            copy.left = this.left;
            copy.top = this.top;
            copy.right = this.right;
            copy.bottom = this.bottom;
            return copy;
        }

        /**
         * The jna.Structure class is passed on by reference by default,
         * however, in some cases, we need it by value.
         */
        public st_rect_t.ByValue copyToValue() {
        	st_rect_t.ByValue retObj = new st_rect_t.ByValue();
            retObj.left = this.left;
            retObj.top = this.top;
            retObj.right = this.right;
            retObj.bottom = this.bottom;
            return retObj;
        }
    }
    
    class st_mobile_106_t extends Structure {
    	public st_rect_t rect;
    	public float score;
    	public float[] points_array = new float[212];
    	public float yaw;
    	public float pitch;
    	public float roll;
    	public int eye_dist;
    	public int ID;

        public st_mobile_106_t() {
            super();
        }

        public st_mobile_106_t(Pointer p) {
            super(p);
        }

        @Override
        protected List getFieldOrder() {
            return Arrays.asList(new String[]{"rect", "score", "points_array", 
            		"yaw", "pitch", "roll", "eye_dist", "ID"});
        }

        @Override
        public st_mobile_106_t clone() {
        	st_mobile_106_t copy = new st_mobile_106_t();
            copy.rect = this.rect.clone();
            copy.score = this.score;
            copy.points_array = this.points_array;
            copy.yaw = this.yaw;
            copy.pitch = this.pitch;
            copy.roll = this.roll;
            copy.eye_dist = this.eye_dist;
            copy.ID = this.ID;
            return copy;
        }

        public static st_mobile_106_t[] arrayCopy(st_mobile_106_t[] origin) {
        	st_mobile_106_t[] copy = new st_mobile_106_t[origin.length];
            for (int i = 0; i < origin.length; ++i) {
                copy[i] = origin[i].clone();
            }
            return copy;
        }
    }

    /**
     * face��Ϣ��face�ϵ���ض���

     typedef struct st_mobile_face_action_t {
         struct st_mobile_106_t face;          /// ������Ϣ���������Ρ�106�㡢pose��Ϣ��//
         unsigned int face_action;             /// ��������
     } st_mobile_face_action_t;

     * */

    class st_mobile_face_action_t extends Structure {
        public st_mobile_106_t face;
        public int face_action;

        public st_mobile_face_action_t() {
            super();
        }

        public st_mobile_face_action_t(Pointer p) {
            super(p);
        }

        @Override
        protected List getFieldOrder() {
            return Arrays.asList(new String[] {"face", "face_action"});
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            st_mobile_face_action_t copy = new st_mobile_face_action_t();
            copy.face = this.face;
            copy.face_action = this.face_action;

            return copy;
        }

        public static st_mobile_face_action_t[] arrayCopy(st_mobile_face_action_t[] origin) {
            st_mobile_face_action_t[] copy = new st_mobile_face_action_t[origin.length];
            for(int i=0; i<origin.length; i++) {
                try {
                    copy[i] = (st_mobile_face_action_t) origin[i].clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
            return copy;
        }
    }
    
    enum ResultCode {
        ST_OK(0),
        ST_E_INVALIDARG(-1),
        ST_E_HANDLE(-2),
        ST_E_OUTOFMEMORY(-3),
        ST_E_FAIL(-4),
        ST_E_DELNOTFOUND(-5),
    	ST_E_INVALID_PIXEL_FORMAT(-6),	///< ��֧�ֵ�ͼ���ʽ
    	ST_E_FILE_NOT_FOUND(-10),   ///< ģ���ļ�������
    	ST_E_INVALID_FILE_FORMAT(-11),	///< ģ�͸�ʽ����ȷ�����¼���ʧ��
    	ST_E_INVALID_APPID(-12),		///< ��������
    	ST_E_INVALID_AUTH(-13),		///< ���ܹ����ܲ�֧��
    	ST_E_AUTH_EXPIRE(-14),		///< SDK����
    	ST_E_FILE_EXPIRE(-15),		///< ģ���ļ�����
    	ST_E_DONGLE_EXPIRE(-16),	///< ���ܹ�����
    	ST_E_ONLINE_AUTH_FAIL(-17),		///< ������֤ʧ��
    	ST_E_ONLINE_AUTH_TIMEOUT(-18);	

        private final int resultCode;

        ResultCode(int resultCode) {
            this.resultCode = resultCode;
        }

        public int getResultCode() {
            return resultCode;
        }
    }

    /**
     * The instance of stmobile DLL
     */
    STMobileApiBridge FACESDK_INSTANCE = (STMobileApiBridge) Native.loadLibrary("st_mobile", STMobileApiBridge.class);

	/// @brief ������Ȩ�ļ����ɼ�����, ��ʹ���µ�license�ļ�ʱʹ��
  /// @param[in] product_name ��Ʒ����
  /// @param[in] license_path license�ļ�·��
  /// @param[out] active_code ���ص�ǰ�豸�ļ����룬���û������ڴ棬���������129���ֽڣ��������1024�ֽ�
  /// @param[in��out] active_code_len  ����Ϊactive_code���ڴ��С, ���ص�ǰ�豸�ļ������ֽڳ���
  /// @return ��������ST_OK�����򷵻ش�������
   int st_mobile_generate_activecode(String license_path, Pointer activation_code, IntByReference activation_code_len);

   /// @brief ��鼤����, ���������нӿ�֮ǰ����
   /// @param[in] product_name ��Ʒ����
   /// @param[in] license_path license�ļ�·��
   /// @param[in] active_path ��ǰ�豸�ļ�����
   /// @return ��������ST_OK�����򷵻ش�������
   int st_mobile_check_activecode(String license_path, String activation_code);

    //cv_mobile_face_106
  /// @brief ����ʵʱ����106�ؼ�����پ��
  /// @param[in] model_path ģ���ļ��ľ���·�������·��������ָ��ģ�Ϳ�ΪNULL; ģ���а���detect+align+poseģ��
  /// @param[in] config ����ѡ�� Ĭ��ʹ��˫�̸߳��٣���ѡ��ʹ�õ��̣߳�ʵʱ��ƵԤ������ʹ��˫�̣߳�ͼƬ����Ƶ������ʹ�õ��߳�
  /// @parma[out] handle �������پ����ʧ�ܷ���NULL
  /// @return �ɹ�����CV_OK, ʧ�ܷ�������������Ϣ
    int st_mobile_tracker_106_create(String model_path, int config, PointerByReference handle);
    
	 int st_mobile_tracker_106_set_smooth_threshold(Pointer handle, float threshold);
  /// @brief ���ü�⵽�����������ĿN������track�Ѽ�⵽��N������ֱ��������С��N�ټ�����detect.
  /// @param[in] handle �ѳ�ʼ���Ĺؼ�����پ��
  /// @param[in] max_facecount ����Ϊ1���ǵ������٣���Ч��ΧΪ[1, 32]
  /// @return �ɹ�����CV_OK, �����򷵻ش�����
    int st_mobile_tracker_106_set_facelimit(Pointer handle,int max_facecount);
   
  /// @brief ����ʵʱ����106�ؼ������
  /// @param [in] handle �ѳ�ʼ����ʵʱĿ������106�ؼ�����پ��
  /// @return �ɹ�����CV_OK, �����򷵻ش�����
    int st_mobile_tracker_106_reset(Pointer handle);
    
  /// @brief ����trackerÿ����֡����һ��detect.
  /// @param[in] handle �ѳ�ʼ���Ĺؼ�����پ��
  /// @param[in] val  ��Ч��Χ[1, -)
  /// @return �ɹ�����CV_OK, �����򷵻ش�����
    int st_mobile_tracker_106_set_detect_interval(Pointer handle,int val);
    
  /// @brief ��������Ƶ֡����ʵʱ��������106�ؼ������
  /// @param handle �ѳ�ʼ����ʵʱ�������پ��
  /// @param image ���ڼ���ͼ������
  /// @param pixel_format ���ڼ���ͼ�����ݵ����ظ�ʽ,��֧�֣����Ƽ�BGRA��BGR������
  /// @param image_width ���ڼ���ͼ��Ŀ��(������Ϊ��λ)
  /// @param image_height ���ڼ���ͼ��ĸ߶�(������Ϊ��λ)
  /// @param orientation ��Ƶ�������ķ���
  /// @param p_faces_array ��⵽��������Ϣ���飬api��������ڴ棬��Ҫ����st_mobile_release_tracker_result�����ͷ�
  /// @param p_faces_count ��⵽����������
  /// @return �ɹ�����CV_OK�����򷵻ش�������
    int st_mobile_tracker_106_track(
    	Pointer handle,
    	byte[] image,
    	int pixel_format,
    	int image_width,
    	int image_height,
    	int image_stride,
    	int orientation,
    	PointerByReference p_faces_array,
    	IntByReference p_faces_count
    );
    
    int st_mobile_tracker_106_track(
        	Pointer handle,
        	int[] image,
        	int pixel_format,
        	int image_width,
        	int image_height,
        	int image_stride,
        	int orientation,
        	PointerByReference p_faces_array,
        	IntByReference p_faces_count
        );
    
  /// @brief ��������Ƶ֡����ʵʱ��������106�ؼ�����٣��������������
    /// @param handle �ѳ�ʼ����ʵʱ�������پ��
    /// @param image ���ڼ���ͼ������
    /// @param pixel_format ���ڼ���ͼ�����ݵ����ظ�ʽ,��֧�֣����Ƽ�BGRA��BGR������
    /// @param image_width ���ڼ���ͼ��Ŀ��(������Ϊ��λ)
    /// @param image_height ���ڼ���ͼ��ĸ߶�(������Ϊ��λ)
    /// @param[in] image_stride ���ڼ���ͼ��Ŀ��(������Ϊ��λ)����ÿ�е��ֽ�����Ŀǰ��֧���ֽڶ����padding����֧��roi
    /// @param orientation ��Ƶ�������ķ���
    /// @param p_face_action_array ��⵽������106����Ϣ���������������飬api��������ڴ棬�Ḳ����һ�ε��û�ȡ��������
    /// @param p_faces_count ��⵽����������
    /// @return �ɹ�����ST_OK��ʧ�ܷ�������������,�����붨����st_common.h �У���ST_E_FAIL��
    int st_mobile_tracker_106_track_face_action(
            Pointer handle,
            byte[] image,
            int pixel_format,
            int image_width,
            int image_height,
            int image_stride,
            int orientation,
            PointerByReference p_face_action_array,
            IntByReference p_faces_count
    );
    void st_mobile_tracker_106_release_result(Pointer faces_array, int faces_count);
 
  /// @brief �����ѳ�ʼ����track106���
  /// @param handle �ѳ�ʼ���ľ��
    void st_mobile_tracker_106_destroy(Pointer handle);
  
   //========face detection
  /// @brief �������������
  int st_mobile_face_detection_create(String model_path, int config, PointerByReference handle);
  
  int st_mobile_face_detection_detect(
  	Pointer handle,
  	byte[] image,
  	int pixel_format,
  	int image_width,
  	int image_height,
  	int image_stride,
  	int orientation,
  	PointerByReference p_faces_array,
  	IntByReference p_faces_count
  );
  
  int st_mobile_face_detection_detect(
		  	Pointer handle,
		  	int[] image,
		  	int pixel_format,
		  	int image_width,
		  	int image_height,
		  	int image_stride,
		  	int orientation,
		  	PointerByReference p_faces_array,
		  	IntByReference p_faces_count
		  );

/// @brief �ͷ����������
  void st_mobile_face_detection_release_result(Pointer faces_array,int faces_count); 
  
/// @brief �����ѳ�ʼ�������������
/// @param[in] handle �ѳ�ʼ���ľ��
  void st_mobile_face_detection_destroy(Pointer handle);
  
}
