package ru.praktika95;

public class Controller {
    public static void getUserCommandsAndGiveAnswer() {
        while(true)
        {
            String[] commandAndArgument=ConsoleOperations.readUserCommand();
            String botCommand=commandAndArgument[0];
            int selectedEventNumber=Integer.parseInt(commandAndArgument[1]);
            String botAnswer=switch(botCommand)
                    {
                        case "choose"-> CommandHandler.choose(selectedEventNumber);
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
