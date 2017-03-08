package com.hkxlegend.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.hkxlegend.app.R;
import com.hkxlegend.app.adapter.MsgAdapter;
import com.hkxlegend.app.model.MsgBean;
import com.hkxlegend.app.service.TcpServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TcpActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private EditText editText;
    private MsgAdapter mAdapter;
    private List<MsgBean> msgList;

    private Intent serviceIntent;
    private Socket mClient;
    private PrintWriter writer;
    private int position = 1;

    private static final String SERVER = "localhost";
    private static final int PORT = 8801;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcp);

        //启动服务端
        serviceIntent = new Intent(this, TcpServer.class);
        startService(serviceIntent);

        new Thread(new InnerSocket()).start();
        initView();
    }

    private void initView() {
        msgList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list_tcp);
        editText = (EditText) findViewById(R.id.edit_tcp);

        findViewById(R.id.button_tcp).setOnClickListener(this);
        mAdapter = new MsgAdapter(this, msgList);
        listView.setAdapter(mAdapter);
    }

    //发送消息
    private void sendMsg(final String msg) {

        if (!TextUtils.isEmpty(msg) && mClient != null) {
            new Thread() {
                @Override
                public void run() {
                    try {
//                        if (bos == null) {
//                            bos = new BufferedOutputStream(mClient.getOutputStream());
//                        }
//                        bos.write(msg.getBytes());
//                        bos.flush();
//                        mClient.shutdownOutput();
                        /**
                         * 表示这边已经输出完毕 才能发送出去,但是调用了shutdownOutput或shutdownInput关闭了输出流或输入流后,该socket无法再次打开输入流或输出流
                         * 因为这种做法通常不适合做持久通信,只适合做一站式交互,如HTTP协议:一种发送响应式的方式.在客户端或者服务端通过socket.shutdownOutput()都是单向关闭的，
                         * 即关闭客户端的输出流并不会关闭服务端的输出流，所以是一种单方向的关闭流；通过socket.shutdownOutput()关闭输出流，但socket仍然是连接状态，连接并未关闭
                         * 如果直接关闭输入或者输出流，即：in.close()或者out.close()，会直接关闭socket
                         *
                         * 这里我们如果采用这种方式,服务端那边是bufferedReader.readLine方法,readLine方法在一般情况下是阻塞当前线程的,但是readLine方法
                         * 在遇到终止符、数据流发生异常或者另一端被close()掉时会返回null值。 这样会造成服务端 while中readLine无法阻塞,导致异常
                         * 所以我们换一种方式
                         */
                        if (writer == null) {
                            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mClient.getOutputStream())), true);
                        }
                        writer.println(msg);

                        MsgBean bean = new MsgBean();
                        bean.setMsg(msg);
                        bean.setMsgFromMe(true);
                        msgList.add(bean);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                                editText.setText("");
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(serviceIntent);
        try {
            if (writer != null) {
                writer.close();
            }
            if (mClient != null) {
                mClient.shutdownInput();
                mClient.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class InnerSocket implements Runnable {

        Socket socket;

        @Override
        public void run() {

            while (socket == null) {
                try {
                    socket = new Socket(SERVER, PORT);
                    mClient = socket;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (!TcpActivity.this.isFinishing()) {
                    String entity = reader.readLine();
                    log(entity + "");

                    if (entity != null) {
                        MsgBean bean = new MsgBean();
                        bean.setMsg(entity);
                        bean.setMsgFromMe(false);
                        msgList.add(bean);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                            }
                        });

                    }

                }

                reader.close();
                mClient.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_tcp:
                sendMsg(editText.getText().toString());
                break;
        }
    }

    private void log(String msg) {
        Log.e("TcpClient", msg);
    }
}
