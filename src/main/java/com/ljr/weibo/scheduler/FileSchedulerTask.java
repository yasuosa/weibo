package com.ljr.weibo.scheduler;


import com.ljr.weibo.utils.AppFileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
public class FileSchedulerTask {

    private Log log = LogFactory.getLog(FileSchedulerTask.class.getSimpleName());


    /**
     * cron表达式语法
     * [秒] [分] [小时] [日] [月] [周] [年]
     */
    @Scheduled(cron="0 0 0 * * ?")
    private void deleteLastDayImgTemp(){
        log.info("开始执行----删除垃圾图片任务");
        AppFileUtils.removeAllTemp();
    }

}
