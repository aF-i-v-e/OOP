package ru.praktika95.bot.notifier;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Slf4j
@Component
public class NotifierJob {
    private final FindNodeService findNodeService;

    @Autowired
    public NotifierJob(FindNodeServiceImpl findNodeServiceImpl) {
        this.findNodeService = findNodeServiceImpl;
    }

    @Scheduled(fixedRateString = "${interval-in-cron}")
    public void findNode() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.uuuu");
        LocalDate localDate = LocalDate.now();
        String date = dtf.format(localDate);
        findNodeService.findNode(date);
    }
}
