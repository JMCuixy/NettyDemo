package org.netty.demo.unit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;
import org.netty.demo.echo.EchoClientHandler;
import java.nio.charset.Charset;


/**
 * Created by XiuYin.Cui on 2018/7/13.
 */
public class EchoClientHandlerTest {

    @Test
    public void echoClientHandlerTest(){

        // 1、创建 EmbeddedChannel，并加入要测试ChannelHandler
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new EchoClientHandler());
        //2、写入入站数据，验证入站逻辑
        ByteBuf buf = Unpooled.copiedBuffer("Test EchoHandler input", Charset.forName("utf-8"));
        embeddedChannel.writeInbound(buf);
        if (buf.refCnt() != 0){
            buf.release();
        }
        //3、写入出站数据，验证出站逻辑
        ByteBuf buf1 = Unpooled.copiedBuffer("Test EchoHandler output", Charset.forName("utf-8"));
        embeddedChannel.writeOutbound(buf1);
        if (buf1.refCnt() != 0){
            buf1.release();
        }
    }
}
