package com.example.util.taskutil.jobs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class DemoTaskProcessor extends AbstractMultiThreadTaskProcessor{
    //处理线程数
    private int numberOfThreads = 1;


    @PostConstruct
    @Override
    public synchronized void start() {
        taskThreadCount = numberOfThreads;
        super.start();
    }

    @Override
    protected List<Long> searchPendingOrgIds() {
        //把任务放在一个库里面，然后从库里面取出数据，去处理
        List<Long> inProcessOrgIdList = new ArrayList<>(inProcessOrgMap.keySet());
        inProcessOrgIdList.add(100L);
        inProcessOrgIdList.add(101L);
        inProcessOrgIdList.add(102L);
        inProcessOrgIdList.add(103L);
        return inProcessOrgIdList;
    }

    @Override
    public boolean process(Long orgId) {
        log.info("process start !!!");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return true;
    }
}
