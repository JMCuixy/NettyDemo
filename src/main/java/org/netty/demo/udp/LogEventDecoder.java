package org.netty.demo.udp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by XiuYin.Cui on 2018/9/12.
 */
public class LogEventDecoder extends MessageToMessageDecoder<DatagramPacket> {


    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {
        // 获取到 DatagramPacket 中的数据（ByteBuf）的引用
        ByteBuf content = msg.content();
        // 获取到该 SEPARATOR 的索引
        int idx = content.indexOf(0, content.readableBytes(), LogEvent.SEPARATOR);
        // 提取文件名
        String fileName = content.slice(0, idx).toString(CharsetUtil.UTF_8);
        //提取日志信息
        String logMsg = content.slice(idx + 1, content.readableBytes()).toString(CharsetUtil.UTF_8);
        //构建一个新的 LogEvent 对象，并且将它添加到（已经解码的消息）列表中
        LogEvent logEvent = new LogEvent(msg.sender(), fileName, logMsg, System.currentTimeMillis());
        out.add(logEvent);
    }
}
