package ru.praktika95.bot;

public class Controller {
    public static void getBotAnswer() {
        ConsoleOperations console = new ConsoleOperations();
        CommandHandler handler = new CommandHandler();
        while(true)
        {
            BotResponse botResponse = handler.commandHandler(console.getBotCommand());
            console.printBotAnswer(botResponse.getMessage());
        }
    }

    public static void main(String[] args) {
        getBotAnswer();
    }
}
