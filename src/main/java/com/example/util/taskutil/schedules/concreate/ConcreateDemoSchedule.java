package com.example.util.taskutil.schedules.concreate;

import com.example.util.taskutil.schedules.base.AbstractCustomSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ConcreateDemoSchedule extends AbstractCustomSchedule {
    private static final Logger logger = LoggerFactory.getLogger(ConcreateDemoSchedule.class);

    @Override
    public boolean saveScheduled(String corn) {
        super.saveScheduled(this, corn);
        return true;
    }

    @Override
    public void run() {
        logger.info("ConcreateDemoSchedule：schedule start！");
    }
}
