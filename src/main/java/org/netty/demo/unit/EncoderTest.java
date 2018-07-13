package org.netty.demo.unit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by XiuYin.Cui on 2018/7/13.
 */
public class EncoderTest {

    @Test
    public void encoderTest(){
        ByteBuf buf = Unpooled.buffer();
        for (int i =1; i < 10; i++){
            buf.writeInt(i * -1);
        }
        //1、创建一个EmbeddedChannel 并安装要测试的Encoder
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new Encoder());
        //2、写入数据
        assertTrue(embeddedChannel.writeOutbound(buf));
        assertTrue(embeddedChannel.finish());
        //3、读取数据
        for (int i = 1; i < 10; i++){
            Object o = embeddedChannel.readOutbound();
            System.out.println(o);
        }
        assertNull(embeddedChannel.readOutbound());
    }
}
