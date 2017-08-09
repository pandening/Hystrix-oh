package com.hujian.hystrix.ooh.common;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;

/**
 * Created by hujian06 on 2017/8/9.
 */
public abstract class AbstractHystrixObservableCommand<T> extends HystrixObservableCommand<T> {

    private String groupKey;

    public AbstractHystrixObservableCommand(String groupKey) {
        super(HystrixCommandGroupKey.Factory.asKey(groupKey));
        this.groupKey = groupKey;
    }

    @Override
    protected  Observable<T> construct(){
        //return Observable.error(new UnsupportedOperationException("No construct available."));
        return run();
    }

    @Override
    protected Observable<T> resumeWithFallback() {
        //return Observable.error(new UnsupportedOperationException("No fallback available."));
        return fallback();
    }

    protected abstract Observable<T> run();
    protected abstract Observable<T> fallback();

}
