package com.example.util.taskutil.schedules.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author luowx on 2018/12/27 0027.
 */
@Component
public abstract class AbstractCustomSchedule implements CustomSchedule,Runnable {

    private static final Logger logger = LoggerFactory.getLogger(AbstractCustomSchedule.class);

    private ScheduledFuture<?> future;

    private static Map<String,ScheduledFuture<?>> futures = new ConcurrentHashMap<>();

    public static ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Override
    public boolean saveScheduled(Runnable runnable,String cron) {
        String className = runnable.getClass().getName();
        logger.info("backups scheduled:{}=================,cron :{}" , className,cron);
        stopSchedule(className);
        future = threadPoolTaskScheduler.schedule(runnable,new CronTrigger(cron));
        futures.put(className,future);
        return true;
    }

    @Override
    public boolean stopSchedule(String scheduleName) {
        future = futures.get(scheduleName);
        if(future != null) {
            logger.info("stop schedule :{}",scheduleName);
            future.cancel(true);
        }
        return true;
    }

    @Override
    public boolean stopAllSchedule() {
        Iterator<Map.Entry<String, ScheduledFuture<?>>> entries = futures.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, ScheduledFuture<?>> entry = entries.next();
            logger.info("stop schedule, key:{}",entry.getKey());
            entries.remove();
        }
        return true;
    }

    @Bean
    @Override
    public boolean initSchedulePool(){
        if (threadPoolTaskScheduler==null) {
            logger.info("init schedule pool ===================");
            threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
            threadPoolTaskScheduler.initialize();
        }
        return true;
    }

    @Override
    public boolean isCanceled(){
        return true;
    }

    @Override
    public boolean isDone(){
        return true;
    }
}
