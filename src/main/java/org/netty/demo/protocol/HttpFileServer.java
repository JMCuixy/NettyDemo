package org.netty.demo.protocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedFile;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * Created by XiuYin.Cui on 2018/8/28.
 */
public class HttpFileServer {

    public void start(int port) throws Exception {

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup child = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, child)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .childHandler(new HttpFileServerInitializer());
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            child.shutdownGracefully();
        }
    }


    public static final class HttpFileServerInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) {
            ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
            ch.pipeline().addLast(new IdleStateHandler(0, 0, 30, TimeUnit.SECONDS));
            ch.pipeline().addLast(new ChannelHandlerAdapter() {
                @Override
                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                    ByteBuf buffer = (ByteBuf) msg;
                    byte[] readmsg = new byte[buffer.readableBytes()];
                    buffer.readBytes(readmsg);
                    System.out.println("来自" + ctx.channel().remoteAddress() + "的原始请求:" + new String(readmsg, Charset.defaultCharset()));
                    buffer.readerIndex(0);
                    super.channelRead(ctx, msg);
                }

                @Override
                public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                    ByteBuf buffer = (ByteBuf) msg;
                    byte[] writemsg = new byte[buffer.readableBytes()];
                    buffer.readBytes(writemsg);
                    System.out.println("来自" + ctx.channel().remoteAddress() + "的响应:" + new String(writemsg, Charset.defaultCharset()));
                    buffer.readerIndex(0);
                    super.write(ctx, msg, promise);
                }
            });
            ch.pipeline().addLast(new HttpServerCodec());
            ch.pipeline().addLast(new HttpObjectAggregator(65536));
            ch.pipeline().addLast(new HttpContentCompressor());
            ch.pipeline().addLast(new ChunkedWriteHandler());
            ch.pipeline().addLast(new HttpFileServerHandler());
        }
    }

    public static final class HttpFileServerHandler extends ChannelHandlerAdapter {

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent stateEvent = (IdleStateEvent) evt;
                if (stateEvent.state() == IdleState.ALL_IDLE) {
                    System.out.println("客户端长时间没发起请求，即将断开连接[" + ctx.channel().remoteAddress() + "]");
                    ctx.close();
                }
            } else {
                super.userEventTriggered(ctx, evt);
            }
        }

        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println("断开连接[" + ctx.channel().remoteAddress() + "]");
            super.channelUnregistered(ctx);
        }

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println("建立连接[" + ctx.channel().remoteAddress() + "]");
            super.channelRegistered(ctx);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            FullHttpRequest request = (FullHttpRequest) msg;
            if (!request.decoderResult().isSuccess()) {
                sendError(ctx, HttpResponseStatus.BAD_REQUEST, Unpooled.wrappedBuffer("BAD REQUEST".getBytes(Charset.defaultCharset())), true);
                return;
            }
            if ("/favicon.ico".equals(request.uri())) {
                sendError(ctx, HttpResponseStatus.OK, Unpooled.buffer(0), false);
                return;
            }
            if ("/9325945.png".equals(request.uri())) {
                String filePath = "E:\\9325945.png";
                File file = new File(filePath);
                //随机读取文件类
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
                //获取文件长度
                long length = randomAccessFile.length();
                //建立响应对象
                DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                ctx.writeAndFlush(response);
                //构造发送文件线程，将文件写入 Chunked 缓冲区中
                ctx.writeAndFlush(new ChunkedFile(randomAccessFile, 0, length, 8192), ctx.newProgressivePromise());
                //如果使用Chunked编码，最后则需要发送一个编码结束的看空消息体，进行标记，表示所有消息体已经成功发送完成。
                ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);


/*                FileInputStream fileInputStream = new FileInputStream(file);
                FileChannel channel = fileInputStream.getChannel();
                //以该文件的完整长度创建一个新的 DefaultFileRegion
                DefaultFileRegion defaultFileRegion = new DefaultFileRegion(channel, 0, file.length());

                response.headers().set(HttpHeaderNames.CONTENT_DISPOSITION, "attachment;filename=9325945.png");
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "image/png");
                ctx.writeAndFlush(defaultFileRegion);
                ctx.writeAndFlush(response);*/
                return;
            }

            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer("Hello".getBytes(Charset.defaultCharset())));
            response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
            boolean keepAlive = HttpHeaderUtil.isKeepAlive(request);
            if (!keepAlive) {
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                ctx.writeAndFlush(response);
            }
        }

        private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status, ByteBuf content, boolean isClose) {
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN);
            if (isClose) {
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                ctx.writeAndFlush(response);
            }
        }
    }


    public static void main(String[] args) throws Exception {
        new HttpFileServer().start(9999);
    }

}
