package org.netty.demo.protocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedStream;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by XiuYin.Cui on 2018/8/27.
 */
public class ChunkedWriteHandlerInitializer extends ChannelInitializer<Channel> {
    private final File file;
    private final SslContext sslCtx;

    public ChunkedWriteHandlerInitializer(File file, SslContext sslCtx) {
        this.file = file;
        this.sslCtx = sslCtx;
    }
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(
                new SslHandler(sslCtx.newEngine(ch.alloc())),
                // 添加 ChunkedWriteHandler 以处理作为 ChunkedInput 传入的数据
                new ChunkedWriteHandler(),
                new WriteStreamHandler()
        );
    }
    private final class WriteStreamHandler extends ChannelHandlerAdapter {
        //当连接建立时，channelActive() 方法将使用 ChunkedInput 写文件数据
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            ctx.writeAndFlush(new ChunkedStream(new FileInputStream(file)));
        }
    }
}
