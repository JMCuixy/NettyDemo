package org.netty.demo.chatroom;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * Created by XiuYin.Cui on 2018/9/5.
 */
public class ChatServerInitializer extends ChannelInitializer<Channel> {

    private final ChannelGroup group;
    private static final int READ_IDLE_TIME_OUT = 60; // 读超时
    private static final int WRITE_IDLE_TIME_OUT = 0;// 写超时
    private static final int ALL_IDLE_TIME_OUT = 0; // 所有超时

    public ChatServerInitializer(ChannelGroup group) {
        this.group = group;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpObjectAggregator(64 * 1024));
        // 处理那些不发送到 /ws URI的请求
        pipeline.addLast(new HttpRequestHandler("/ws"));
        // 如果被请求的端点是 "/ws",则处理该升级握手
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        // //当连接在60秒内没有接收到消息时，进会触发一个 IdleStateEvent 事件，被 HeartbeatHandler 的 userEventTriggered 方法处理
        pipeline.addLast(new IdleStateHandler(READ_IDLE_TIME_OUT, WRITE_IDLE_TIME_OUT, ALL_IDLE_TIME_OUT, TimeUnit.SECONDS));
        pipeline.addLast(new TextWebSocketFrameHandler(group));

    }
}
