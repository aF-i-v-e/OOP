package ru.praktika95.bot.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzJobScheduler {

    public static void startQuartzApp() throws SchedulerException {
        JobDetail notifyJob = new QuartzJobDetail("notify","notifyJob", "group1").getJob();
        Trigger notifyTrigger = new QuartzTrigger("notifyJob", "group1",
                "triggerN", "group1", "0 1 20 * * ?").getTrigger();

        JobDetail deleteOldNotesJob = new QuartzJobDetail("delete","deleteJob", "group2").getJob();
        Trigger deleteTrigger = new QuartzTrigger("deleteJob", "group2",
                "triggerD", "group2", "0 0 6 * * ?").getTrigger();

        SchedulerFactory schFactory = new StdSchedulerFactory();
        Scheduler sch = schFactory.getScheduler();
        sch.start();
        sch.scheduleJob(deleteOldNotesJob, deleteTrigger);
        sch.scheduleJob(notifyJob, notifyTrigger);
    }
}
