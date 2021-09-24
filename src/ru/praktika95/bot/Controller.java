package ru.praktika95.bot;

public class Controller {
    public static void getBotAnswer() {
        while(true)
        {
            ConsoleOperations console = new ConsoleOperations();
            CommandHandler handler = new CommandHandler();
            String botAnswer= handler.commandHandler(console.getBotCommand());
            console.printBotAnswer(botAnswer);
        }
    }

    public static void main(String[] args){
        getBotAnswer();
    }
}
