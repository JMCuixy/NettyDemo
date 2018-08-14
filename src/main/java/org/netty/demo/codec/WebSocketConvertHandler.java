package org.netty.demo.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.*;

import java.util.List;

/**
 * Created by XiuYin.Cui on 2018/8/13.
 */
public class WebSocketConvertHandler extends MessageToMessageCodec<WebSocketFrame, WebSocketConvertHandler.MyWebSocketFrame> {

    /**
     * 1、对于每个 OUTBOUND_IN 类型的消息，这个方法都会被调用。
     * 2、这个消息将会被编码为 INBOUND_IN 类型的消息。
     * 3、然后被转发给 ChannelPipeline 中的下一个 ChannelOutboundHandler
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, MyWebSocketFrame msg, List<Object> out) throws Exception {
        ByteBuf byteBuf = msg.getByteBuf().duplicate().retain();
        switch (msg.getFrameType()) {
            case BINARY:
                out.add(new BinaryWebSocketFrame(byteBuf));
                break;
            case TEXT:
                out.add(new TextWebSocketFrame(byteBuf));
                break;
            case CLOSE:
                out.add(new CloseWebSocketFrame(true, 0, byteBuf));
                break;
            case CONTINUATION:
                out.add(new ContinuationWebSocketFrame(byteBuf));
                break;
            case PONG:
                out.add(new PongWebSocketFrame(byteBuf));
                break;
            case PING:
                out.add(new PingWebSocketFrame(byteBuf));
            default:
                break;
        }


    }

    /**
     * 1、传入 INBOUND_IN 类型的消息，该方法会被调用。
     * 2、这个消息会被解码为 OUTBOUND_IN 类型的消息。
     * 3、然后被转发给 ChannelPipeline 中的下一个 ChannelInboundHandler
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
        ByteBuf byteBuf = msg.content().duplicate().retain();
        if (msg instanceof BinaryWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.BINARY, byteBuf));
        } else if (msg instanceof CloseWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.CLOSE, byteBuf));
        } else if (msg instanceof TextWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.TEXT, byteBuf));
        } else if (msg instanceof PingWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.PING, byteBuf));
        } else if (msg instanceof PongWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.PONG, byteBuf));
        } else if (msg instanceof ContinuationWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.CONTINUATION, byteBuf));
        } else {
            throw new IllegalStateException("Unsupported websocket msg " + msg);
        }

    }


    public static final class MyWebSocketFrame {

        public enum FrameType {
            BINARY,
            CLOSE,
            PING,
            PONG,
            TEXT,
            CONTINUATION
        }

        private final FrameType frameType;
        private final ByteBuf byteBuf;

        public MyWebSocketFrame(FrameType frameType, ByteBuf byteBuf) {
            this.frameType = frameType;
            this.byteBuf = byteBuf;
        }

        public FrameType getFrameType() {
            return frameType;
        }

        public ByteBuf getByteBuf() {
            return byteBuf;
        }
    }
}
