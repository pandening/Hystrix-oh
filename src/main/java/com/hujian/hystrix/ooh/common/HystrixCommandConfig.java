package com.hujian.hystrix.ooh.common;

import com.google.common.base.Preconditions;
import com.netflix.hystrix.HystrixCommand.Setter;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;

/**
 * Created by hujian06 on 2017/8/9.
 */
public class HystrixCommandConfig  {

    private String GroupKey; // group key    +
    private String CommandKey; // command key  -
    private String threadPoolKey; // thread pool key -

    private Integer threadCoreSize; // core size    +
    private Long maxQueueSize; // wait queue size    -

    private Boolean isCircuitBreakerEnabled; // breaker set  + default : true
    private Boolean isBreakerForceOpen;  // force open breaker -  true -> all requests will reject
    private Boolean isBreakerForceClosed; // force close breaker -  false -> ignore all errors
    private Integer circuitBreakerRequestVolumeThreshold; // default : 20 -
    private Long circuitBreakerSleepWindowInMilliseconds; // default : 10 ms -
    private Double circuitBreakerErrorThresholdPercentage; // default: 50% - like 50.0 means 50%
    private Boolean isIsolationStrategyWithSemaphore; // IsolationStrategy -> Semaphore -
    private Boolean isIsolationStrategyWithThread; // default. -
    private Integer executionIsolationSemaphoreMaxConcurrentRequests; // default : 10 -
    private Integer fallbackIsolationSemaphoreMaxConcurrentRequests; // default : 10 -
    private Long executionIsolationThreadTimeoutInMilliseconds; // default : 1s -

    private Boolean isFallbackEnabled; // default : true -
    private Boolean isRequestCacheEnabled; // default : true -

    private Setter hystrixSetter;


