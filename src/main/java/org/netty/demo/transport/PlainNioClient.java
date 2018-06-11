package org.netty.demo.transport;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by XiuYin.Cui on 2018/6/11.
 */
public class PlainNioClient {

    private Integer blockSize = 4096;
    /**
     * 发送数据缓冲区
     */
    private ByteBuffer sendbuffer = ByteBuffer.allocate(blockSize);
    /**
     * 接收数据缓冲区
     */
    private ByteBuffer receivebuffer = ByteBuffer.allocate(blockSize);


    public static void main(String[] args) {
        new PlainNioClient().start();
    }

    public void start() {
        try {
            //打开通道
            SocketChannel socketChannel = SocketChannel.open();
            //通道设置成非阻塞模式
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress("127.0.0.1", 5555));
            //打开筛选器
            Selector selector = Selector.open();
            //注册选择器
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            SocketChannel client = null;
            while (true) {
                //selectNow()非阻塞，select(timeout)和select()阻塞
                int readyChannels = selector.selectNow();
                if (readyChannels == 0 ){
                    continue;
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    try {
                        if (key.isConnectable()) {
                            System.out.println("client connect");
                            client = (SocketChannel) key.channel();
                            if (client.isConnectionPending()) {
                                client.finishConnect();
                                System.out.println("客户端完成连接操作！");
                                sendbuffer.clear();
                                sendbuffer = Charset.forName("UTF-8").encode("Hello,Server");
                                sendbuffer.flip();
                                client.write(sendbuffer);
                            }
                            client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        } else if (key.isReadable()) {
                            client = (SocketChannel) key.channel();
                            receivebuffer.clear();
                            StringBuffer sb = new StringBuffer();
                            while ((client.read(receivebuffer)) > 0) {
                                receivebuffer.flip();
                                sb.append(Charset.forName("UTF-8").decode(receivebuffer).toString());
                                receivebuffer.clear();
                            }
                            System.out.println("接收到服务端回复的消息是：" + sb.toString());
                        } else if (key.isWritable()) {
                            sendbuffer.clear();
                            client = (SocketChannel) key.channel();
                            sendbuffer = Charset.forName("UTF-8").encode("客户端发送消息");
                            sendbuffer.flip();
                            client.write(sendbuffer);
                        } else {
                            client.close();
                        }
                    } finally {

                    }
                }
            }
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
