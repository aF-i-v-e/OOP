package ru.praktika95.bot;

public class Controller {
    public static void getBotAnswer() {
        while(true)
        {
            String[] commandAndArgument=ConsoleOperations.getBotCommand();
            String botCommand=commandAndArgument[0];
            int eventNumber=(commandAndArgument.length>1)?Integer.parseInt(commandAndArgument[1]):-1;
            String botAnswer= CommandHandler.commandHandler(botCommand,eventNumber);
            ConsoleOperations.printBotAnswer(botAnswer);
        }
    }

    public static void main(String[] args){
        getBotAnswer();
    }
}
