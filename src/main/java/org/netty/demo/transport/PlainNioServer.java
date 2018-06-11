package org.netty.demo.transport;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by XiuYin.Cui on 2018/6/11.
 */
public class PlainNioServer {

    private Integer blockSize = 4096;
    /**
     * 发送数据缓存区
     */
    private ByteBuffer sendBuffer = ByteBuffer.allocate(blockSize);
    /**
     * 接收数据缓冲区
     */
    private ByteBuffer receiveBuffer = ByteBuffer.allocate(blockSize);
    /**
     * 选择器
     */
    private Selector selector;

    public static void main(String[] args) {
        new PlainNioServer().listen(5555);
    }

    public void listen(int port) {
        try {
            //1、创建未连接的SocketChannel
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            //2、设置为非阻塞模式
            serverSocketChannel.configureBlocking(false);
            //3、返回此SocketChannel的socket
            ServerSocket socket = serverSocketChannel.socket();
            //4、将服务器绑定到指定的端口
            socket.bind(new InetSocketAddress(port));
            //5、打开Selector来处理Channel
            selector = Selector.open();
            //6、将 ServerSocket 注册到 Selector 以接受连接
            //OP_READ:输入流有数据可读
            //OP_WRITE:输出流有空位可写
            //OP_CONNECT:连接成功
            //OP_ACCEPT:有连接请求事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            //服务器监听
            while (true) {
                //7、等待需要处理的新事件；阻塞 将一直持续到下一个传入事件，如果timeout为0，则禁用超时
                selector.select();
                //8、获取所有接收事件的SelectionKey实例,对这个集合只能删除，不得做其它修改。
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    //业务逻辑
                    handleKey(key);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //业务逻辑
    public void handleKey(SelectionKey key) throws IOException {
        // 服务端监听通道
        ServerSocketChannel serverSocketChannel;
        SocketChannel socketChannel = null;
        try {
            //9、检查是否有连接请求事件以发生
            if (key.isAcceptable()) {
                //10、返回事件触发的Channel
                serverSocketChannel = (ServerSocketChannel) key.channel();
                socketChannel = serverSocketChannel.accept();//接受连接请求
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                System.out.println("Accepted connection from:" + socketChannel);
            } else if (key.isReadable()) {//检查是否有可读事件发生
                socketChannel = (SocketChannel) key.channel();
                StringBuffer sb = new StringBuffer();

                while ((socketChannel.read(receiveBuffer)) > 0) {
                    //一定要调用flip函数，否则读取错误数据
                    receiveBuffer.flip();
                    sb.append(Charset.forName("UTF-8").decode(receiveBuffer).toString());
                    receiveBuffer.clear();
                }
                System.out.println("接收到客户端的消息是：" + sb.toString());
            } else if (key.isWritable()) {//检查是否有可写事件发生
                sendBuffer.clear();
                socketChannel = (SocketChannel) key.channel();
                sendBuffer = Charset.forName("UTF-8").encode("服务端反馈消息");
                socketChannel.write(sendBuffer);
            } else {
                socketChannel.close();
            }
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //解除绑定的事件
            key.cancel();
            key.channel().close();
        }
    }
}
