package org.netty.demo.protocol;

import io.netty.channel.*;

import java.io.FileInputStream;
import java.nio.channels.FileChannel;

/**
 * Created by XiuYin.Cui on 2018/8/27.
 */
public class FileRegionHandler extends ChannelHandlerAdapter {


    /**
     * 这个示例只适用于文件内容的直接传输，不包括应用程序对数据的任何处理。在需要将数据
     * 从文件系统复制到用户内存中时，可以使用 ChunkedWriteHandler， 它支持异步写大型数据
     * 流，而又不会导致大量的内存消耗
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String file = "";
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel channel = fileInputStream.getChannel();
        //以该文件的完整长度创建一个新的 DefaultFileRegion
        DefaultFileRegion defaultFileRegion = new DefaultFileRegion(channel, 0, file.length());
        //发送该 DefaultFileRegion， 并注册一个 ChannelFutureListener
        ctx.writeAndFlush(defaultFileRegion).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }
}
