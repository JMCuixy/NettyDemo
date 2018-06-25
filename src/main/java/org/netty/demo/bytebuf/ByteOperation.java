package org.netty.demo.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;
import java.util.Random;

/**
 * Created by XiuYin.Cui on 2018/6/21.
 */
public class ByteOperation {

    private ByteBuf byteBuf = null;


    public void test(){

        //遍历ByteBuf
        //使用那些需要一个索引值参数的方法来访问数据既不会改变readerIndex的值，也不会改变winterIndex的值。
        //可以通过调用 readerIndex(index)或者 writerIndex(index)来手动移动这两者
        for (int i = 0; i < byteBuf.capacity(); i++){
            byte aByte = byteBuf.getByte(i);
            System.out.println(aByte);
        }
        //读取所有数据
        while (byteBuf.isReadable()){
            System.out.println(byteBuf.readByte());
        }
        //写数据
        Random random = new Random();
        while (byteBuf.writableBytes() >= 4){
            byteBuf.writeInt(random.nextInt());
        }


    }

    public static void main(String[] args) {
        //对ByteBuf进行切片
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        //创建该ByteBuf从索引0开始到索引15结束的一个新切片
        ByteBuf slice = buf.slice(0, 15);
        System.out.println(slice.toString(utf8));
        //更新索引0处的字节
        buf.setByte(0, (byte)'J');
        byte aByte = slice.getByte(0);
        System.out.println(aByte);

        //复制一个ByteBuf
        ByteBuf copy = buf.copy(0, 15);
        System.out.println(copy.toString(utf8));
        buf.setByte(0,(byte)'A');
        System.out.println(copy.getByte(0));

        System.out.println("-------------------读写操作（get、set）------------------");
        //打印第一个字符
        System.out.println(buf.getByte(0));
        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();
        System.out.println(readerIndex);
        System.out.println(writerIndex);
        //将索引0处的字符更新为'B'
        buf.setByte(0, (byte)'B');
        System.out.println(buf.readerIndex());
        System.out.println(buf.writerIndex());

        System.out.println("-----------------读写操作（read，write）----------------");
        int readerIndex1 = buf.readerIndex();
        int writerIndex1 = buf.writerIndex();
        System.out.println(readerIndex1);
        System.out.println(writerIndex1);
        //打印第一个字符
        System.out.println(buf.readByte());
        buf.writeByte((byte)'?');
        System.out.println(buf.toString(utf8));
        System.out.println(buf.readerIndex());
        System.out.println(buf.writerIndex());

        System.out.println("如果至少有一个字节可读取：" + buf.isReadable());
        System.out.println("如果至少有一个字节可写入：" + buf.isWritable());
        System.out.println("返回可被读取的字节数：" + buf.readableBytes());
        System.out.println("返回可被写入的字节数：" + buf.writableBytes());
        System.out.println("可容纳的字节数：" + buf.capacity() + ",可扩展最大的字节数：" + buf.maxCapacity());
        System.out.println("是否由一个字节数组支撑：" + buf.hasArray());
        System.out.println("由一个字节数组支撑则返回该数组：" + buf.array().length);
        System.out.println("计算第一个字节的偏移量：" + buf.arrayOffset());
        System.out.println("返回Bytebuf的十六进制：" + ByteBufUtil.hexDump(buf.array()));


        System.out.println("--------------------------------");
        Unpooled.buffer();
    }

}
