package com.hkxlegend.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.hkxlegend.app.R;
import com.hkxlegend.app.adapter.MsgAdapter;
import com.hkxlegend.app.model.MsgBean;
import com.hkxlegend.app.service.UdpServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class UdpActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private EditText editText;
    private MsgAdapter mAdapter;
    private List<MsgBean> msgList;

    private Intent serverIntent;
    private InetAddress inetAddress;
    private DatagramSocket mSocket;

    private final static int MYPORT = 8880;
    private final static int OTHERPORT = 8881;
    private final static String ADDRESS = "localhost";
    private int position = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udp);

        serverIntent = new Intent(this, UdpServer.class);
        startService(serverIntent);

        initView();
        startConnect();
    }

    private void initView() {
        msgList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list_tcp);
        editText = (EditText) findViewById(R.id.edit_tcp);

        findViewById(R.id.button_tcp).setOnClickListener(this);
        mAdapter = new MsgAdapter(this, msgList);
        listView.setAdapter(mAdapter);
    }

    private void startConnect() {

        new Thread() {
            @Override
            public void run() {
                try {
                    mSocket = new DatagramSocket(MYPORT);
                    inetAddress = InetAddress.getByName(ADDRESS);
                    while (true) {
                        byte[] data = new byte[1024];
                        DatagramPacket packet = new DatagramPacket(data, data.length);
                        mSocket.receive(packet);
                        final String content = new String(packet.getData(), packet.getOffset(), packet.getLength());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MsgBean bean = new MsgBean();
                                bean.setMsgFromMe(false);
                                bean.setMsg(content);
                                msgList.add(bean);
                                mAdapter.notifyDataSetChanged();

                            }
                        });
                    }

                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    @Override
    protected void onDestroy() {
        stopService(serverIntent);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_tcp:
                sendMsg(editText.getText().toString());
                break;
        }
    }

    private void sendMsg(final String s) {


        new Thread() {
            @Override
            public void run() {
                try {
                    byte[] msg = s.getBytes();
                    DatagramPacket packet = new DatagramPacket(msg, msg.length, inetAddress, OTHERPORT);
                    mSocket.send(packet);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MsgBean bean = new MsgBean();
                            bean.setMsg(s);
                            bean.setMsgFromMe(true);

                            editText.setText("");
                            msgList.add(bean);
                            position += 2;
                            listView.smoothScrollToPosition(position);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }

    private void log(String msg) {
        Log.e("UdpClient", msg);
    }
}
