package ru.praktika95;

public class Controller {
    public static String getBotAnswer() {
        while(true)
        {
            String[] commandAndArgument=ConsoleOperations.getBotCommand();
            String botCommand=commandAndArgument[0];
            int eventNumber=(commandAndArgument.length>1)?Integer.parseInt(commandAndArgument[1]):0;
            return CommandHandler.commandHandler(botCommand,eventNumber);
        }
    }

    public static void main(String[] args){
        String botAnswer= getBotAnswer();
        ConsoleOperations.printBotAnswer(botAnswer);
    }
}
