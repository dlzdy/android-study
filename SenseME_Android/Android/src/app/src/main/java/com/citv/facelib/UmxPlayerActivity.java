package com.citv.facelib;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.citv.facelib.Model.ModelUtil;
import com.citv.facelib.Model.UmxResult;
import com.unity3d.player.UnityPlayer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kay on 2016/7/15.<br/>
 * 重写UnityPlayerActivity，提供与Unity交互接口
 * @author kay

 */
public class UmxPlayerActivity extends Activity {


    protected UnityPlayer mUnityPlayer;

    protected String friendFaceData = "";

    protected void onCreate(Bundle paramBundle) {
        requestWindowFeature(1);
        super.onCreate(paramBundle);
        getWindow().setFormat(2);
        this.mUnityPlayer = new UnityPlayer(this);
        setContentView(this.mUnityPlayer);
        this.mUnityPlayer.requestFocus();

        if (!ModelUtil.isModelDirExist()) {
            ModelUtil.copyFilesFassets(this, "AssetsData", ModelUtil.MODEL_PATH);
        }
    }

    protected void onDestroy() {
        this.mUnityPlayer.quit();
        super.onDestroy();
    }

    protected void onPause() {
        super.onPause();
        this.mUnityPlayer.pause();
    }

    protected void onResume() {
        super.onResume();
        this.mUnityPlayer.resume();
    }

    public void onConfigurationChanged(Configuration paramConfiguration) {
        super.onConfigurationChanged(paramConfiguration);
        this.mUnityPlayer.configurationChanged(paramConfiguration);
    }

    public void onWindowFocusChanged(boolean paramBoolean) {
        super.onWindowFocusChanged(paramBoolean);
        this.mUnityPlayer.windowFocusChanged(paramBoolean);
    }

