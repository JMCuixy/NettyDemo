package org.netty.demo.protocol.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * Created by XiuYin.Cui on 2018/8/26.
 */
public class HttpAggregatorInitializer extends ChannelInitializer<Channel> {
    private final SslContext sslContext;
    private final Boolean isClient;

    public HttpAggregatorInitializer(SslContext sslContext, Boolean isClient) {
        this.sslContext = sslContext;
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        SSLEngine sslEngine = sslContext.newEngine(ch.alloc());
        if (isClient) {
            //使用 HTTPS，添加 SSL 认证
            pipeline.addFirst("ssl", new SslHandler(sslEngine, true));
            pipeline.addLast("codec", new HttpClientCodec());
            //1、建议开启压缩功能以尽可能多地减少传输数据的大小
            //2、客户端处理来自服务器的压缩内容
            pipeline.addLast("decompressor", new HttpContentDecompressor());
        }else {
            pipeline.addFirst("ssl", new SslHandler(sslEngine));
            //HttpServerCodec:将HTTP客户端请求转成HttpRequest对象，将HttpResponse对象编码成HTTP响应发送给客户端。
            pipeline.addLast("codec", new HttpServerCodec());
            //服务端，压缩数据
            pipeline.addLast("compressor", new HttpContentCompressor());
        }
        //目的多个消息转换为一个单一的FullHttpRequest或是FullHttpResponse
        //将最大的消息为 512KB 的HttpObjectAggregator 添加到 ChannelPipeline
        //在消息大于这个之后会抛出一个 TooLongFrameException 异常。
        pipeline.addLast("aggregator", new HttpObjectAggregator(512 * 1024));

    }
}
