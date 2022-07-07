package com.example.util.taskutil.schedules.constants;

/**
 * @author luowx on 2019/3/19 0019.
 */
public class ScheduleCronConstants {

    /**
     * 删除缓存定时任务cron表达式
     * 每个月的10号凌晨1点10分30秒执行
     */
    public static final String DELETE_CACHE_SCHEDULE_CRON = "30 10 1 28 * ?";
//    public static final String DELETE_CACHE_SCHEDULE_CRON = "30 50 10 24 * ?";

    /**
     * 数据备份定时任务cron表达式
     * 每天凌晨1点33分30秒执行
     * 20191015修改：每周一凌晨1点33分30秒执行
     */
    public static final String BACKUPS_DATA_SCHEDULE_CRON = "30 33 1 ? * MON";
//    public static final String BACKUPS_DATA_SCHEDULE_CRON = "0 */3 * * * ?";

    /**
     * 删除过期或无用数据定时任务cron表达式
     * 每天凌晨4点30分30秒执行
     */
    public static final String DELETE_EXPIRE_DATA_SCHEDULE_CRON = "30 30 4 * * ?";

    /**
     * 删除超出内存限制百分比数据定时任务cron表达式
     * 每天凌晨2点30分30秒执行
     */
    public static final String DELETE_OVERFLOW_DATA_SCHEDULE_CRON = "30 30 2 * * ?";

    /**
     * 拉取设备前一天日志定时任务cron表达式
     * 每天凌晨3点30分30秒执行
     */
    public static final String PULL_DEVICE_LOG_SCHEDULE_CRON = "30 30 3 * * ?";

    /**
     * 删除无效人脸定时任务cron表达式
     * 每周一凌晨1点执行
     */
    public static final String DELETE_INVALID_FACE_IMAGE_SCHEDULE_CRON = "0 0 1 ? * MON";
//    public static final String DELETE_INVALID_FACE_IMAGE_SCHEDULE_CRON = "0 */3 * * * ?";

    /**
     * 删除无效缓存数据定时任务cron表达式
     * 每3执行
     */
    public static final String DELETE_CACHE_DATA_SCHEDULE_CRON = "*/30 * * * * ?";

    /**
     * 校验app授权是否过期定时任务cron表达式
     * 每天凌晨0点00分30秒执行
     */
    public static final String APP_VALID_SCHEDULE_CRON = "30 0 0 * * ?";

    /**
     * 访客是否过期定时任务cron表达式
     * 每天凌晨0点00分30秒执行
     */
    public static final String EXTERNAL_CONTACT_VALID_SCHEDULE_CRON = "30 0 0 * * ?";
//    public static final String EXTERNAL_CONTACT_VALID_SCHEDULE_CRON = "0 */5 * * * ?";

    /**
     * 万雍人脸库图片变更同步更新
     */
    public static final String CONCREATE_DEMO_SCHEDULE_CRON = "*/5 * * * * ?";
}
