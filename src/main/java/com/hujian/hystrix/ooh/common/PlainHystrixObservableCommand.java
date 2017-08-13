package com.hujian.hystrix.ooh.common;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;

/**
 * Created by hujian06 on 2017/8/13.
 */
public abstract class PlainHystrixObservableCommand<T> extends HystrixObservableCommand<T> {
    
    protected PlainHystrixObservableCommand(HystrixCommandGroupKey group) {
        super(group);
    }

    protected PlainHystrixObservableCommand(Setter setter) {
        super(setter);
    }

    @Override
    protected Observable<T> construct() {
        return run();
    }

    @Override
    protected Observable<T> resumeWithFallback() {
        return fallback();
    }

    protected abstract Observable<T> run();
    protected abstract Observable<T> fallback();
}
