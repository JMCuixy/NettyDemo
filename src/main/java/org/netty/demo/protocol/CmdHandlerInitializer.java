package org.netty.demo.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * Created by XiuYin.Cui on 2018/8/26.
 */
public class CmdHandlerInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {

    }

    /**
     * Cmd POJO
     */
    public static final class Cmd {
        private final ByteBuf name;
        private final ByteBuf args;

        public Cmd(ByteBuf name, ByteBuf args) {
            this.name = name;
            this.args = args;
        }

        public ByteBuf getName() {
            return name;
        }

        public ByteBuf getArgs() {
            return args;
        }
    }

    public static final class CmdDecoder extends LineBasedFrameDecoder {

        private byte SPACE = ' ';

        public CmdDecoder(int maxLength) {
            super(maxLength);
        }

        @Override
        protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
            //1、提取由行尾符序列分隔的桢
            ByteBuf frame =(ByteBuf) super.decode(ctx, buffer);
            //如果输入没有桢，则返回null
            if (frame == null){
                return null;
            }
            frame.indexOf(frame.readerIndex(), frame.writerIndex(), SPACE);
        }
    }
}
