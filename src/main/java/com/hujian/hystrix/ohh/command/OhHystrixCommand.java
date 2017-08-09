package com.hujian.hystrix.ohh.command;

import com.hujian.hystrix.ooh.common.AbstractHystrixCommand;
import com.hujian.hystrix.ooh.common.HystrixCommandConfig;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by hujian06 on 2017/8/9.
 */
public class OhHystrixCommand extends AbstractHystrixCommand<String> {

    private HystrixCommandConfig hystrixCommandConfig;
    private String key;
    private int extraSleepMs;

    Map<String, String> dict = new ConcurrentHashMap<String, String>();

    public OhHystrixCommand(HystrixCommandConfig hystrixCommandConfig, String key) {
        super(hystrixCommandConfig);
        this.hystrixCommandConfig = hystrixCommandConfig;
        this.key = key;
    }

    public OhHystrixCommand(HystrixCommandConfig hystrixCommandConfig, String key, int extraSleepMs) {
        super(hystrixCommandConfig);
        this.hystrixCommandConfig = hystrixCommandConfig;
        this.key = key;
        this.extraSleepMs =extraSleepMs;
    }


    protected String realRun() {
        Random random = new Random();
        int time = random.nextInt(1000) % 1000;
        if (extraSleepMs > 0) {
            time += extraSleepMs;
        }
        try {
            TimeUnit.MILLISECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("get value from map takes " + time + " ms");

        int size = key.length(); // do eggs when key == null

        String value = dict.get(key);
        if (value == null) {
            dict.put(key, key + time);
        }
        return "from run:" + dict.get(key);
    }

    protected String fallback() {
        return "from fallback:" + key;
    }

    public static void main(String ... args) throws InterruptedException, ExecutionException, TimeoutException {
        HystrixCommandConfig hystrixCommandConfig = new HystrixCommandConfig();
        hystrixCommandConfig.setGroupKey("oh-hystrix");

        //设置使用线程隔离时调用超时时间（ms）
        hystrixCommandConfig.setExecutionIsolationThreadTimeoutInMilliseconds(2000L);

        //sync test
        String value = new OhHystrixCommand(hystrixCommandConfig,"hujian").execute();
        System.out.println(value);
        String nValul = new OhHystrixCommand(hystrixCommandConfig,null).execute();
        System.out.println(nValul);

        //async test
        Future<String> avf = new OhHystrixCommand(hystrixCommandConfig, "hujian").queue();
        String av = avf.get();
        System.out.println(av);
        Future<String> avf1 = new OhHystrixCommand(hystrixCommandConfig,"hujian", 3000).queue();
        String av1 = avf1.get();
        System.out.print(av1);

    }

}