    public boolean dispatchKeyEvent(KeyEvent paramKeyEvent) {
        if (paramKeyEvent.getAction() == 2) {
            return this.mUnityPlayer.injectEvent(paramKeyEvent);
        } else if (paramKeyEvent.getAction() == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.dispatchKeyEvent(paramKeyEvent);
    }

    public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent) {
        return this.mUnityPlayer.injectEvent(paramKeyEvent);
    }

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        return this.mUnityPlayer.injectEvent(paramKeyEvent);
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        return this.mUnityPlayer.injectEvent(paramMotionEvent);
    }

    public boolean onGenericMotionEvent(MotionEvent paramMotionEvent) {
        return this.mUnityPlayer.injectEvent(paramMotionEvent);
    }


    /**
     * CITV interfaces hereafter
     */


    /**
     * 获取Unity返回的面部动画数据
     * @return 无返回值
     */
    public void GetFaceAnimatorData() {
        UnityPlayer.UnitySendMessage("UmxMananger", "GetFaceAnimatorData", "");
    }

    /**
     * 张嘴校验
     * @return 无返回值
     */
    public void calibrateMyMouthOpen() {
        UnityPlayer.UnitySendMessage("UmxMananger", "OpenMouthCheck", "");
    }

    /**
     * 闭嘴校验
     * @return 无返回值
     */
    public void calibrateMyMouthClose() {
        UnityPlayer.UnitySendMessage("UmxMananger", "CloseMouthCheck", "");
    }

    /**
     * 设置角色
     * @param {String} Name (模型名称)
     * @param {boolean} isMy (true:设置自己；false:设置好友)
     * @return {UmxResult} 枚举类型
     * @throws JSONException
     */
    public UmxResult setRole(String Name, boolean isMy) {
        JSONObject roleData = new JSONObject();
        try {
            roleData.put("name", ModelUtil.MODEL_PATH + "/" + Name);
            roleData.put("isMy", isMy);
            UnityPlayer.UnitySendMessage("UmxMananger", "LoadMyCharacter", roleData.toString());
            return UmxResult.SUCCESS;
        } catch (JSONException e) {
            return UmxResult.ERR_UNKOWN;
        }
    }

    /**
     * 设置为前置窗口
     * @param {boolean} isMy (true:设置自己；false:设置好友)
     * @return 无返回值
     * @throws JSONException
     */
    public void setWindowFront(boolean isMy){
        JSONObject Data = new JSONObject();
        try {
            Data.put("isMy", isMy);
            UnityPlayer.UnitySendMessage("UmxMananger", "SetMyWindowFront", Data.toString());
        } catch (JSONException e) {

        }
    }

    /**
     * 设置窗口坐标起点<br/>
     * x,y范围为0-1，（0,0）为左下角
     * @param {float} x (横坐标)
     * @param {float} y (纵坐标)
     * @param {boolean} isMy (true:设置自己；false:设置好友)
     * @return 无返回值
     * @throws JSONException
     */
    public void SetWindowPosition(float x, float y, boolean isMy){
        JSONObject winData = new JSONObject();
        try {
            winData.put("x", x);
            winData.put("y", y);
            winData.put("isMy", isMy);
            UnityPlayer.UnitySendMessage("UmxMananger", "SetMyWindowPosition", winData.toString());
        } catch (JSONException e) {

        }
    }

    /**
     * 设置窗口大小<br/>
     * x,y范围为0-1，（1,1）为满窗口
     * @param {float} x (窗口宽)
     * @param {float} y (窗口高)
     * @param {boolean} isMy (true:设置自己；false:设置好友)
     * @return 无返回值
     * @throws JSONException
     */
    public void SetWindowSize(float x, float y, boolean isMy){
        JSONObject winData = new JSONObject();
        try {
            winData.put("x", x);
            winData.put("y", y);
            winData.put("isMy", isMy);
            UnityPlayer.UnitySendMessage("UmxMananger", "SetMyWindowSize", winData.toString());
        } catch (JSONException e) {

        }
    }

    /**
     * 设置窗口背景色<br/>
     * r,g,b范围为0-255
     * @param {float} r
     * @param {float} g
     * @param {float} b
     * @param {boolean} isMy (true:设置自己；false:设置好友)
     * @return 无返回值
     * @throws JSONException
     */
    public void SetWindowBackColor(int r, int g, int b, boolean isMy) {
        JSONObject winData = new JSONObject();
        try {
            winData.put("r", (float)r/255);
            winData.put("g", (float)g/255);
            winData.put("b", (float)b/255);
            winData.put("isMy", isMy);
            UnityPlayer.UnitySendMessage("UmxMananger", "SetMyWindowBackColor", winData.toString());
        } catch (JSONException e) {

        }
    }

    /**
     * 设置相机位置（左手系坐标）
     * @param {float} x  相机左右坐标，左负右正
     * @param {float} y  相机垂直坐标，下负上正
     * @param {float} z  相机前后坐标，后负前正
     * @param {boolean} isMy (true:设置自己；false:设置好友)
     * @return 无返回值
     * @throws JSONException
     */
    public void SetCameraPosition(float x, float y, float z, boolean isMy) {
        JSONObject Data = new JSONObject();
        try {
            Data.put("x", x);
            Data.put("y", y);
            Data.put("z", z);
            Data.put("isMy", isMy);
            UnityPlayer.UnitySendMessage("UmxMananger", "SetCameraPosition", Data.toString());
        } catch (JSONException e) {

        }
    }

    /**
     * 交换窗口
     * @return 无返回值
     */
    public void SwapWindow() {
        UnityPlayer.UnitySendMessage("UmxMananger", "SwapWindow", "");
    }

    /**
     * 发送动画数据
     * @param {String} data (动作数据)
     * @param {boolean} isMy (true:设置自己；false:设置好友)
     * @return 无返回值
     * @throws JSONException
     */
    public void SendCharacterData(String data, boolean isMy) {
        JSONObject faceData = new JSONObject();
        try {
            faceData.put("data", data);
            faceData.put("isMy", isMy);
            UnityPlayer.UnitySendMessage("UmxMananger", "SendMyCharacterDate", faceData.toString());
        } catch (JSONException e) {

        }
    }
}
