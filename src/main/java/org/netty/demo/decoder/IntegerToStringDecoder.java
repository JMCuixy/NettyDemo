package org.netty.demo.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Created by XiuYin.Cui on 2018/8/12.
 * MessageToMessageEncoder 用于在两种消息格式之间进行转换（从一种 POJO 转换为另一种）
 */
public class IntegerToStringDecoder extends MessageToMessageEncoder<Integer> {

    /**
     * 1、对于每个需要被解码为另一种格式的入站消息来说，该方法将会被调用。
     * 2、解码消息随后会被传递给 ChannelPipeline 中的下一个 ChannelInboundHandler
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Integer integer, List<Object> list) throws Exception {
        list.add(String.valueOf(integer));
    }
}
