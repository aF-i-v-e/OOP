package ru.praktika95.bot;

public class Controller {
    public static void getBotAnswer() {
        while(true)
        {
            ConsoleOperations consoleOp = new ConsoleOperations();
            CommandHandler comh = new CommandHandler();
            String botAnswer= comh.commandHandler(commandAndArgument);
            consoleOp.printBotAnswer(botAnswer);
        }
    }

    public static void main(String[] args){
        getBotAnswer();
    }
}
