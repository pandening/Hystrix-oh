package com.hujian.hystrix.ooh.common;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixThreadPoolKey;

/**
 * Created by hujian06 on 2017/8/13.
 */
public abstract class PlainHystrixCommand<T> extends HystrixCommand<T>{

    /**
     * the recommend construct
     * @param hystrixCommandConfig
     */
    public PlainHystrixCommand(HystrixCommandConfig hystrixCommandConfig) {
        super(hystrixCommandConfig.builderCommandSetter());
    }

    protected PlainHystrixCommand(HystrixCommandGroupKey group) {
        super(group);
    }

    protected PlainHystrixCommand(HystrixCommandGroupKey group, HystrixThreadPoolKey threadPool) {
        super(group, threadPool);
    }

    protected PlainHystrixCommand(HystrixCommandGroupKey group, int executionIsolationThreadTimeoutInMilliseconds) {
        super(group, executionIsolationThreadTimeoutInMilliseconds);
    }

    protected PlainHystrixCommand(HystrixCommandGroupKey group, HystrixThreadPoolKey threadPool, int executionIsolationThreadTimeoutInMilliseconds) {
        super(group, threadPool, executionIsolationThreadTimeoutInMilliseconds);
    }

    protected PlainHystrixCommand(HystrixCommand.Setter setter) {
        super(setter);
    }

    @Override
    protected T run() throws Exception {
        return realRun();
    }

    @Override
    protected T getFallback() {
        return fallback();
    }

    protected abstract T realRun();
    protected abstract T fallback();

}
