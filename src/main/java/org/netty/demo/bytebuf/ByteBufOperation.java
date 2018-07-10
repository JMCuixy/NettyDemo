package org.netty.demo.bytebuf;

import io.netty.buffer.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

/**
 * Created by XiuYin.Cui on 2018/6/21.
 */
public class ByteBufOperation {

    private ByteBuf byteBuf = Unpooled.copiedBuffer("Hello,Netty!", CharsetUtil.UTF_8);

    public void createBuf() {
        //获得ByteBufAllocator 的两种方式
        //1、
        Channel channel = null;
        ByteBufAllocator alloc = channel.alloc();
        //2、
        ChannelHandlerContext channelHandlerContext = null;
        ByteBufAllocator alloc1 = channelHandlerContext.alloc();

        //ByteBufAllocator 创建 ByteBuf 实例
        //1、返回一个基于堆或者直接内存存储的ByteBuf
        ByteBuf byteBuf = alloc.buffer(256, Integer.MAX_VALUE);
        //2、返回一个基于堆内存存储的 ByteBuf 实例
        ByteBuf byteBuf1 = alloc.heapBuffer(256);

        byteBuf1.refCnt();//检查该ByteBuf的引用计数
        byteBuf1.release();//将ByteBuf的引用计数设为0并释放

        //3、返回一个基于直接内存存储的 ByteBuf
        ByteBuf byteBuf2 = alloc.directBuffer();
        //4、返回一个可以通过添加最大到指定数目的基于堆的或者直接内存存储的缓冲区来扩展的 CompositeByteBuf
        CompositeByteBuf compositeByteBuf = alloc.compositeBuffer();
        CompositeByteBuf compositeByteBuf1 = alloc.compositeHeapBuffer(16);
        CompositeByteBuf compositeByteBuf2 = alloc.compositeDirectBuffer(16);
        //5、返回一个用于套接字的 I/O 操作的ByteBuf
        ByteBuf byteBuf3 = alloc.ioBuffer();


        //Unpooled 创建 ByteBuf 实例
        //1、创建一个未池化的基于堆内存存储的 ByteBuf 实例
        ByteBuf buf = Unpooled.buffer();
        //2、创建一个未池化的基于内存存储的ByteBuf
        ByteBuf buf1 = Unpooled.directBuffer(256, Integer.MAX_VALUE);
        //3、返回一个包装了给定数据的 ByteBuf
        Unpooled.wrappedBuffer("Hello Netty".getBytes());
        //4、返回一个复制了给定数据的 ByteBuf
        Unpooled.copiedBuffer("Hello Netty", CharsetUtil.UTF_8);
    }

    public void operationByteBuf() {
        //1、遍历访问(第一个字节的索引是0，最后一个字节的索引总是capacity()-1 )
        for (int i = 0; i < byteBuf.capacity(); i++) {
            byte aByte = byteBuf.getByte(i);
            System.out.print((char) aByte);
        }

        System.out.println("\r\n-----2--分割线--------");
        //2、读取所有数据
        while (byteBuf.isReadable()){
            System.out.print((char) byteBuf.readByte());
        }

        System.out.println("\r\n-----3--分割线--------");
        //3、写入数据
        while (byteBuf.writableBytes() >= 4){
            byteBuf.writeByte(65);
        }
        while (byteBuf.isReadable()){
            System.out.print((char) byteBuf.readByte());
        }

        System.out.println("\r\n----4---分割线--------");
        //4、索引标志管理
        ByteBuf buf = byteBuf.readerIndex(0);//将 readerIndex 移动到指定的位置
        buf.markReaderIndex();//标记当前的 readerIndex
        while (buf.isReadable()){
            System.out.print((char) buf.readByte());
        }
        buf.resetReaderIndex();//回退到之前标记的 readerIndex
        while (buf.isReadable()){
            System.out.print((char) buf.readByte());
        }

        System.out.println("\r\n----5---分割线--------");
        //5、查找操作
        int index = byteBuf.indexOf(0, byteBuf.capacity() - 1, (byte) 65);
        System.out.print(index);

        System.out.println("\r\n----6---分割线--------");
        //6、分片操作
        ByteBuf slice = byteBuf.slice(0, 15);
        System.out.print(slice.toString(CharsetUtil.UTF_8));
        //更新索引0处的字节
        slice.setByte(0, (byte) 'J');
        byte aByte = byteBuf.getByte(0);
        System.out.print("\r\n" + (char)aByte);

        System.out.println("\r\n----7---分割线--------");
        //7、复制操作
        ByteBuf copy = buf.copy(0, 15);
        System.out.println(copy.toString(CharsetUtil.UTF_8));
        copy.setByte(0, (byte) 'A');
        System.out.println((char) byteBuf.getByte(0));

        System.out.println("\r\n----8---分割线--------");
        //8、其他操作
        System.out.println("如果至少有一个字节可读取：" + byteBuf.isReadable());
        System.out.println("如果至少有一个字节可写入：" + byteBuf.isWritable());
        System.out.println("返回可被读取的字节数：" + byteBuf.readableBytes());
        System.out.println("返回可被写入的字节数：" + byteBuf.writableBytes());
        System.out.println("可容纳的字节数：" + byteBuf.capacity() + ",可扩展最大的字节数：" + byteBuf.maxCapacity());
        System.out.println("是否由一个字节数组支撑：" + byteBuf.hasArray());
        System.out.println("由一个字节数组支撑则返回该数组：" + byteBuf.array().length);
        System.out.println("计算第一个字节的偏移量：" + byteBuf.arrayOffset());
        System.out.println("返回Bytebuf的十六进制：" + ByteBufUtil.hexDump(byteBuf.array()));


        byteBuf.release();

    }

    public static void main(String[] args) {
        ByteBufOperation bufOperation = new ByteBufOperation();
        bufOperation.operationByteBuf();
    }

}
