package com.hkxlegend.app.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hkxlegend.app.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tcp_button).setOnClickListener(this);
        findViewById(R.id.udp_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tcp_button:
                Intent intent1 = new Intent(this, TcpActivity.class);
                startActivity(intent1);
                break;
            case R.id.udp_button:
                Intent intent2 = new Intent(this, UdpActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
