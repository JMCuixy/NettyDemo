package org.netty.demo.udp;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by XiuYin.Cui on 2018/6/4.
 */
public class UDPServer {

    public static void main(String[] args) {
        try {
            DatagramSocket ds = new DatagramSocket(8088);
            byte[] datas = new byte[1024];
            //接收数据报
            DatagramPacket datagramPacket = new DatagramPacket(datas, datas.length);
            ds.receive(datagramPacket);

            System.out.println(new String(datas));

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
