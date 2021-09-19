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
                        case "start"-> CommandHandler.start();
                        default -> {
                            String firstPartBotAnswer=String.format("Unexpected command: %s",userCommand);
                            String secondPartBptAnswer="To view the available commands, type \"help\"";
                            yield firstPartBotAnswer+'\n'+secondPartBptAnswer;
                        }
                    };
            ConsoleOperations.printBotAnswer(botAnswer);
        }
    }

    public static void main(String[] args){
        getUserCommandsAndGiveAnswer();
    }
}
