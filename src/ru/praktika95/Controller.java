package ru.praktika95;

public class Controller {
    public static void getUserCommandsAndGiveAnswer() {
        while(true)
        {
            String userCommand=ConsoleOperations.readUserCommand();
            String botAnswer=switch(userCommand)
                    {
                        case "choose"-> CommandHandler.choose();
                        case "hello"-> CommandHandler.hello() ;
                        case "help"->CommandHandler.help();
                        case "show"-> CommandHandler.show();
                        case "start"-> CommandHandler.hello();
                        default -> CommandHandler.other();
                    };
            ConsoleOperations.printBotAnswer(botAnswer);
        }
    }

    public static void main(String[] args){
        getUserCommandsAndGiveAnswer();
    }
}
