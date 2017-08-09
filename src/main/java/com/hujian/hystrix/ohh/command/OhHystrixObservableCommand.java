package com.hujian.hystrix.ohh.command;

import com.hujian.hystrix.ooh.common.AbstractHystrixObservableCommand;
import rx.Observable;
import rx.Subscriber;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by hujian06 on 2017/8/9.
 */
public class OhHystrixObservableCommand extends AbstractHystrixObservableCommand<String> {

    private String groupKey;
    private String key;
    private int extraSleepMs;

    private Map<String, String> dict = new ConcurrentHashMap<String, String>();

    public OhHystrixObservableCommand(String groupKey, String key, int extraSleepMs) {
        super(groupKey);
        this.groupKey = groupKey;
        this.key = key;
        this.extraSleepMs = extraSleepMs;
    }

    protected Observable<String> run() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            public void call(Subscriber<? super String> subscriber) {
                System.out.println("from run,hello," + Thread.currentThread().getName() + " with key:" + key);
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
                subscriber.onNext("from run:" + dict.get(key));
            }
        });
    }

    protected Observable<String> fallback() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("from fallback: hello " + Thread.currentThread().getName() + " with " + key);
                subscriber.onCompleted(); //不会往下面继续执行了
                subscriber.onError(new Exception("get value from map error")); //直接抛出异常然后停止往下执行
            }
        });
    }

    public static void main(String ... args) {

        String groupKey = "OhHystrixObservableCommand";

        String v = new OhHystrixObservableCommand(groupKey,"hujian",1000)
                .observe().toBlocking().single();

        System.out.println(v);

        String v1 = new OhHystrixObservableCommand(groupKey,"libai",100)
                .observe().toBlocking().first();

        System.out.println(v1);

    }

}