    /**
     * convert hystrix command setter to Observable command setter
     * @return
     */
    public HystrixObservableCommand.Setter convertToObservableCommandSetter() {
        HystrixObservableCommand.Setter setter =
                HystrixObservableCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(GroupKey))
                .andCommandKey(HystrixCommandKey.Factory.asKey(CommandKey));
        return setter;
    }

    /**
     * stupid initializer
     * @return the setter
     */
    public Setter builderCommandSetter() {
        Preconditions.checkArgument(GroupKey != null && GroupKey != "");
        if ( this.threadCoreSize == null || this.threadCoreSize <= 0 ) {
            this.threadCoreSize = Runtime.getRuntime().availableProcessors();
        }
        hystrixSetter = Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(this.GroupKey));
        hystrixSetter.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
               .withCoreSize(threadCoreSize)
        );

        if (CommandKey != null) {
            hystrixSetter.andCommandKey(HystrixCommandKey.Factory.asKey(CommandKey));
        }

        if (threadPoolKey != null) {
            hystrixSetter.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(threadPoolKey));
        }

        if (maxQueueSize != null && maxQueueSize.compareTo(threadCoreSize.longValue()) > 0 ) {
            hystrixSetter.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                    .withMaxQueueSize(maxQueueSize.intValue()));
        }

        if (isCircuitBreakerEnabled != null) {
            hystrixSetter.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                    .withCircuitBreakerEnabled(isBreakerForceClosed)
            );
        }

        if (isBreakerForceOpen != null) {
            hystrixSetter.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                    .withCircuitBreakerForceOpen(isBreakerForceOpen)
            );
        }

        if (isBreakerForceClosed != null) {
            hystrixSetter.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                    .withCircuitBreakerForceOpen(isBreakerForceClosed)
            );
        }

        if (circuitBreakerRequestVolumeThreshold != null) {
            hystrixSetter.andCommandPropertiesDefaults(
                    HystrixCommandProperties.Setter()
                    .withCircuitBreakerRequestVolumeThreshold(circuitBreakerRequestVolumeThreshold)
            );
        }

        if (circuitBreakerSleepWindowInMilliseconds != null) {
            hystrixSetter.andCommandPropertiesDefaults(
                    HystrixCommandProperties.Setter()
                    .withCircuitBreakerSleepWindowInMilliseconds(circuitBreakerSleepWindowInMilliseconds.intValue())
            );
        }

        if (circuitBreakerErrorThresholdPercentage != null) {
            hystrixSetter.andCommandPropertiesDefaults(
                    HystrixCommandProperties.Setter()
                    .withCircuitBreakerErrorThresholdPercentage(circuitBreakerErrorThresholdPercentage.intValue())
            );
        }

        if (isIsolationStrategyWithSemaphore != null) {
            hystrixSetter.andCommandPropertiesDefaults(
                    HystrixCommandProperties.Setter()
                    .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
            );
        }

        if (isIsolationStrategyWithThread != null) {
            hystrixSetter.andCommandPropertiesDefaults(
                    HystrixCommandProperties.Setter()
                            .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
            );
        }

        if (executionIsolationSemaphoreMaxConcurrentRequests != null) {
            hystrixSetter.andCommandPropertiesDefaults(
                    HystrixCommandProperties.Setter()
                    .withFallbackIsolationSemaphoreMaxConcurrentRequests
                            (executionIsolationSemaphoreMaxConcurrentRequests)
            );
        }

        if (fallbackIsolationSemaphoreMaxConcurrentRequests != null) {
            hystrixSetter.andCommandPropertiesDefaults(
                    HystrixCommandProperties.Setter()
                    .withFallbackIsolationSemaphoreMaxConcurrentRequests
                            (fallbackIsolationSemaphoreMaxConcurrentRequests)
            );
        }

        if (executionIsolationThreadTimeoutInMilliseconds != null) {
            hystrixSetter.andCommandPropertiesDefaults(
                    HystrixCommandProperties.Setter()
                    .withExecutionIsolationThreadTimeoutInMilliseconds
                            (executionIsolationThreadTimeoutInMilliseconds.intValue())
            );
        }

        if (hystrixSetter == null) {
            hystrixSetter = defaultCommandSetter();
        }

        //you can also edit
        return hystrixSetter;
    }

    /**
     * get a default command setter
     * @return
     */
    public Setter defaultCommandSetter() {
        int coreSize = Runtime.getRuntime().availableProcessors();
        return Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("default"))
                .andThreadPoolPropertiesDefaults( //set thread pool
                        HystrixThreadPoolProperties.Setter()
                                .withCoreSize(coreSize)  //the thread pool's core thread size
                );
    }

    public String getGroupKey() {
        return GroupKey;
    }

    public void setGroupKey(String groupKey) {
        GroupKey = groupKey;
    }

    public String getCommandKey() {
        return CommandKey;
    }

    public void setCommandKey(String commandKey) {
        CommandKey = commandKey;
    }

    public String getThreadPoolKey() {
        return threadPoolKey;
    }

    public void setThreadPoolKey(String threadPoolKey) {
        this.threadPoolKey = threadPoolKey;
    }

    public Integer getThreadCoreSize() {
        return threadCoreSize;
    }

    public void setThreadCoreSize(Integer threadCoreSize) {
        this.threadCoreSize = threadCoreSize;
    }

    public Long getMaxQueueSize() {
        return maxQueueSize;
    }

    public void setMaxQueueSize(Long maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
    }

    public Boolean getCircuitBreakerEnabled() {
        return isCircuitBreakerEnabled;
    }

    public void setCircuitBreakerEnabled(Boolean circuitBreakerEnabled) {
        isCircuitBreakerEnabled = circuitBreakerEnabled;
    }

    public Boolean getBreakerForceOpen() {
        return isBreakerForceOpen;
    }

    public void setBreakerForceOpen(Boolean breakerForceOpen) {
        isBreakerForceOpen = breakerForceOpen;
    }

    public Boolean getBreakerForceClosed() {
        return isBreakerForceClosed;
    }

    public void setBreakerForceClosed(Boolean breakerForceClosed) {
        isBreakerForceClosed = breakerForceClosed;
    }

    public Boolean getIsolationStrategyWithSemaphore() {
        return isIsolationStrategyWithSemaphore;
    }

    public void setIsolationStrategyWithSemaphore(Boolean isolationStrategyWithSemaphore) {
        isIsolationStrategyWithSemaphore = isolationStrategyWithSemaphore;
    }

    public Boolean getIsolationStrategyWithThread() {
        return isIsolationStrategyWithThread;
    }

    public void setIsolationStrategyWithThread(Boolean isolationStrategyWithThread) {
        isIsolationStrategyWithThread = isolationStrategyWithThread;
    }

    public Integer getCircuitBreakerRequestVolumeThreshold() {
        return circuitBreakerRequestVolumeThreshold;
    }

    public void setCircuitBreakerRequestVolumeThreshold(Integer circuitBreakerRequestVolumeThreshold) {
        this.circuitBreakerRequestVolumeThreshold = circuitBreakerRequestVolumeThreshold;
    }

    public Long getCircuitBreakerSleepWindowInMilliseconds() {
        return circuitBreakerSleepWindowInMilliseconds;
    }

    public void setCircuitBreakerSleepWindowInMilliseconds(Long circuitBreakerSleepWindowInMilliseconds) {
        this.circuitBreakerSleepWindowInMilliseconds = circuitBreakerSleepWindowInMilliseconds;
    }

    public Double getCircuitBreakerErrorThresholdPercentage() {
        return circuitBreakerErrorThresholdPercentage;
    }

    public void setCircuitBreakerErrorThresholdPercentage(Double circuitBreakerErrorThresholdPercentage) {
        this.circuitBreakerErrorThresholdPercentage = circuitBreakerErrorThresholdPercentage;
    }

    public Integer getExecutionIsolationSemaphoreMaxConcurrentRequests() {
        return executionIsolationSemaphoreMaxConcurrentRequests;
    }

    public void setExecutionIsolationSemaphoreMaxConcurrentRequests(Integer executionIsolationSemaphoreMaxConcurrentRequests) {
        this.executionIsolationSemaphoreMaxConcurrentRequests = executionIsolationSemaphoreMaxConcurrentRequests;
    }

    public Integer getFallbackIsolationSemaphoreMaxConcurrentRequests() {
        return fallbackIsolationSemaphoreMaxConcurrentRequests;
    }

    public void setFallbackIsolationSemaphoreMaxConcurrentRequests(Integer fallbackIsolationSemaphoreMaxConcurrentRequests) {
        this.fallbackIsolationSemaphoreMaxConcurrentRequests = fallbackIsolationSemaphoreMaxConcurrentRequests;
    }

    public Long getExecutionIsolationThreadTimeoutInMilliseconds() {
        return executionIsolationThreadTimeoutInMilliseconds;
    }

    public void setExecutionIsolationThreadTimeoutInMilliseconds(Long executionIsolationThreadTimeoutInMilliseconds) {
        this.executionIsolationThreadTimeoutInMilliseconds = executionIsolationThreadTimeoutInMilliseconds;
    }

    public Boolean getFallbackEnabled() {
        return isFallbackEnabled;
    }

    public void setFallbackEnabled(Boolean fallbackEnabled) {
        isFallbackEnabled = fallbackEnabled;
    }

    public Boolean getRequestCacheEnabled() {
        return isRequestCacheEnabled;
    }

    public void setRequestCacheEnabled(Boolean requestCacheEnabled) {
        isRequestCacheEnabled = requestCacheEnabled;
    }

    public Setter getHystrixSetter() {
        return hystrixSetter;
    }

    public void setHystrixSetter(Setter hystrixSetter) {
        this.hystrixSetter = hystrixSetter;
    }
}
