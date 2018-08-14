package org.netty.demo.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

/**
 * Created by XiuYin.Cui on 2018/8/12.
 */
public class MyBytetoMessageCodec extends ByteToMessageCodec<Short> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) throws Exception {
        while (in.readableBytes() > 4){
            out.add(in.readInt());
        }
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Short msg, ByteBuf out) throws Exception {
        out.writeShort(msg);
    }
}
