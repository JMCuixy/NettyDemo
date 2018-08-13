package org.netty.demo.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * Created by XiuYin.Cui on 2018/8/12.
 */
public class MyDecoder extends ByteToMessageDecoder {

    private static final int MAX_FRAME_SIZE = 1024;

    /**
     * 1、该方法被调用时，将会传入一个包含了传入数据的 ByteBuf，以及一个用来添加解码消息的 List.
     * 2、对该方法的调用将会重复进行，直到确定没有新的元素被添加到该 List，或者Butebuf 没有更多可读取的字节为止。
     * 3、List 的内容将会被传递给 ChannelPipeline 中的下一个 ChannelInboundHandler。
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int readableBytes = byteBuf.readableBytes();
        //不能让解码器缓冲大量的数据以致于耗尽可用的内存
        if (readableBytes > MAX_FRAME_SIZE){
            //跳过所有的可读字节
            byteBuf.skipBytes(readableBytes);
            throw new TooLongFrameException("数据超过可缓存字节...");
        }
        //假设需要解析 int 类型的消息（int 4个字节）
        if  (readableBytes > 4){
            list.add(byteBuf.readInt());
        }

    }
}
