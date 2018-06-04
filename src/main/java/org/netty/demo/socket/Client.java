package org.netty.demo.socket;

import java.io.*;
import java.net.Socket;

/**
 * Created by XiuYin.Cui on 2018/6/4.
 */
public class Client {

    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket("192.168.1.22", 8989);
            //向服务器发送数据
            //printWriter（字符流）/printStream（字节流）
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

            //接收服务器的反馈
            InputStream inputStream = socket.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            //模拟交互
            BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

            String request, responese;
            while ((request = bf.readLine()) != null) {
                printWriter.println(request);
                responese = in.readLine();
                System.out.println(responese);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
