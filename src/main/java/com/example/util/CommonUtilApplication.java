package com.example.util;

import com.example.util.taskutil.schedules.concreate.ConcreateDemoSchedule;
import com.example.util.taskutil.schedules.constants.ScheduleCronConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommonUtilApplication implements ApplicationRunner {

    @Autowired
    private ConcreateDemoSchedule concreateDemoSchedule;

    public static void main(String[] args) {
        SpringApplication.run(CommonUtilApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        concreateDemoSchedule.saveScheduled(ScheduleCronConstants.CONCREATE_DEMO_SCHEDULE_CRON);
    }
}
