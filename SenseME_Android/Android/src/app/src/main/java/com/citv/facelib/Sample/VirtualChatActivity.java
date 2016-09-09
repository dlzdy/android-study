package com.citv.facelib.Sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.citv.facelib.Accelerometer;
import com.citv.facelib.FaceOverlapFragment;
import com.citv.facelib.Model.UmxResult;
import com.citv.facelib.R;
import com.citv.facelib.UmxPlayerActivity;

/**
 *
 * Created by kay on 2016/7/15.<br/>
 * 继承UmxPlayerActivity，Unity交互接口使用实例
 * @author kay
 *
 */
public class VirtualChatActivity extends UmxPlayerActivity {

    static VirtualChatActivity instance = null;
    private LinearLayout u3d_layout;
    private Button bOpenMouth = null, bCLoseMouth = null, bChooseMyColor = null, bChooseFriendColor = null, bChangeWindow = null;
    final static int My = 0, Friend = 1;

    //重力传感器
    public static Accelerometer acc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtualchat);
        instance = this;

        //开启重力传感器监听
        acc = new Accelerometer(this);
        acc.start();

        //添加UnityPlayer到布局
        u3d_layout = (LinearLayout) findViewById(R.id.u3d_layout);
        u3d_layout.addView(mUnityPlayer);

        //设模型置窗口位置
        SetWindowPosition(0.6f,0.7f,true);
        //设模型置窗口大小
        SetWindowSize(0.4f,0.3f,true);
        //设置模型窗口背景色
        SetWindowBackColor(0,0,0,true);

        SetWindowPosition(0,0,false);
        SetWindowSize(1,1,false);
        SetWindowBackColor(255,255,255,false);

        //设置模型窗口置前
        setWindowFront(true);
        //设置相机位置
        SetCameraPosition(0, 1.27f, -1.5f, true);
        SetCameraPosition(0, 1.27f, -1.5f, false);

        bChangeWindow = (Button) findViewById(R.id.windowChange);
        bChangeWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //交换窗口
                SwapWindow();
            }
        });

        bChooseMyColor = (Button) findViewById(R.id.my_color);
        bChooseMyColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VirtualChatActivity.this, ColorActivity.class);
                startActivityForResult(intent, My);
            }
        });
        bChooseFriendColor = (Button) findViewById(R.id.friend_color);
        bChooseFriendColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VirtualChatActivity.this, ColorActivity.class);
                startActivityForResult(intent, Friend);
            }
        });


        //设置角色
        if (setRole("maili", true) == UmxResult.SUCCESS && setRole("maili", false) == UmxResult.SUCCESS) {

            bOpenMouth = (Button) findViewById(R.id.openmouth);
            bOpenMouth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //张嘴校验
                    calibrateMyMouthOpen();
                }
            });

            bCLoseMouth = (Button) findViewById(R.id.closemouth);
            bCLoseMouth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //闭嘴校验
                    calibrateMyMouthClose();
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        final FaceOverlapFragment fragment = (FaceOverlapFragment) getFragmentManager()
                .findFragmentById(R.id.overlapFragment);
        fragment.registTrackCallback(new FaceOverlapFragment.TrackCallBack() {

            @Override
            public void onTrackdetected(final String value) {
                // TODO Auto-generated method stub

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //获取摄像头数据传给unity，驱动自己的3D模型
                        SendCharacterData(value, true);

                        //从Unity获取自己的3D模型数据
                        GetFaceAnimatorData();

                        if (!friendFaceData.equals("")) {
                            //用3D模型数据驱动友方角色
                            SendCharacterData(friendFaceData, false);
                        }
                    }
                });
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                int color = data.getExtras().getInt("color");
                int r = (color & 0xff0000) >> 16;
                int g = (color & 0x00ff00) >> 8;
                int b = (color & 0x0000ff);
                if (requestCode == My) {
                    SetWindowBackColor(r,g,b,true);
                } else if (requestCode == Friend) {
                    SetWindowBackColor(r,g,b,false);
                }
                break;
            default:
                break;
        }
    }
}
