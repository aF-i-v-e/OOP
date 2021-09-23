package ru.praktika95.bot;
import java.util.Scanner;

public class ConsoleOperations {
    public String[] getBotCommand() {
        String userInput= new Scanner(System.in).nextLine();
        return userInput.split(" ");
    }

    public void printBotAnswer(String botAnswer) {
        System.out.println(botAnswer);
    }
}
