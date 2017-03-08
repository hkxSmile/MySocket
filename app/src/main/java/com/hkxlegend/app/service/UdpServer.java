package com.hkxlegend.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.hkxlegend.app.util.MsgUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpServer extends Service {

    private InetAddress inetAddress;
    private DatagramSocket socket;

    private final static int MYPORT = 8881;
    private final static int OTHERPORT = 8880;
    private final static String ADDRESS = "localhost";

    @Override
    public IBinder onBind(Intent intent) {
        throw null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new MsgClass()).start();
    }

    class MsgClass implements Runnable {

        @Override
        public void run() {
            try {
                //先发送信息
                socket = new DatagramSocket(MYPORT);
                inetAddress = InetAddress.getByName(ADDRESS);

                String s = "hi 有什么可以帮您的吗?";
                byte[] hello = s.getBytes();
                DatagramPacket packet = new DatagramPacket(hello, hello.length, inetAddress, OTHERPORT);
                socket.send(packet);

                //后开始监听消息
                while (true) {
                    byte[] msg = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(msg, msg.length);
                    socket.receive(receivePacket);

                    String getMsg = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
                    String returnMsg = MsgUtil.returnMsg(getMsg);
                    byte[] returnByte = returnMsg.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(returnByte, returnByte.length, inetAddress, OTHERPORT);
                    socket.send(sendPacket);
                }

            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
