package org.netty.demo.unit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;


import static org.junit.Assert.*;

/**
 * Created by XiuYin.Cui on 2018/7/12.
 * 测试入站消息
 */
public class DecoderTest {


    //1、利用Junit执行单元测试
    @Test
    public void decoderTest() throws IllegalAccessException {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf buf1 = buf.duplicate();
        //2、创建EmbeddedChannel，并添加一个Decoder（我们的要测试 ChannelHandler） 其将以3字节帧长度被测试
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new Decoder(3));
        //3、将数据写入 EmbeddedChannel
        boolean writeInbound = embeddedChannel.writeInbound(buf1.retain());
        assertTrue(writeInbound);
        //4、标记 Channel 为已完成状态
        boolean finish = embeddedChannel.finish();
        assertTrue(finish);

        //5、读取数据
        ByteBuf readInbound =  embeddedChannel.readInbound();
        ByteBuf readSlice = buf.readSlice(3);
        assertEquals(readInbound, readSlice);
        readInbound.release();

        readInbound =  embeddedChannel.readInbound();
        readSlice = buf.readSlice(3);
        assertEquals(readInbound, readSlice);
        readInbound.release();

        readInbound =  embeddedChannel.readInbound();
        readSlice = buf.readSlice(3);
        assertEquals(readInbound, readSlice);
        readInbound.release();

        //是否读取完数据了
        assertNull(embeddedChannel.readInbound());
        //释放资源
        buf.release();

    }

    @Test
    public void decoderTest2() throws IllegalAccessException {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++){
            buf.writeByte(i);
        }
        ByteBuf duplicate = buf.duplicate();
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new Decoder(3));
        assertFalse(embeddedChannel.writeInbound(duplicate.readBytes(2)));
        assertTrue(embeddedChannel.writeInbound(duplicate.readBytes(7)));
        assertTrue(embeddedChannel.finish());
        ByteBuf readInbound = embeddedChannel.readInbound();
        assertEquals(buf.readSlice(3), readInbound);
        readInbound.release();

        readInbound = embeddedChannel.readInbound();
        assertEquals(buf.readSlice(3), readInbound);
        readInbound.release();

        readInbound = embeddedChannel.readInbound();
        assertEquals(buf.readSlice(3), readInbound);
        readInbound.release();

        buf.release();
    }

}
