package org.netty.demo.transport;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by XiuYin.Cui on 2018/6/11.
 */
public class PlainOioServer {

    public static void main(String[] args) {
        new PlainOioServer().server(7070);
    }

    /**
     * 这段代码完全可以处理中等数量的并发客户端。但是随着应用程序变得流行起来，你会发现它并不能很好地
     * 伸缩到支撑成千上万的并发连接
     *
     * @param port 服务端端口号
     */
    public void server(int port) {
        System.out.println("启动服务器");
        try {
            final ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                            String readLine;
                            while ((readLine = reader.readLine()) != null){
                                System.out.println("服务器接收到请求" + readLine + ",现在把它回馈给客户端");
                                writer.println(readLine);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }finally {
                            try {
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
