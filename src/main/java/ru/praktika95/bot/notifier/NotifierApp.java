package ru.praktika95.bot.notifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class NotifierApp {
    public static void mainNotifier(String[] args) {
        SpringApplication.run(NotifierApp.class);
    }
}
