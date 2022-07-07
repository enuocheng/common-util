1、实现接口ApplicationRunner，run方法中加载

public class CommonUtilApplication implements ApplicationRunner
@Override
public void run(ApplicationArguments args) throws Exception {
concreateDemoSchedule.saveScheduled(ScheduleCronConstants.CONCREATE_DEMO_SCHEDULE_CRON);
}

