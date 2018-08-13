package org.netty.demo.encoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Created by XiuYin.Cui on 2018/8/12.
 */
public class IntegerToStringEncoder extends MessageToMessageEncoder<Integer> {


    @Override
    protected void encode(ChannelHandlerContext ctx, Integer msg, List<Object> out) throws Exception {
        out.add(String.valueOf(msg));
    }
}
