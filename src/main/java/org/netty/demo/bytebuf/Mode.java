package org.netty.demo.bytebuf;

import io.netty.buffer.*;
import io.netty.util.CharsetUtil;

import java.nio.BufferUnderflowException;
import java.util.Iterator;

/**
 * Created by XiuYin.Cui on 2018/6/15.
 * <p>
 * ByteBuf 的使用模式
 */
public class Mode {

    private ByteBuf byteBuf = null;


    /**
     * ByteBuf 堆缓冲区
     * 将数据存储在 JVM 的堆空间中。
     * 这种模式被称为支撑数组（backing array）， 它能在没有使用池化的情况下提供快速的分配和释放。
     * 非常适合于有遗留的数据需要处理的情况。
     */
    public void heapBuffer() {
        //检查 ByteBuf 是否有一个支撑数组
        if (byteBuf.hasArray()) {
            //如果有，则获取对该数组的引用
            byte[] array = byteBuf.array();
            //计算第一个字节的偏移量
            int offset = byteBuf.arrayOffset() + byteBuf.readerIndex();
            //获得可读字节数
            int length = byteBuf.readableBytes();
            //使用数组、偏移量和长度作为参数处理数据
        }
    }

    /**
     * 直接缓冲区
     * 直接缓冲区的内容将驻留在常规的会被垃圾回收的堆之外
     * 缺点：
     * 1、它们的分配和释放都很昂贵
     * 2、数据不在堆上，不得不进行一次复制
     */
    public void directBuffer() {
        if (byteBuf.hasArray()) {

        } else {
            //不是由数组支撑，是一个直接缓冲区
            int length = byteBuf.readableBytes();
            //分配一个新的数组来保存具有该长度的字节数据
            byte[] array = new byte[length];
            //将字节复制到该数组
            byteBuf.getBytes(byteBuf.readerIndex(), array);
        }

    }

    /**
     * 复合缓冲区，为多个ByteBuf 提供了一个聚合视图。可以根据需要添加或者删除 ByteBuf 实例
     *
     * Netty使用了CompositeByteBuf来优化套接字的 I/O 操作，尽可能地消除了由JDK的缓冲区实现所导致的性能以及内存使用率的惩罚
     */
    public void CompositeBuffer() {
        CompositeByteBuf messageBuf = Unpooled.compositeBuffer();
        ByteBuf headBuf = Unpooled.copiedBuffer("Hello,", CharsetUtil.UTF_8);
        ByteBuf bodyBuf = Unpooled.copiedBuffer("Netty!", CharsetUtil.UTF_8);
        //将 ByteBuf 实例追加到 CompositeByteBuf
        messageBuf.addComponents(headBuf, bodyBuf);
        Iterator<ByteBuf> it = messageBuf.iterator();
        //访问CompositeByteBuf数据
        while(it.hasNext()){
            ByteBuf buf = it.next();
            while (buf.isReadable()){
                System.out.print((char) buf.readByte());
            }
        }
        //使用数组访问数据
        if(!messageBuf.hasArray()){
            int len = messageBuf.readableBytes();
            byte[] arr = new byte[len];
            messageBuf.getBytes(0, arr);
            for (byte b : arr){
                System.out.print((char)b);
            }
        }
        messageBuf.removeComponent(0); //删除位于索引位置为 0（第一个组件）的 ByteBuf
    }


    public static void main(String[] args) {
        Mode mode = new Mode();
        mode.CompositeBuffer();
    }
}
