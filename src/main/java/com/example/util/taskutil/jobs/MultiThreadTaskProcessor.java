package com.example.util.taskutil.jobs;

/**
 * Multi-thread task processor
 */

public interface MultiThreadTaskProcessor {

    int getTaskQueueSize();

    int getTaskThreadCount();

    void start();

    void stop();

    boolean isStarted();

    void scan();

    void scan(Long orgId);

    boolean process(Long orgId);

}
