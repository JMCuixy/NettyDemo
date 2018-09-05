package org.netty.demo.eventloop;


import io.netty.channel.Channel;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.concurrent.TimeUnit;

/**
 * Created by XiuYin.Cui on 2018/7/10.
 */
public class EventLoopSchedule {

    public static void main(String[] args) {
        Channel channel = null;
        //10 秒后延迟执行
        channel.eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("10 seconds later");
            }
        }, 10, TimeUnit.SECONDS);

        //每隔3秒执行一次
        ScheduledFuture<?> schedule = channel.eventLoop().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("Run every 3 seconds");
            }
        }, 0, 3, TimeUnit.SECONDS);
        //取消该任务
        schedule.cancel(false);

    }
}
