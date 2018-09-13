package org.netty.demo.udp;

import java.net.InetSocketAddress;

/**
 * Created by XiuYin.Cui on 2018/9/10.
 */
public class LogEvent {
    public static final byte SEPARATOR = ':';
    /**
     * IP套接字地址（IP地址+端口号）
     */
    private final InetSocketAddress inetSocketAddress;
    /**
     * 文件名
     */
    private final String logfile;
    /**
     * 消息内容
     */
    private final String msg;

    private final long received;

    /**
     * 用于传入消息的构造函数
     *
     * @param inetSocketAddress
     * @param logfile
     * @param msg
     * @param received
     */
    public LogEvent(InetSocketAddress inetSocketAddress, String logfile, String msg, long received) {
        this.inetSocketAddress = inetSocketAddress;
        this.logfile = logfile;
        this.msg = msg;
        this.received = received;
    }

    /**
     * 用于传出消息的构造函数
     *
     * @param logfile
     * @param msg
     */
    public LogEvent(String logfile, String msg) {
        this(null, logfile, msg, -1);
    }

    public InetSocketAddress getInetSocketAddress() {
        return inetSocketAddress;
    }

    public String getLogfile() {
        return logfile;
    }

    public String getMsg() {
        return msg;
    }

    public long getReceived() {
        return received;
    }
}
