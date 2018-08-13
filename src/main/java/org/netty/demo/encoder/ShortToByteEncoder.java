package org.netty.demo.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by XiuYin.Cui on 2018/8/12.
 */
public class ShortToByteEncoder extends MessageToByteEncoder<Short> {

    /**
     * 1、类型为 I 的出站消息被编码为 ByteBuf
     * 2、该 ByteBuf 随后将会被转发给 ChannelPipeline中的下一个 ChannelOutboundHandler。
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Short aShort, ByteBuf byteBuf) throws Exception {
        byteBuf.writeShort(aShort);
    }
}
