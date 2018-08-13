package org.netty.demo.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * Created by XiuYin.Cui on 2018/8/12.
 *
 * Void 代表不需要进行状态管理
 */
public class MyReplayingDecoder extends ReplayingDecoder<Void> {

    /**
     * ReplayingDecoder 扩展了 ByteToMessageDecoder。
     *  ReplayingDecoder 自定义了 ByteBuf 实现 ReplayingDecoderByteBuf，对要转换的消息的字节数
     * 进行内部管理，如果没有足够的字节使用，将会抛出一个 Signal，由ReplayingDecoder进行处理。
     *
     * @param channelHandlerContext
     * @param byteBuf 传入的 ByteBuf 实际上是 ReplayingDecoderByteBuf
     * @param list
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
       list.add(byteBuf.readInt());
    }
}
