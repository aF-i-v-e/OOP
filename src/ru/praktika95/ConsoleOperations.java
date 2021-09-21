package ru.praktika95;
import java.util.Scanner;

public class ConsoleOperations {
    public static String[] readUserCommand() {
        String userInput= new Scanner(System.in).nextLine();
        String[] commandAndArgument=userInput.split(" ");
        return commandAndArgument;
    }

    public static void printBotAnswer(String botAnswer) {
        System.out.println(botAnswer);
    }
}
