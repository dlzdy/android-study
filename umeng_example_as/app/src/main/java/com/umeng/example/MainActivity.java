package com.umeng.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.umeng.example.analytics.AnalyticsHome;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobclickAgent.setDebugMode(true);

    }

    public void onClick(View v) {
        int id = v.getId();
        Intent in = null;
        if (id == R.id.normal) {
            in = new Intent(this, AnalyticsHome.class);
        } else if (id == R.id.game) {

        } else if (id == R.id.btnTestEvent){
            Map<String, String> map_ekv = new HashMap<String, String>();
            map_ekv.put("did", System.currentTimeMillis() + "");
            map_ekv.put("uid", "6591");
            MobclickAgent.onEvent(this, "scan_qrcode_success555", map_ekv);
        }
        if (in != null) {
            startActivity(in);
        }
    }

}
