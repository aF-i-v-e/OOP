package ru.praktika95;
import java.util.Scanner;

public class ConsoleOperations {
    public static String readUserCommand() {
        String userCommand= new Scanner(System.in).nextLine();
        return userCommand;
    }

    public static void printBotAnswer(String botAnswer) {
        System.out.println(botAnswer);
    }
}
