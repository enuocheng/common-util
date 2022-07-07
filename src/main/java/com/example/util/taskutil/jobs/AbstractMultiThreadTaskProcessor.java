package com.example.util.taskutil.jobs;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Abstract multi-thread task processor
 */

@Slf4j
public abstract class AbstractMultiThreadTaskProcessor implements MultiThreadTaskProcessor {

    /**
     * How many organization IDs at most in queue
     */
    @Getter
    protected int taskQueueSize = 100;

    /**
     * Number of task threads
     */
    @Getter
    protected int taskThreadCount = 1;

    /**
     * How many tasks fetch at most at a time
     */
    protected int taskFetchSize=100;

    /**
     * Organization ID queue
     */
    protected BlockingQueue<Long> orgIdQueue;

    /**
     * Task threads
     */
    protected Thread[] taskThreads;

    /**
     * Holder for the organizations in process. Both key and value are OrgInfo.id
     */
    protected volatile ConcurrentMap<Long, Long> inProcessOrgMap = new ConcurrentHashMap<>();


    protected abstract List<Long> searchPendingOrgIds();


    @Override
    public synchronized void start() {
        String taskProcessorName = getClass().getSimpleName();
        log.info("Starting {}...", taskProcessorName);

        if (isStarted()) {
            log.warn("{} is running. You MUST stop it first before restarting it.", taskProcessorName);
            return;
        }

        orgIdQueue = new LinkedBlockingQueue<>(taskQueueSize);
        TaskRunner taskRunner = new TaskRunner(orgIdQueue);
        taskThreads = new Thread[taskThreadCount];

        for (int i = 0; i < taskThreads.length; i++) {
            taskThreads[i] = new Thread(taskRunner, taskProcessorName + "Thread" + (i + 1));
            taskThreads[i].start();
        }

        log.info("{} started with {} threads.", taskProcessorName, taskThreads.length);
    }

    @PreDestroy
    @Override
    public synchronized void stop() {
        String taskProcessorName = getClass().getSimpleName();
        log.info("Stopping {}...", taskProcessorName);

        if (isStarted()) {
            for (Thread taskThread : taskThreads) {
                if (taskThread.isAlive()) {
                    taskThread.interrupt();
                }
            }

            log.info("{} shutdown.", taskProcessorName);
        }
        else {
            log.info("{} is not running or has shut down.", taskProcessorName);
        }
    }

    @Override
    public boolean isStarted() {
        boolean started = false;

        if (taskThreads != null) {
            for (Thread taskThread : taskThreads) {
                if (taskThread.isAlive()) {
                    started = true;
                    break;
                }
            }
        }

        return started;
    }

    @Override
    public synchronized void scan() {
        if (!orgIdQueue.isEmpty()) {
            //    log.debug("Skipped scanning pending tasks because queue was not empty.");
            return;
        }

        searchAndAddToQueue();
    }

    @Override
    public synchronized void scan(final Long orgId) {
        if (!orgIdQueue.isEmpty()) {
            //    log.debug("Skipped scanning pending tasks because queue was not empty.");
            return;
        }

        // 当前机构正在被处理，不扫描
        if (inProcessOrgMap.containsKey(orgId)) {
            //    log.debug("Skipped scanning pending tasks because there was one in process for organization {}.", orgId);
            return;
        }

        searchAndAddToQueue();
    }

    private void searchAndAddToQueue() {
        try {
            searchPendingOrgIds().forEach(this::add);
        }
        catch (final Exception e) {
            log.error("An error occurred while searching pending organization IDs.", e);
        }
    }

    private boolean add(final Long orgId) {
        if (orgId == null) {
            return false;
        }

        if (orgIdQueue.contains(orgId)) {
            log.warn("Skipped adding organization ID {} to queue because it was already in it.", orgId);
            return false;
        }

        boolean added = orgIdQueue.offer(orgId);

        if (!added) {
            log.warn("Unable to add organization ID {} to queue because queue was full.", orgId);
        }

        return added;
    }


    class TaskRunner implements Runnable {

        final BlockingQueue<Long> orgIdQueue;

        TaskRunner(BlockingQueue<Long> orgIdQueue) {
            this.orgIdQueue = orgIdQueue;
        }


        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    Long orgId = orgIdQueue.take();
                    inProcessOrgMap.putIfAbsent(orgId, orgId);
                    boolean processed;

                    try {
                        processed = process(orgId);
                    }
                    finally {
                        inProcessOrgMap.remove(orgId);
                    }

                    // The current thread is available, try to trigger a new scan
                    if (processed && orgIdQueue.isEmpty()) {
                        scan();
                    }
                }
            }
            catch (final InterruptedException e) {
                log.error("Task thread {} interrupted.", Thread.currentThread().getName());
                return;
            }

            log.info("Task thread {} has been interrupted.", Thread.currentThread().getName());
        }
    }

}
