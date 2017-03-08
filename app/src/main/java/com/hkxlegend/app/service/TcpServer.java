package com.hkxlegend.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.hkxlegend.app.util.MsgUtil;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer extends Service {

    private ServerSocket serverSocket;

    private static boolean isConnect = true;

    private final static int PORT = 8801;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startServer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isConnect = false;
    }

    private void startServer() {

        new Thread() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(PORT);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                while (isConnect) {
                    try {
                        final Socket socket = serverSocket.accept();
                        log("connect success");

                        new Thread() {
                            @Override
                            public void run() {
                                responseClient(socket);
                            }
                        }.start();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }.start();

    }

    private void responseClient(Socket socket) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            /**
             * PrintWriter pWriter = new PrintWriter(oStream,true); 设置为true 直接发送，不必等到缓冲区,如果使用BufferedWriter,最好采用 socket.shutdownOutput();这样另一端才会接受到，或者采用socket.close();
             * 不过在服务器端，最好是采用socket.shutdownOutput(),因为这样不会关闭socket，socket的关闭给客户端来做，减少通信的开销
             * BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             * writer.write("您好,请问有什么想聊的呀 ^ ^");
             * writer.flush();
             * socket.shutdownOutput();
             */
            PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            writer.println("您好,客官想聊点啥 ^ ^");

            while (isConnect) {
                String entitiy = br.readLine();
                log(entitiy + "");
                String msg = MsgUtil.returnMsg(entitiy);
                writer.println(msg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void log(String msg) {
        Log.e("TcpServer", msg);
    }
}
