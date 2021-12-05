package ru.praktika95.bot.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import ru.praktika95.bot.service.DataBaseWorkService;
import ru.praktika95.bot.service.TimeService;

public class DeleteOldNotesJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("QuartzDeleteOldNotesJob run successfully.");
        String date = TimeService.getCurrentTime();
        DataBaseWorkService.deleteOldNotes(date);
    }
}
