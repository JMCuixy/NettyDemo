package org.netty.demo.unit;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

/**
 * 这个特定的解码器将产生固定为3字节大小的帧
 */
public class Decoder extends ByteToMessageDecoder {

    private final Logger logger = LoggerFactory.getLogger(Decoder.class);

    private final int length;

    public Decoder(int length) throws IllegalAccessException {
        if (length <=0 ){
           logger.error("长度必须大于0");
           throw new IllegalAccessException("");
        }
        this.length = length;
    }


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        while (in.readableBytes() >= length){
            //重新生成一个三字节的帧并将它提交到消息列表中
            ByteBuf buf = in.readBytes(length);
            out.add(buf);
        }
    }
}
