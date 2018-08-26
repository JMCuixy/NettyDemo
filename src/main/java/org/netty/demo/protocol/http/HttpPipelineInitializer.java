package org.netty.demo.protocol.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * Created by XiuYin.Cui on 2018/8/26.
 */
public class HttpPipelineInitializer extends ChannelInitializer<Channel> {

    private final boolean client;

    public HttpPipelineInitializer(boolean client) {
        this.client = client;
    }


    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (client){
            //解码器，处理来自服务端的响应
            pipeline.addLast("decoder",new HttpResponseDecoder());
            //编码器，向服务端发送请求
            pipeline.addLast("encoder", new HttpRequestEncoder());
        }else {
            //解码器，接收来自客户端的请求
            pipeline.addLast("decoder",new HttpRequestDecoder());
            //编码器，以向客户端发出响应
            pipeline.addLast("encoder",new HttpResponseEncoder());
        }

    }
}
