package org.netty.demo.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * Created by XiuYin.Cui on 2018/8/26.
 */
public class CmdHandlerInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(
                new CmdDecoder(64 * 1024),
                new CmdHandler()
        );

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

    /**
     * 解码成 Cmd 对象
     */
    public static final class CmdDecoder extends LineBasedFrameDecoder {

        private byte SPACE = ' ';

        public CmdDecoder(int maxLength) {
            super(maxLength);
        }

        @Override
        protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
            //1、提取由行尾符序列分隔的桢
            ByteBuf frame = (ByteBuf) super.decode(ctx, buffer);
            //如果输入没有桢，则返回null
            if (frame == null) {
                return null;
            }
            int index = frame.indexOf(frame.readerIndex(), frame.writerIndex(), SPACE);
            return new Cmd(frame.slice(frame.readerIndex(), index), frame.slice(index + 1, frame.writerIndex()));
        }
    }

    /**
     * 业务ChannelHandler
     */
    public static final class CmdHandler extends SimpleChannelInboundHandler<Cmd> {

        @Override
        protected void messageReceived(ChannelHandlerContext ctx, Cmd msg) throws Exception {
            //Do something with Cmd
        }
    }
}
