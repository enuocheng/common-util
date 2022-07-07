package com.example.util.taskutil.schedules.base;

/**
 * 定时任务
 * @author luowx on 2018/12/27 0027.
 */
public interface CustomSchedule<T> {


    boolean saveScheduled(String corn);

    boolean saveScheduled(Runnable runnable,String cron);

    boolean stopSchedule(String scheduleName);

    boolean stopAllSchedule();

    boolean initSchedulePool();

    boolean isCanceled();

    boolean isDone();
}
