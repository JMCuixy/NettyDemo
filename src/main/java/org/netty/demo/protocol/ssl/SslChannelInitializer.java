package org.netty.demo.protocol.ssl;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * Created by XiuYin.Cui on 2018/8/16.
 */
public class SslChannelInitializer extends ChannelInitializer<Channel> {

    private final SslContext context;
    private final boolean startTls;

    /**
     *
     * @param context
     * @param startTls 如果为true，第一个写入的消息将不会被加密（客户端应该设置为true）
     */
    public SslChannelInitializer(SslContext context, boolean startTls) {
        this.context = context;
        this.startTls = startTls;
    }


    @Override
    protected void initChannel(Channel ch) throws Exception {
        ByteBufAllocator byteBufAllocator = ch.alloc();
        //对于每个 SslHandler 实例，都使用 Channel 的 ByteBufAllocator 从 SslContext 获取一个新的 SSLEngine
        SSLEngine sslEngine = context.newEngine(byteBufAllocator);
        //服务器端模式，客户端模式设置为true
        sslEngine.setUseClientMode(false);
        //不需要验证客户端，客户端不设置该项
        sslEngine.setNeedClientAuth(false);
        //要将 SslHandler 设置为第一个 ChannelHandler。这确保了只有在所有其他的 ChannelHandler 将他们的逻辑应用到数据之后，才会进行加密。
        ch.pipeline().addFirst("ssl",new SslHandler(sslEngine, startTls));
    }
}
