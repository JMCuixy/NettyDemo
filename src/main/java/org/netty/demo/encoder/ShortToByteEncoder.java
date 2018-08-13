package org.netty.demo.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by XiuYin.Cui on 2018/8/12.
 */
public class ShortToByteEncoder extends MessageToByteEncoder<Short> {


    /**
     * 它被调用时将会传入要被该类编码为 ByteBuf 的（类型为 I 的）出站消息。
     * 该 ByteBuf 随后将会被转发给 ChannelPipeline中的下一个 ChannelOutboundHandler。
     * @param channelHandlerContext
     * @param aShort
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Short aShort, ByteBuf byteBuf) throws Exception {
        byteBuf.writeShort(aShort);
    }
}
