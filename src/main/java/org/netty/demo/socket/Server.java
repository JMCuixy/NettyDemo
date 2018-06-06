package org.netty.demo.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by XiuYin.Cui on 2018/6/4.
 */
public class Server {

    public static void main(String[] args) {
        try {
            System.out.println("启动服务器......");
            //创建一个新的ServerSocket，用来监听指定端口上的连接请求
            ServerSocket serverSocket = new ServerSocket(8989);
            Socket socket = null;
            while (true) {
                try {
                    //对accept()方法的调用将被阻塞，直到一个连接创建
                    //该socket用于客户端和服务器之间的通信
                    //流对象都派生于该套接字的流对象
                    socket = serverSocket.accept();
                    InputStream inputStream = socket.getInputStream();
                    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    String request, response;
                    while ((request = in.readLine()) != null) {
                        //如果客户端发送了exit，则退出循环
                        if ("exit".equals(request)) {
                            break;
                        }
                        System.out.println("接收到客户端请求：" + request);

                        //服务端处理方法
                        response ="客户端：" + socket.getInetAddress().getHostAddress() + " 请求，" + processRequest(request);
                        //响应给客户端
                        out.println(response);
                    }
                } catch (Exception e) {
                    System.out.println("Error");
                } finally {
                    socket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String processRequest(String request) {
        return "服务端反馈:" + request;
    }
}
