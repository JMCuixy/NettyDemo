package org.netty.demo.chatroom;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by XiuYin.Cui on 2018/9/5.
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final String wsUri;
    private static File INDEX;

    static {
        URL location = HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            String path = location.toURI() + "index.html";
            path = !path.contains("file:") ? path : path.substring(5);
            INDEX = new File(path);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }


    @Override
    protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        // 如果请求了Websocket，协议升级，增加引用计数(调用retain())，并将他传递给下一个 ChannelHandler
        // 之所以需要调用 retain() 方法，是因为调用 channelRead() 之后，资源会被 release() 方法释放掉，需要调用 retain() 保留资源
        if (wsUri.equalsIgnoreCase(request.uri())) {
            ctx.fireChannelRead(request.retain());
        } else {
            //处理 100 Continue 请求以符合 HTTP 1.1 规范
            if (HttpHeaderUtil.is100ContinueExpected(request)) {
                send100Continue(ctx);
            }
            // 读取 index.html
            RandomAccessFile randomAccessFile = new RandomAccessFile(INDEX, "r");
            HttpResponse response = new DefaultHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);
            HttpHeaders headers = response.headers();
            //在该 HTTP 头信息被设置以后，HttpRequestHandler 将会写回一个 HttpResponse 给客户端
            headers.set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
            boolean keepAlive = HttpHeaderUtil.isKeepAlive(request);
            if (keepAlive) {
                headers.setLong(HttpHeaderNames.CONTENT_LENGTH, randomAccessFile.length());
                headers.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }
            ctx.write(response);
            //将 index.html 写给客户端
            if (ctx.pipeline().get(SslHandler.class) == null) {
                ctx.write(new DefaultFileRegion(randomAccessFile.getChannel(), 0, randomAccessFile.length()));
            } else {
                ctx.write(new ChunkedNioFile(randomAccessFile.getChannel()));
            }
            //写 LastHttpContent 并冲刷至客户端，标记响应的结束
            ChannelFuture channelFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!keepAlive) {
                channelFuture.addListener(ChannelFutureListener.CLOSE);
            }
        }

    }

    private void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
