package ru.praktika95.bot;
import java.util.Scanner;

public class ConsoleOperations {
    private final Scanner scanner = new Scanner(System.in);

    public String[] getBotCommand() {
        String userInput = scanner.nextLine();
        return userInput.split(" ");
    }

    public void printBotAnswer(String botAnswer) {
        System.out.println(botAnswer);
    }
}
