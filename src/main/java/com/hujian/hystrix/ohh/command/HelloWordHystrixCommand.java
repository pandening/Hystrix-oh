package com.hujian.hystrix.ohh.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by hujian06 on 2017/8/9.
 */
public class HelloWordHystrixCommand extends HystrixCommand<String>{

    private String value;
    private String groupKey;
    private Long sleep;

    private static final String GROUP_KEY = "helloHystrix";
    private static final int threadCoreSize = Runtime.getRuntime().availableProcessors();

    public HelloWordHystrixCommand(String value, String groupKey, long sleep) {
        //super(HystrixCommandGroupKey.Factory.asKey(groupKey));
        super(Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey(GROUP_KEY))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(GROUP_KEY + "-thread-poll-key"))
                .andThreadPoolPropertiesDefaults( //set thread pool
                        HystrixThreadPoolProperties.Setter()
                        .withCoreSize(threadCoreSize)  //the thread pool's core thread size
                )
        );
        this.value = value;
        this.groupKey = groupKey;
        this.sleep = sleep;
    }

    @Override
    protected String run() throws Exception {
        TimeUnit.SECONDS.sleep(sleep);
        if ("egg".equals(value)) {
            sleep = sleep/sleep; //do egg... [0/0]
        }
        return "from run()/ groupKey = " + groupKey + "value = " + value;
    }

    @Override
    protected String getFallback() {
        return "from fallBack:" + value;
    }


    public static void main(String ... args) throws InterruptedException, ExecutionException, TimeoutException {

        //sync call
        String r1 =
                new HelloWordHystrixCommand("hujian", GROUP_KEY, 0).execute();
        String r2 =
                new HelloWordHystrixCommand("egg", GROUP_KEY, 0).execute();

        System.out.println("sync test: r1 = {" + r1 + "} r2 = {" + r2 + "}");

        //async test
        Future<String> r3 =
                new HelloWordHystrixCommand("hujian", GROUP_KEY, 1).queue();
        Future<String> r4 =
                new HelloWordHystrixCommand("hujian", GROUP_KEY, 2).queue();

        String rr3 = r3.get(3, TimeUnit.SECONDS);
        String rr4 = r4.get(2, TimeUnit.SECONDS);

        System.out.println("async test: r3 = {" + rr3 + "} r4 = {" + rr4 + "}");

    }

}
