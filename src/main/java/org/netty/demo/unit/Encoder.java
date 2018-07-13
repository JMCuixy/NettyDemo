package org.netty.demo.unit;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Created by XiuYin.Cui on 2018/7/13.
 */
public class Encoder extends MessageToMessageEncoder<ByteBuf> {


    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        while (msg.readableBytes() >= 4){
            int abs = Math.abs(msg.readInt());
            out.add(abs);
        }
    }
}
