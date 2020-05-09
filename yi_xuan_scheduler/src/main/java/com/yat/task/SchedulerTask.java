package com.yat.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>Description: 定时任务 </p>
 *
 * @Created with IDEA
 * @author: Yat
 * @Date: 2020/5/9
 * @Time: 10:18
 */
@Component
public class SchedulerTask {

    private int count = 0;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");

    /**
     * 当前任务：6秒执行一次
     * <p>
     * 具体参考：http://note.youdao.com/noteshare?id=0572475e45a3629ef0c4f4bac49d8086
     * <p>
     * corn从左到右（用空格隔开）：秒 分 小时 月份中的日期 月份 星期中的日期 年份
     * <p>
     * 常用表达式例子
     * <p>
     * （1）0 0 2 1 * ? *   表示在每月的1日的凌晨2点调整任务
     * <p>
     * （2）0 15 10 ? * MON-FRI   表示周一到周五每天上午10:15执行作业
     * <p>
     * （3）0 15 10 ? 6L 2002-2006   表示2002-2006年的每个月的最后一个星期五上午10:15执行作
     * <p>
     * （4）0 0 10,14,16 * * ?   每天上午10点，下午2点，4点
     * <p>
     * （5）0 0/30 9-17 * * ?   朝九晚五工作时间内每半小时
     * <p>
     * （6）0 0 12 ? * WED    表示每个星期三中午12点
     * <p>
     * （7）0 0 12 * * ?   每天中午12点触发
     * <p>
     * （8）0 15 10 ? * *    每天上午10:15触发
     * <p>
     * （9）0 15 10 * * ?     每天上午10:15触发
     * <p>
     * （10）0 15 10 * * ? *    每天上午10:15触发
     * <p>
     * （11）0 15 10 * * ? 2005    2005年的每天上午10:15触发
     * <p>
     * （12）0 * 14 * * ?     在每天下午2点到下午2:59期间的每1分钟触发
     * <p>
     * （13）0 0/5 14 * * ?    在每天下午2点到下午2:55期间的每5分钟触发
     * <p>
     * （14）0 0/5 14,18 * * ?     在每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发
     * <p>
     * （15）0 0-5 14 * * ?    在每天下午2点到下午2:05期间的每1分钟触发
     * <p>
     * （16）0 10,44 14 ? 3 WED    每年三月的星期三的下午2:10和2:44触发
     * <p>
     * （17）0 15 10 ? * MON-FRI    周一至周五的上午10:15触发
     * <p>
     * （18）0 15 10 15 * ?    每月15日上午10:15触发
     * <p>
     * （19）0 15 10 L * ?    每月最后一日的上午10:15触发
     * <p>
     * （20）0 15 10 ? * 6L    每月的最后一个星期五上午10:15触发
     * <p>
     * （21）0 15 10 ? * 6L 2002-2005   2002年至2005年的每月的最后一个星期五上午10:15触发
     * <p>
     * （22）0 15 10 ? * 6#3   每月的第三个星期五上午10:15触发
     */
    @Scheduled(cron = "*/6 * * * * ?")
    private void process() {
        System.out.println("SchedulerTask:this is scheduler task runing  " + (count++));
        System.out.println("SchedulerTask:现在时间：" + DATE_FORMAT.format(new Date()));
    }
}
