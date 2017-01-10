package com.hljk365.umeng_test;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private final String mPageName = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        MobclickAgent.setDebugMode(true);
        MobclickAgent.setScenarioType(mContext, MobclickAgent.EScenarioType.E_UM_NORMAL);

        View btnStartup = findViewById(R.id.btnStartup);
        btnStartup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MobclickAgent.onEvent(mContext, "startup");
                Log.i("testUmeng", "startup");
            }
        });

        //
        View btnScan = findViewById(R.id.btnScan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> map_ekv = new HashMap<String, String>();
                map_ekv.put("did", System.currentTimeMillis() + "");
                map_ekv.put("uid", "6591");
                MobclickAgent.onEvent(mContext, "open_scan_qrcode", map_ekv);
                Log.i("testUmeng", "open_scan_qrcode");
            }
        });

        //
        View btnScanSuccess = findViewById(R.id.btnScanSuccess);
        btnScanSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> map_ekv = new HashMap<String, String>();
                map_ekv.put("did", System.currentTimeMillis() + "");
                map_ekv.put("uid", "6591");
                MobclickAgent.onEvent(mContext, "scan_qrcode_success", map_ekv);
                Log.i("testUmeng", "scan_qrcode_success");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
       // MobclickAgent.onPageStart(mPageName);
        //MobclickAgent.onResume(mContext);
    }

    @Override
    public void onPause() {
        super.onPause();
       // MobclickAgent.onPageEnd(mPageName);
       // MobclickAgent.onPause(mContext);
    }
}
