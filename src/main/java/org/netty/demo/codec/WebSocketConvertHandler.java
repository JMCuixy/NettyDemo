package org.netty.demo.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.util.List;

/**
 * Created by XiuYin.Cui on 2018/8/13.
 */
public class WebSocketConvertHandler extends MessageToMessageCodec<WebSocketFrame, WebSocketConvertHandler.MyWebSocketFrame> {

    private final MyWebSocketFrame.FrameType type;

    private final ByteBuf byteBuf;

    public WebSocketConvertHandler(MyWebSocketFrame.FrameType type, ByteBuf byteBuf) {
        this.type = type;
        this.byteBuf = byteBuf;
    }

    public MyWebSocketFrame.FrameType getType() {
        return type;
    }

    public ByteBuf getByteBuf() {
        return byteBuf;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, MyWebSocketFrame msg, List<Object> out) throws Exception {
        
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {

    }


    public static final class MyWebSocketFrame{
        public enum FrameType {
            BINARY,
            CLOSE,
            PING,
            PONG,
            TEXT,
            CONTINUATION
        }
    }
}
