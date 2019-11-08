package com.appengine.deploy;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class StatisticsScheduledTask {

    /**
     * 每天0点5分存上一天的统计
     */
    @Scheduled(cron = "5 0 * * * *")
    public void saveByDay() {
//        statisticsService.saveAllChannel2MysqlByDay();
//        statisticsService.saveAllTChannel2MysqlByDay();
//        statisticsService.saveRegistNumByDay();
//        loginRecordService.saveAllChannel2MysqlByDay();
//        statisticsService.saveAppOpenNum();
    }

    /**
     * 自动排序
     */
    @Scheduled(cron = "1 */3 * * * *")
//    @Scheduled(cron = "1 * * * * *")
    public void autoSort() {
//        marketService.autoSort();
//        marketService.saveLimitNum();
    }

    /**
     * 检查掉签
     */
    @Scheduled(cron = "1 */10 * * * *")
    public void warning() {
//        warningService.checkSign();
    }

    /**
     * 每小时2分存上一小时的统计
     */
    @Scheduled(cron = "2 */1 * * * *")
    public void saveByHour() {
//        statisticsService.saveAllChannel2MysqlByHour();
//        statisticsService.saveAllTChannel2MysqlByHour();
    }


    /**
     * 是上一个调用开始后再次调用的延时（不用等待上一次调用完成）
     */
    //@Scheduled(fixedRate = 1000 * 1)
    public void fixedRate() throws Exception {
        Thread.sleep(2000);
        System.out.println("执行测试fixedRate时间：" + new Date(System.currentTimeMillis()));
    }

    /**
     * 上一个调用完成后再次调用的延时调用
     */
    //@Scheduled(fixedDelay = 1000 * 1)
    public void fixedDelay() throws Exception {
        Thread.sleep(3000);
        System.out.println("执行测试fixedDelay时间：" + new Date(System.currentTimeMillis()));
    }

    /**
     * 第一次被调用前的延时，单位毫秒
     */
    @Scheduled(initialDelay = 1000 * 20, fixedDelay = 1000 * 43200)
    public void initialDelay() {
//        statisticsService.saveAllChannel2MysqlByHour();
//        statisticsService.saveAllTChannel2MysqlByHour();
//        statisticsService.saveAllChannel2MysqlByDay();
//        statisticsService.saveAllTChannel2MysqlByDay();
//        loginRecordService.saveAllChannel2MysqlByDay();
//        statisticsService.saveRegistNumByDay();
    }
}
