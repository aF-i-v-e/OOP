package ru.praktika95.bot.quartz;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;

public class QuartzJobDetail {

    private final JobDetail job;

    public QuartzJobDetail(String job, String name, String group) {
        if ("notify".equals(job)) {
            this.job = JobBuilder.newJob(NotifyUsersJob.class)
                    .withIdentity(name, group)
                    .build();
        }
        else
        {
            this.job = JobBuilder.newJob(DeleteOldNotesJob.class)
                    .withIdentity(name, group)
                    .build();
        }
    }

    public JobDetail getJob() {
        return job;
    }

}
