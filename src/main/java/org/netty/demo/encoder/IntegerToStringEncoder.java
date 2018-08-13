package org.netty.demo.encoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Created by XiuYin.Cui on 2018/8/12.
 */
public class IntegerToStringEncoder extends MessageToMessageEncoder<Integer> {

    /**
     * 1、类型为 I 的出站消息被编码为目标类型 存入List 中
     * 2、该 List 随后将会被转发给 ChannelPipeline中的下一个 ChannelOutboundHandler。
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Integer msg, List<Object> out) throws Exception {
        out.add(String.valueOf(msg));
    }
}
