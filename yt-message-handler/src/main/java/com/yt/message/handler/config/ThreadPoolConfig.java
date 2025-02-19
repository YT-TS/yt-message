package com.yt.message.handler.config;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName ThreadPoolConfig
 * @Author Ts
 * @Version 1.0
 */
@Slf4j
public class ThreadPoolConfig {



//    @Bean
//    public ThreadPoolExecutor sendMessageThreadPoolExecutor(){
//        ThreadFactory threadFactory = new ExceptionHandlerThreadFactory((t, e) -> log.error("异步消费异常"+e.getMessage()));
//        return new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TIME_UNIT, new LinkedBlockingQueue<>(),threadFactory, new BlockPolicy());
//    }

    private static class ExceptionHandlerThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;
        private final Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

        ExceptionHandlerThreadFactory(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
            this.uncaughtExceptionHandler = uncaughtExceptionHandler;
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            t.setUncaughtExceptionHandler(uncaughtExceptionHandler);
            return t;
        }
    }


}
