package ru.praktika95.bot.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.TriggerBuilder;

public class QuartzTrigger {

    private final CronTrigger trigger;

    public QuartzTrigger(String jobName, String jobGroup, String triggerName, String triggerGroup, String cronExpr) {
        trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerName, triggerGroup)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpr))
                .forJob(jobName, jobGroup)
                .build();
    }

    public CronTrigger getTrigger() {
        return trigger;
    }
}
