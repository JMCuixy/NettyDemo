package org.netty.demo.eventloop;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by XiuYin.Cui on 2018/7/10.
 */
public class ScheduleExecutor {

    public static void main(String[] args) throws InterruptedException {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("10 seconds later");
            }
        }, 10, TimeUnit.SECONDS);
        Thread.sleep(15000);
        executorService.shutdown();
    }

}
