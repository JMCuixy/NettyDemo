package org.netty.demo.udp;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.security.spec.DSAGenParameterSpec;

/**
 * Created by XiuYin.Cui on 2018/6/4.
 */
public class UDPServer {

    public static void main(String[] args) {
        DatagramSocket server = null;
        try {
            server = new DatagramSocket(8088);
            byte[] datas = new byte[1024];
            //用一个字节数组接收UDP包，字节数组在传递给构造函数时是空的
            DatagramPacket datagramPacket = new DatagramPacket(datas, datas.length);
            server.receive(datagramPacket);
            System.out.println(new String(datas));
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            server.close();
        }
    }
}
