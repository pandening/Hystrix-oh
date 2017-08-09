package com.hujian.hystrix.ooh.common;

import com.netflix.hystrix.HystrixCommand;

/**
 * Created by hujian06 on 2017/8/9.
 */
public abstract class AbstractHystrixCommand<T> extends HystrixCommand<T> {

    private HystrixCommandConfig hystrixCommandConfig;

    public AbstractHystrixCommand(HystrixCommandConfig hystrixCommandConfig) {
        super(hystrixCommandConfig.builderCommandSetter());
        this.hystrixCommandConfig = hystrixCommandConfig;
    }

    @Override
    protected T run() throws Exception {
        return realRun();
    }

    @Override
    protected T getFallback() {
        return fallback();
    }

    //you should implement this method
    protected abstract T realRun ();

    //the fall back method
    protected abstract T fallback ();

    public HystrixCommandConfig getHystrixCommandConfig() {
        return hystrixCommandConfig;
    }

    public void setHystrixCommandConfig(HystrixCommandConfig hystrixCommandConfig) {
        this.hystrixCommandConfig = hystrixCommandConfig;
    }
}
