package ru.praktika95.bot;
import java.util.Scanner;

public class ConsoleOperations {
    public static String[] getBotCommand() {
        String userInput = new Scanner(System.in).nextLine();
        return userInput.split(" ");
    }

    public static void printBotAnswer(String botAnswer) {
        System.out.println(botAnswer);
    }
}
